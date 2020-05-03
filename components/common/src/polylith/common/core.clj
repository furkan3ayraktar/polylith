(ns polylith.common.core
  (:require [clojure.core.async :refer [<! go-loop chan close!]]
            [clojure.java.shell :as shell]
            [clojure.string :as str]
            [clojure.tools.deps.alpha :as tools-deps]
            [clojure.tools.deps.alpha.util.maven :as mvn]
            [polylith.file.interface :as file])
  (:import (java.util.concurrent ExecutorService Executors)))

(def alias-nses #{"service" "env"})
(def alias-nses-with-tests (into alias-nses #{"service.test" "env.test"}))

(def number-of-processors
  (memoize
    (fn []
      (max (int (/ (.availableProcessors (Runtime/getRuntime)) 2)) 1))))

(defmacro execute-in [pool body]
  "Executes the body in a separate thread with using the given thread pool."
  `(.submit ^ExecutorService ~pool
            ^Callable (fn [] ~@body)))

(defn ^ExecutorService create-thread-pool [size]
  (Executors/newFixedThreadPool (or size (number-of-processors))))

(defn create-print-channel []
  (let [ch (chan 1)]
    (go-loop []
      (let [message (<! ch)]
        (if (= :done message)
          (do
            (close! ch))
          (do
            (println message)
            (recur)))))
    ch))

(defn extract-aliases
  ([deps service-or-env include-tests?]
   (let [polylith-nses (if include-tests? alias-nses-with-tests alias-nses)
         alias-keys    (when service-or-env (into #{} (map #(keyword % service-or-env) polylith-nses)))
         aliases       (filter #(if alias-keys
                                  (contains? alias-keys (key %))
                                  (contains? polylith-nses (-> % key namespace)))
                               (:aliases deps))]
     (doall aliases)))
  ([deps service-or-env]
   (extract-aliases deps service-or-env false))
  ([deps]
   (extract-aliases deps nil)))

(defn extract-extra-deps [deps service-or-env include-tests? additional-deps]
  (let [aliases     (extract-aliases deps service-or-env include-tests?)
        alias-deps  (map #(-> % val (select-keys [:extra-deps])) aliases)
        merged-deps (apply merge-with merge alias-deps)]
    (merge additional-deps
           (:extra-deps merged-deps))))

(defn extract-source-paths
  ([ws-path deps service-or-env include-tests?]
   (let [aliases        (extract-aliases deps service-or-env include-tests?)
         paths          (into #{} (mapcat #(-> % val :extra-paths) aliases))
         absolute-paths (mapv #(str ws-path (if (str/starts-with? % "/") "" "/") %) paths)]
     absolute-paths))
  ([ws-path deps service-or-env]
   (extract-source-paths ws-path deps service-or-env false)))

(defn resolve-libraries
  ([{:keys [mvn/repos] :as deps} service-or-env include-tests? additional-deps]
   (let [mvn-repos  (merge mvn/standard-repos repos)
         deps       (assoc deps :mvn/repos mvn-repos)
         extra-deps (extract-extra-deps deps service-or-env include-tests? additional-deps)]
     (tools-deps/resolve-deps deps {:extra-deps extra-deps})))
  ([deps service-or-env include-tests?]
   (resolve-libraries deps service-or-env include-tests? nil))
  ([deps service-or-env]
   (resolve-libraries deps service-or-env false nil))
  ([deps]
   (resolve-libraries deps nil false nil)))

(defn make-classpath [libraries source-paths]
  (tools-deps/make-classpath libraries source-paths nil))

(defn run-in-jvm [classpath expression dir ex-msg]
  (let [{:keys [exit err out]} (shell/sh "java" "-cp" classpath "clojure.main" "-e" expression :dir dir)]
    (if (= 0 exit)
      out
      (throw (ex-info ex-msg {:err err :exit-code exit})))))

(defn filter-paths [all paths prefix]
  (filterv #(contains? all %)
           (into #{} (map #(-> %
                               (str/replace prefix "")
                               (str/split #"\/")
                               (second))
                          (filter #(str/starts-with? % prefix) paths)))))

(defn all-bases
  ([ws-path paths]
   (let [prefix    (str ws-path "/bases")
         all-bases (file/directory-names prefix)]
     (if paths
       (filter-paths all-bases paths prefix)
       all-bases)))
  ([ws-path]
   (all-bases ws-path nil)))

(defn all-components
  ([ws-path paths]
   (let [prefix         (str ws-path "/components")
         all-components (file/directory-names prefix)]
     (if paths
       (filter-paths all-components paths prefix)
       all-components)))
  ([ws-path]
   (all-components ws-path nil)))
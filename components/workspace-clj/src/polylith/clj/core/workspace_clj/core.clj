(ns polylith.clj.core.workspace-clj.core
  (:require [polylith.clj.core.common.interfc :as common]
            [polylith.clj.core.file.interfc :as file]
            [polylith.clj.core.util.interfc :as util]
            [polylith.clj.core.util.interfc.color :as color]
            [polylith.clj.core.user-config.interfc :as user-config]
            [polylith.clj.core.workspace-clj.profile :as profile]
            [polylith.clj.core.workspace-clj.bases-from-disk :as bases-from-disk]
            [polylith.clj.core.workspace-clj.environment-from-disk :as envs-from-disk]
            [polylith.clj.core.workspace-clj.components-from-disk :as components-from-disk]))

(def ws-reader
  {:name "polylith-clj"
   :project-url "https://github.com/tengstrand/polylith/tree/core"
   :reader-version "1.0"
   :ws-contract-version 1
   :language "Clojure"
   :type-position "postfix"
   :slash "/"
   :file-extensions [".clj" "cljc"]})

(defn stringify-key-value [[k v]]
  [(str k) (str v)])

(defn stringify [ns->lib]
  (into {} (mapv stringify-key-value ns->lib)))

(defn read-config-file [ws-dir]
  (try
    (read-string (slurp (str ws-dir "/deps.edn")))
    (catch Exception e
      (println (str (color/error (user-config/color-mode) "  Couldn't read 'deps.edn': ") (.getMessage e)))
      (System/exit 1))))

(defn workspace-from-disk
  ([ws-dir]
   (let [config (read-config-file ws-dir)]
     (workspace-from-disk ws-dir config)))
  ([ws-dir {:keys [polylith aliases]}]
   (let [{:keys [vcs top-namespace interface-ns env->alias ns->lib]} polylith
         top-src-dir (-> top-namespace common/suffix-ns-with-dot common/ns-to-path)
         color-mode (user-config/color-mode)
         thousand-sep (user-config/thousand-separator)
         component-names (file/directory-paths (str ws-dir "/components"))
         components (components-from-disk/read-components ws-dir top-src-dir component-names interface-ns)
         bases (bases-from-disk/read-bases ws-dir top-src-dir)
         environments (envs-from-disk/read-environments ws-dir)
         profile->settings (profile/profile->settings aliases)
         settings (util/ordered-map :vcs (or vcs "git")
                                    :top-namespace top-namespace
                                    :interface-ns (or interface-ns "interface")
                                    :color-mode color-mode
                                    :thousand-sep thousand-sep
                                    :profile->settings profile->settings
                                    :env->alias env->alias
                                    :ns->lib (stringify ns->lib))]
     (util/ordered-map :ws-dir ws-dir
                       :ws-reader ws-reader
                       :settings settings
                       :components components
                       :bases bases
                       :environments environments))))

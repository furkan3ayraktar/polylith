(ns dev.furkan
  (:require [polylith.clj.core.user-input.interface :as user-input]
            [polylith.clj.core.common.interface :as common]
            [polylith.clj.core.command.interface :as command]
            [clojure.java.io :as io]
            [zprint.core :as zprint]))

(def params (user-input/extract-params []))
(def color-mode (common/color-mode params))
(def ws-dir (common/workspace-dir params color-mode))
(def workspace (command/read-workspace ws-dir params))

(def development-project (->> workspace :projects (filter #(= "development" (:name %))) first))

(def lib-deps (:lib-deps development-project))

(defn has-directory? [type name directory]
  (let [resources-path (str ws-dir "/" type "s/" name "/" directory)
        resources-file (io/file resources-path)]
    (and (.exists resources-file) (.isDirectory resources-file))))

(defn dep-dispatcher [{:keys [type]}]
  type)

(defmulti dep->dep-data dep-dispatcher)

(defmethod dep->dep-data "maven" [{:keys [version]}]
  (when version
    {:mvn/version version}))

(defmethod dep->dep-data :default [dep]
  (when dep
    (dissoc dep :type :size :version :path)))

(defn dep-reducer [acc dep-name]
  (let [dep (get lib-deps dep-name nil)
        dep-data (dep->dep-data dep)]
    (if dep-data
      (assoc acc (symbol dep-name) dep-data)
      acc)))

(defn create-deps-file-for-brick! [{:keys [name type lib-dep-names]}]
  (let [has-resources? (has-directory? type name "resources")
        has-test? (has-directory? type name "test")
        deps (reduce dep-reducer {} lib-dep-names)
        deps-file-content (cond-> {:paths ["src"]
                                     :deps  deps}

                                  has-resources?
                                  (update :paths (conj "resources"))

                                  has-test?
                                  (assoc :aliases {:test {:extra-paths ["test"]}}))
        file-name "deps.edn"
        path (str ws-dir "/" type "s/" name "/deps.edn")]
    ;; FIXME: Couldn't find how to keep keys in deps map as symbols when printed.
    ;;        The zprint call below converts symbols to keywords when they are written to file.
    (spit path (zprint/zprint-file-str (str deps-file-content "\n")
                                       file-name
                                       {:width  60
                                        :map    {:comma? false
                                                 :unlift-ns? true}
                                        :vector {:respect-nl? true
                                                 :wrap-coll?  false}}))))

;(create-deps-file-for-brick! (-> workspace :components (nth 5)))

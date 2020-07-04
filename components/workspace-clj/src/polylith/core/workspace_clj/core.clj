(ns polylith.core.workspace-clj.core
  (:require [polylith.core.util.interfc :as util]
            [polylith.core.workspace-clj.environment :as env]
            [polylith.core.workspace-clj.namespace :as namespace]
            [polylith.core.workspace-clj.bases-from-disk :as bases-from-disk]
            [polylith.core.workspace-clj.components-from-disk :as components-from-disk]))

(defn workspace-from-disk
  ([ws-path]
   (let [config (read-string (slurp (str ws-path "/deps.edn")))]
     (workspace-from-disk ws-path config)))
  ([ws-path {:keys [polylith]}]
   (let [{:keys [top-namespaces color-mode env-short-names]} polylith
         top-src-dirs (namespace/top-src-dirs top-namespaces)
         components (components-from-disk/read-components ws-path top-src-dirs)
         bases (bases-from-disk/read-bases ws-path top-src-dirs)
         environments (env/environments ws-path)
         settings (util/ordered-map :top-namespaces top-namespaces
                                    :color-mode color-mode
                                    :env-short-names env-short-names)]
     (util/ordered-map :ws-path ws-path
                       :settings settings
                       :components components
                       :bases bases
                       :environments environments))))

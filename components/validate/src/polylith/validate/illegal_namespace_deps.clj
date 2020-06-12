(ns polylith.validate.illegal-namespace-deps
  (:require [polylith.deps.interface :as deps]))

(defn error-message [{:keys [ns-path depends-on-interface depends-on-ns]} type]
  (when ns-path
    (str "Illegal dependency on namespace '" depends-on-interface "." depends-on-ns "' in '" type "s/" ns-path
         "'. Use '" depends-on-interface ".interface' instead to fix the problem.")))

(defn brick-errors [top-ns {:keys [interface type imports]} interface-names errors]
  "Checks for dependencies to component interface namespaces other than 'interface'."
  (let [interface-name (:name interface)
        dependencies (deps/brick-dependencies top-ns interface-name interface-names imports)
        error-messages (filterv identity (map #(error-message % type)
                                              (filterv #(not= "interface" (:depends-on-ns %)) dependencies)))]
    (vec (concat errors error-messages))))

(defn errors [top-ns interface-names components bases]
  (vec (mapcat #(brick-errors top-ns % interface-names [])
               (concat components bases))))
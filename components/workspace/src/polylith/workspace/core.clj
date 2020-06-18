(ns polylith.workspace.core
  (:require [clojure.tools.deps.alpha.util.maven :as mvn]
            [polylith.common.interface :as common]
            [polylith.deps.interface :as deps]
            [polylith.validate.interface :as validate]
            [polylith.workspace.calculate-interfaces :as ifcs]
            [polylith.workspace.lib-imports :as lib-imports]
            [polylith.util.interface :as util]
            [polylith.file.interface :as file]
            [polylith.workspace-clj.interface :as ws-clojure]))

(defn brick-loc [namespaces]
  (apply + (mapv #(file/lines-of-code %)
                 (mapv :file-path namespaces))))

(defn env-loc [brick-names brick->loc]
  (let [locs (map brick->loc brick-names)]
    {:loc-src (apply + (map :loc-src locs))
     :loc-test (apply + (map :loc-test locs))}))

(defn enrich-component [top-ns interface-names {:keys [name type namespaces test-namespaces interface] :as component}]
  (let [interface-deps (deps/brick-interface-deps top-ns interface-names component)
        lib-imports (lib-imports/lib-imports top-ns interface-names component)]
    (array-map :name name
               :type type
               :loc-src (brick-loc namespaces)
               :loc-test (brick-loc test-namespaces)
               :interface interface
               :namespaces namespaces
               :test-namespaces test-namespaces
               :lib-imports lib-imports
               :interface-deps interface-deps)))

(defn enrich-base [top-ns interface-names {:keys [name type namespaces test-namespaces] :as base}]
  (let [interface-deps (deps/brick-interface-deps top-ns interface-names base)
        lib-imports (lib-imports/lib-imports top-ns interface-names base)]
    (array-map :name name
               :type type
               :loc-src (brick-loc namespaces)
               :loc-test (brick-loc test-namespaces)
               :namespaces namespaces
               :test-namespaces test-namespaces
               :lib-imports lib-imports
               :interface-deps interface-deps)))

(defn enrich-env [{:keys [name group test? type components bases paths deps]}
                  brick->lib-imports
                  brick->loc]
  (let [brick-names (concat components bases)
        lib-imports (mapcat brick->lib-imports brick-names)
        {:keys [loc-src loc-test]} (env-loc brick-names brick->loc)]
    (util/ordered-map :name name
                      :group group
                      :test? test?
                      :type type
                      :loc-src loc-src
                      :loc-test loc-test
                      :components components
                      :bases bases
                      :paths paths
                      :lib-imports (vec (sort (set lib-imports)))
                      :deps deps)))

(defn brick->lib-imports [brick]
  (into {} (mapv (juxt :name :lib-imports) brick)))

(defn enrich-settings [{:keys [maven-repos] :as settings}]
  (assoc settings :maven-repos (merge mvn/standard-repos maven-repos)))

(defn enrich-workspace [{:keys [ws-path settings components bases environments]}]
  (let [top-ns (common/top-namespace (:top-namespace settings))
        interfaces (ifcs/interfaces components)
        interface-names (apply sorted-set (mapv :name interfaces))
        enriched-components (mapv #(enrich-component top-ns interface-names %) components)
        enriched-bases (mapv #(enrich-base top-ns interface-names %) bases)
        enriched-bricks (concat enriched-components enriched-bases)
        enriched-interfaces (deps/interface-deps interfaces enriched-components)
        brick->lib-imports (brick->lib-imports enriched-bricks)
        brick->loc (into {} (map (juxt :name #(select-keys % [:loc-src :loc-test])) enriched-bricks))
        enriched-environments (mapv #(enrich-env % brick->lib-imports brick->loc) environments)
        enriched-settings (enrich-settings settings)
        warnings (validate/warnings interfaces components)
        errors (validate/errors top-ns interface-names enriched-interfaces enriched-components bases)]
    (array-map :ws-path ws-path
               :settings enriched-settings
               :interfaces enriched-interfaces
               :components enriched-components
               :bases enriched-bases
               :environments enriched-environments
               :messages {:warnings warnings
                          :errors   errors})))

(def workspace (-> "../clojure-polylith-realworld-example-app"
                   ws-clojure/workspace-from-disk
                   enrich-workspace))

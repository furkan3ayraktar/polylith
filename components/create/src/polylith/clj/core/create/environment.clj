(ns polylith.clj.core.create.environment
  (:require [polylith.clj.core.file.interfc :as file]
            [polylith.clj.core.util.interfc :as util]
            [polylith.clj.core.util.interfc.color :as color]))

(defn str-attr [attr color-mode]
  (color/yellow color-mode (str "\"" attr "\"")))

(defn env-key [env alias color-mode]
  (color/purple color-mode (str (str-attr env color-mode) " " (str-attr alias color-mode))))

(defn create-env [env color-mode]
  (let [alias (subs env 0 1)
        path (str (file/current-path) "/environments/" env)]
    (file/create-dir path)
    (file/create-file (str path "/deps.edn")
                      [""
                       (str "{:paths []")
                       ""
                       (str " :deps {org.clojure/clojure {:mvn/version \"1.10.1\"}")
                       (str "        org.clojure/tools.deps.alpha {:mvn/version \"0.8.695\"}}")
                       ""
                       (str " :aliases {:test {:extra-paths []")
                       (str "                  :extra-deps  {}}}}")])
    (println (str "Feel free to add a short name for the " (color/environment env color-mode) " environment "
                  "to the :env-short-names key in deps.edn, e.g.: "
                  "{" (env-key env alias color-mode) "}"))))

(defn create [{:keys [environments settings]} env]
  (let [color-mode (:color-mode settings color/none)]
    (if (util/find-first #(= env (:name %)) environments)
      (println (str "Environment " (color/environment env color-mode) " already exists."))
      (create-env env color-mode))))

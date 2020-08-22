(ns polylith.clj.core.workspace.text-table.info-tables
  (:require [clojure.string :as str]
            [polylith.clj.core.common.interfc :as common]
            [polylith.clj.core.util.interfc.color :as color]
            [polylith.clj.core.workspace.text-table.count-table :as count-table]
            [polylith.clj.core.workspace.text-table.env-table :as env-table]
            [polylith.clj.core.workspace.text-table.ws-table :as ws-table]))

(defn print-active-dev-profiles [{:keys [active-dev-profiles]} {:keys [color-mode]}]
  (println (str "  Active dev profiles: " (color/profile (str/join ", " (sort active-dev-profiles)) color-mode))))

(defn print-info [{:keys [settings messages user-input] :as workspace} show-loc? show-resources?]
  (count-table/print-table workspace)
  (println)
  (print-active-dev-profiles user-input settings)
  (println)
  (env-table/print-table workspace show-loc? show-resources?)
  (println)
  (ws-table/print-table workspace show-loc? show-resources?)
  (when (-> messages empty? not)
    (println)
    (println (common/pretty-messages messages (-> workspace :settings :color-mode)))))

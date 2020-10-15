(ns polylith.clj.core.deps.text-table.brick-deps-table
  (:require [polylith.clj.core.deps.text-table.shared :as shared]
            [polylith.clj.core.common.interface :as common]
            [polylith.clj.core.text-table.interface :as text-table]
            [polylith.clj.core.deps.brick-deps :as brick-deps]
            [polylith.clj.core.util.interface.color :as color]))

(defn table [workspace project brick]
  (let [color-mode (-> workspace :settings :color-mode)
        brick-name (:name brick)
        brick->color (shared/brick->color workspace)
        {:keys [dependers dependees]} (brick-deps/deps project brick->color brick-name)
        used-by-column (shared/deps-column 1 "used by" dependers)
        uses-column (shared/deps-column 9 "uses" dependees)
        headers (shared/brick-headers brick color-mode)
        spaces (text-table/spaces 1 [2 4 6 8] (repeat "  "))]
    (text-table/table "  " color-mode used-by-column uses-column headers spaces)))

(defn validate [project-name brick-name project brick color-mode]
  (cond
    (nil? project) [false (str "  Couldn't find the " (color/project project-name color-mode) " project.")]
    (nil? brick) [false (str "  Couldn't find brick '" brick-name "'.")]
    :else [true]))

(defn print-table [{:keys [projects] :as workspace} project-name brick-name]
  (let [color-mode (-> workspace :settings :color-mode)
        project (common/find-project project-name projects)
        brick (common/find-brick brick-name workspace)
        [ok? message] (validate project-name brick-name project brick color-mode)]
    (if ok?
      (text-table/print-table (table workspace project brick))
      (println message))))

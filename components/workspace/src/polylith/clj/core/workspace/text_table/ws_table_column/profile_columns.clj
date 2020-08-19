(ns polylith.clj.core.workspace.text-table.ws-table-column.profile-columns
  (:require [polylith.clj.core.workspace.text-table.shared :as shared]))

(defn profile-sorting [profile]
  [(not= :default profile) profile])

(defn status-flags [profile brick-name profile->bricks]
  (let [has-src (if (contains? (-> profile profile->bricks :src) brick-name) "x" "-")
        has-test-src (if (contains? (-> profile profile->bricks :test) brick-name) "x" "-")]
    (str has-src has-test-src)))

(defn profile-cell [index brick-name profile column profile->bricks]
  (let [status (status-flags profile brick-name profile->bricks)]
    (shared/standard-cell status column (+ index 3) :purple :center)))

(defn column [index profile start-column bricks profile->bricks]
  (let [column (+ start-column (* 2 index))]
    (concat
      [(shared/header (str "+" (name profile)) column :purple :left)]
      (map-indexed #(profile-cell %1 %2 profile column profile->bricks)
                   (map :name bricks)))))

(defn profile-bricks [[profile {:keys [src-bricks test-bricks]}]]
  [profile {:src  src-bricks
            :test test-bricks}])

(defn profiles-to-show [{:keys [profile->settings active-dev-profiles]}]
  (sort-by profile-sorting
    (filter #(not (contains? active-dev-profiles %))
            (map first profile->settings))))

(defn columns [start-column bricks profiles {:keys [profile->settings]}]
  (let [profile->bricks (into {} (map profile-bricks profile->settings))]
    (apply concat
      (map-indexed #(column %1 %2 start-column bricks profile->bricks)
                   profiles))))

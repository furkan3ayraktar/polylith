(ns polylith.clj.core.entity.path-selector
  (:require [polylith.clj.core.entity.matchers :as m]))

(defn select-paths [path-entries]
  (vec (sort (set (map :path path-entries)))))

(defn all-src-paths [path-entries]
  (select-paths (m/filter-entries path-entries [m/=src])))

(defn all-test-paths [path-entries]
  (select-paths (m/filter-entries path-entries [m/=test])))

(defn brick-src-entries [brick-name path-entries]
  (m/filter-entries path-entries [m/=brick m/=src (m/=name brick-name)]))

(defn select-name [path-entries & criterias]
  (vec (sort (set (map :name (m/filter-entries path-entries criterias))))))

(defn src-component-names [path-entries]
  (select-name path-entries m/=component m/=src m/=exists))

(defn src-base-names [path-entries]
  (select-name path-entries m/=base m/=src m/=exists))

(defn test-component-names [path-entries]
  (select-name path-entries m/=component m/=test m/=exists))

(defn test-base-names [path-entries]
  (select-name path-entries m/=base m/=test m/=exists))

(defn src-brick-names [path-entries]
  (select-name path-entries m/=brick m/=src m/=exists))

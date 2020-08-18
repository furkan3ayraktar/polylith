(ns polylith.clj.core.common.paths
  (:require [clojure.string :as str]))

(defn- starts-with [path start]
  (and (string? path)
       (str/starts-with? path start)))

(defn- entity-name [path start-index]
  (let [end-index (+ start-index (str/index-of (subs path start-index) "/"))]
    (if (< end-index 0)
      path
      (subs path start-index end-index))))

(defn- component? [path]
  (starts-with path "components/"))

(defn- base? [path]
  (starts-with path "bases/"))

(defn- environment? [path]
  (starts-with path "environments/"))

(defn- component-name [path]
  (entity-name path 11))

(defn- base-name [path]
  (entity-name path 6))

(defn- environment-name [path]
  (entity-name path 13))

(defn src-path? [path]
  (and (not (str/ends-with? path "/test"))
       (not (str/ends-with? path "/resources"))))

(defn resources-path? [path]
  (str/ends-with? path "/resources"))

(defn test-path? [path]
  (str/ends-with? path "/test"))

(defn component-paths [paths]
  (filterv component? paths))

(defn base-paths [paths]
  (filterv base? paths))

(defn components-from-paths [paths]
  (vec (sort (set (mapv component-name (component-paths paths))))))

(defn bases-from-paths [paths]
  (vec (sort (set (map base-name (base-paths paths))))))

(defn bricks-from-paths [paths]
  (concat (components-from-paths paths)
          (bases-from-paths paths)))

(defn environments-from-paths [paths]
  (vec (sort (set (map environment-name (filter environment? paths))))))
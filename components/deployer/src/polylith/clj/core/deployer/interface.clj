(ns polylith.clj.core.deployer.interface
  (:require [polylith.clj.core.deployer.core :as core]))

(defn deploy []
  (core/deploy))

(defn create-artifacts []
  (core/create-artifacts))

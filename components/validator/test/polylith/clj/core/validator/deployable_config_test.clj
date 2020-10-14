(ns polylith.clj.core.validator.deployable-config-test
  (:require [clojure.test :refer :all]
            [polylith.clj.core.validator.data :as data]))

(def config {:paths   ["../../components/change/src"
                       "../../components/command/src"
                       "../../bases/poly-cli/src"]

             :deps    '{org.clojure/clojure {:mvn/version "1.10.1"}
                        org.clojure/tools.deps.alpha {:mvn/version "0.8.695"}
                        me.raynes/fs {:mvn/version "1.4.6"}
                        mvxcvi/puget {:mvn/version "1.3.1"}}

             :aliases {:test      {:extra-paths ["../../components/change/test"
                                                 "../../components/creator/test"
                                                 "test"]
                                   :extra-deps  {}}

                       :aot       {:extra-paths ["classes"]
                                     :main-opts   ["-e" "(compile,'polylith.clj.core.poly-cli.core)"]}

                       :uberjar   {:extra-deps {'uberdeps {:mvn/version "0.1.10"}}
                                   :main-opts  ["-m" "uberdeps.uberjar"]}}})

(deftest valid-config--returns-nil
  (is (= nil
         (data/validate-deployable-config config))))

(deftest valid-config-withoug-deps--returns-nil
  (is (= nil
         (data/validate-deployable-config (dissoc config :deps)))))

(deftest invalid-nop-namespace--returns-error-message
  (is (= {:aliases {:test ["invalid type"]}}
         (data/validate-deployable-config (assoc-in config [:aliases :test] 1)))))

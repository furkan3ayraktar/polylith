(ns polylith.clj.core.util.params-test
  (:require [clojure.test :refer :all]
            [polylith.clj.core.util.interfc.params :as params]))

(deftest pars--named-and-unamed-parameters--returns-expected-result
  (is (= {:unnamed-args ["w" "abc"]
          :named-args {:flag "true"
                       :name "x"
                       :top-ns "se.example"}}
         (params/parse "w" "name:x" "top-ns:se.example" ":flag" "abc" nil))))
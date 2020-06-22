(ns polylith.validate.m105-illegal-name-sharing-test
  (:require [clojure.test :refer :all]
            [polylith.validate.m105-illegal-name-sharing :as m105]))

(deftest errors--when-a-base-and-an-interface-but-not-a-component-shares-the-same-name--returns-error
  (let [interfaces '[{:name "mybase"}]
        components '[{:name "mybase1"
                      :interface {:name "mybase"}}]
        bases [{:name "mybase"}]
        interface-names (set (map :name interfaces))]
    (is (= [{:type "error"
             :code 105
             :message "A base can't have the same name as an interface or component: mybase"
             :interfaces ["mybase"]
             :components []
             :bases ["mybase"]}]
           (m105/errors interface-names components bases)))))

(deftest errors--when-a-base-and-a-component-the-same-name--returns-error
  (let [interfaces '[{:name "mybase"}]
        components '[{:name "mybase"
                      :interface {:name "mybase"}}]
        bases [{:name "mybase"}]
        interface-names (set (map :name interfaces))]
    (is (= [{:type "error"
             :code 105
             :message "A base can't have the same name as an interface or component: mybase"
             :interfaces ["mybase"]
             :components ["mybase"]
             :bases ["mybase"]}]
           (m105/errors interface-names components bases)))))
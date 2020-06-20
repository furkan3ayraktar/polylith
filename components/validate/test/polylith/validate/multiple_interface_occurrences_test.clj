(ns polylith.validate.multiple-interface-occurrences-test
  (:require [clojure.test :refer :all]
            [polylith.validate.multiple-interface-occurrences :as multi]))

(def components [{:name "change"
                  :type "component"
                  :interface {:name "change"}}
                 {:name "shell"
                  :type "component"
                  :interface {:name "shell"}}
                 {:name "shell2"
                  :type "component"
                  :interface {:name "shell"}}
                 {:name "file"
                  :type "component"
                  :interface {:name "file"}}
                 {:name "git"
                  :type "component"
                  :interface {:name "git"}}])

(def environments [{:name "core"
                    :group "core"
                    :test? false
                    :type "environment"
                    :component-names ["change" "shell" "shell2"]
                    :base-names ["tool"]
                    :paths ["bases/tool/src"
                            "components/change/src"
                            "components/shell/src"
                            "components/shell2/src"]}
                   {:name "core-test"
                    :group "core"
                    :test? true
                    :type "environment"
                    :component-names ["change"]
                    :base-names []
                    :paths ["components/change/test"]}])

(deftest errors--when-more-than-one-component-implements-the-same-interface-in-an-environment--return-error-message
  (is (= ["More than one component that implements the shell interface was found in the core environment: shell, shell2"]
         (multi/errors components environments))))
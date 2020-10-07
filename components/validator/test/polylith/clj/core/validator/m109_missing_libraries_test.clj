(ns polylith.clj.core.validator.m109-missing-libraries-test
  (:require [clojure.test :refer :all]
            [polylith.clj.core.util.interface.color :as color]
            [polylith.clj.core.validator.m109-missing-libraries :as m109])
  (:refer-clojure :exclude [bases]))

(def environments [{:name "development"
                    :component-names ["article" "comment" "database" "log" "profile" "spec" "tag" "user"]
                    :base-names ["rest-api"]
                    :lib-deps {"clj-jwt" {:mvn/version "0.1.1"}}
                    :profile {:lib-deps {"clj-time" {:mvn/version "0.14.2"}}}}
                   {:name "realworld-backend"
                    :component-names ["article" "comment" "database" "log" "profile" "spec" "tag" "user"]
                    :base-names ["rest-api"]
                    :lib-deps {"clj-jwt" {:mvn/version "0.1.1"}
                               "com.taoensso/timbre" {:mvn/version "4.10.0"}
                               "compojure/compojure" {:mvn/version "1.6.0"}
                               "spec-tools" {:mvn/version "1.0"}
                               "honeysql" {:mvn/version "0.7.0"}}
                    :profile {:lib-deps {}}}])

(def components [{:name "article"
                  :lib-dep-names ["clj-time" "honeysql"]}
                 {:name "comment"
                  :lib-dep-names ["clj-time" "honeysql"]}
                 {:name "database"
                  :lib-dep-names ["honeysql"]}])

(def bases [{:name "rest-api"
             :lib-dep-names ["spec-tools"]}])

(def settings {:profile-to-settings {"default" {:lib-deps {"clj-time" {:size 0, :type "maven", :version "0.14.2"}}}}})

(deftest warnings--missing-libraries-in-an-environment--returns-a-warning
  (is (= [{:type "error"
           :code 109
           :environment "development"
           :message           "Missing libraries in the development environment: honeysql, spec-tools"
           :colorized-message "Missing libraries in the development environment: honeysql, spec-tools"}
          {:type "error"
           :code 109
           :environment "realworld-backend"
           :message           "Missing libraries in the realworld-backend environment: clj-time"
           :colorized-message "Missing libraries in the realworld-backend environment: clj-time"}]
         (m109/errors environments components bases settings color/none))))

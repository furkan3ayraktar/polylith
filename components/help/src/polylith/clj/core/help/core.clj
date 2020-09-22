(ns polylith.clj.core.help.core
  (:require [polylith.clj.core.help.check :as check]
            [polylith.clj.core.help.create :as create]
            [polylith.clj.core.help.deps :as deps]
            [polylith.clj.core.help.diff :as diff]
            [polylith.clj.core.help.info :as info]
            [polylith.clj.core.help.libs :as libs]
            [polylith.clj.core.help.test :as test]
            [polylith.clj.core.help.summary :as summary]))

(defn print-help [cmd ent show-env? show-brick? show-bricks? color-mode]
  (case cmd
    "check" (check/print-help color-mode)
    "create" (create/print-help ent color-mode)
    "deps" (deps/print-help show-env? show-brick? show-bricks? color-mode)
    "diff" (diff/print-help color-mode)
    "info" (info/print-help color-mode)
    "libs" (libs/print-help color-mode)
    "test" (test/print-help color-mode)
    "ws" (println "  Not implemented yet!")
    (summary/print-help color-mode)))

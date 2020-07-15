(ns polylith.clj.core.help.test
  (:require [polylith.clj.core.util.interfc.color :as color]))

(defn help-text [color-mode]
  (let [env (color/environment "env" color-mode)
        mycomponent (color/component "mycomponent" color-mode)
        mybase (color/component "mybase" color-mode)]
    (str "  Executes tests for the given environment or all environments if not given.\n\n"

         "  The tests for a brick are executed one environment at a time when these criterias are met:\n"
         "  - The brick's 'test' folder is added to the environment.\n"
         "  - The brick is either directly or indirectly changed (*).\n\n"

         "  The tests for an environment are executed when these criterias are met:\n"
         "  - '-all' is sent in as an argument.\n"
         "  - The environment's 'test' folder is added to the environment.\n"
         "  - At least one file in the 'environments/" env "' directory structure has changed\n"
         "    since the last git commit.\n\n"

         "  The src and test folders are added to 'environments/" env "/deps.edn' for each environment\n"
         "  in the keys ':paths' and ':aliases > :test > :extra-paths'.\n\n"

         "  The paths are relative to each deps.edn file, e.g:\n"
         "  - \"test\"\n"
         "  - \"../../bases/" mybase "/test\"\n"
         "  - \"../../components/" mycomponent "/test\"\n\n"

         "  (*) This is explained in the 'info' command.\n\n"

         "  poly test [" env "]\n"
         "    " env " = (omitted) -> Run tests for all environments.\n"
         "          else      -> Run test for the given environment.\n\n"

         "  Examples:\n"
         "    poly test\n"
         "    poly test -all\n"
         "    poly test dev"
         "    poly test dev -all")))

(defn print-help [color-mode]
  (println (help-text color-mode)))

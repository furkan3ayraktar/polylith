(ns polylith.cli.cmd.info
  [:require [clojure.pprint :as pp]
            [polylith.workspace.interface :as ws]])

(defn execute [workspace arg]
  (if (= "-dump" arg)
    (pp/pprint workspace)
    (ws/print-table workspace)))
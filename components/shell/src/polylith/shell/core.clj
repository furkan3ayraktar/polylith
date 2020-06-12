(ns polylith.shell.core
  (:require [clojure.java.shell :as shell]
            [polylith.shared.interface :as shared]))

(defn sh [args]
  (let [current-env (into {} (System/getenv))
        new-env (dissoc current-env "CLASSPATH")
        {:keys [exit out err]} (shell/with-sh-env new-env (apply shell/sh args))]
    (if (= 0 exit)
      out
      (do
        ;; Print out the stack trace with the error message
        (println out)
        (shared/throw-polylith-exception (str "Shell Err: " err " Exit code: " exit))))))
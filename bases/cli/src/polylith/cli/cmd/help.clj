(ns polylith.cli.cmd.help
  (:require [polylith.common.interface.color :as color]
            [clojure.string :as str]))

(defn command
  ([cmd dark-mode?]
   (str "  " (color/grey dark-mode? cmd)))
  ([cmd dark-mode? & args]
   (str "  " (color/grey dark-mode? cmd) " " (str/join " " args))))

(defn arg [cmd dark-mode?]
  (color/grey dark-mode? cmd))

(defn environment [cmd env dark-mode?]
  (str "  " (color/grey dark-mode? cmd) " " (color/environment env)))

(defn help-cmd [dark-mode?]
  (str "  [" (color/grey dark-mode? "help") "]"))

(defn test-cmd [dark-mode?]
  (let [cmd (color/grey dark-mode? "test")
        env (color/environment "env")]
    (str "  " cmd " [" env "]")))

(defn help-text [dark-mode?]
  (str
    (command "check" dark-mode?) "         Checks that the workspace is valid.\n"
    (test-cmd dark-mode?) "    Runs the tests for the given environment (or all).\n"
    (command "info" dark-mode?) " [" (arg "-dump" dark-mode?) "]  Views current workspace.\n"
    (help-cmd dark-mode?) "        Views this help."))

(defn execute [dark-mode?]
  (-> dark-mode? help-text println))
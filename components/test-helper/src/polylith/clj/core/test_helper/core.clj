(ns polylith.clj.core.test-helper.core
  (:require [clojure.string :as str]
            [clojure.stacktrace :as stacktrace]
            [polylith.clj.core.file.interfc :as file]
            [polylith.clj.core.command.interfc :as command]
            [polylith.clj.core.user-input.interfc :as user-input]
            [polylith.clj.core.user-config.interfc :as user-config]
            [polylith.clj.core.workspace-clj.interfc :as ws-clj]
            [polylith.clj.core.workspace.interfc :as ws]))

(def user-home "USER-HOME")

(def root-dir (atom nil))

(defn sub-dir [dir]
  (str @root-dir "/" dir))

(defn test-setup-and-tear-down [function]
  (let [path (file/create-temp-dir "polylith-root")]
    (if path
      (do
        (reset! root-dir path)
        (file/create-dir (sub-dir user-home)))
      (throw (Exception. "Could not create temp directory")))
    (function)
    (file/delete-dir path)))

(defn execute-command [current-dir args]
  (with-redefs [file/current-dir (fn [] (if (str/blank? current-dir)
                                          @root-dir
                                          (str @root-dir "/" current-dir)))
                user-config/home-dir (fn [] (str @root-dir "/" user-home))]
    (let [user-input (user-input/extract-params args)
          {:keys [exception]} (command/execute-command user-input)]
      (when (-> exception nil? not)
        (stacktrace/print-stack-trace exception)))))

(defn paths [dir]
  (let [paths (-> dir sub-dir file/relative-paths)]
    (set (filter #(not (str/starts-with? (str %) ".git/")) paths))))

(defn content [dir filename]
  (str/split-lines (slurp (str (sub-dir dir) "/" filename))))

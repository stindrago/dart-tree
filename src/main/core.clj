(ns main.core
  (:require [medley.core :as m]
            [cli-matic.core :refer [run-cmd]]
            [babashka.fs :as fs]))

(defn create-project
  "Create a project from skeleton."
  [args]
  (case (count (:_arguments args))
    1 (do (println "Creating default directory:"
                   (first (:_arguments args)))
          (fs/create-dir (first (:_arguments args))))
    2 (println "Creating default directory:" (second (:_arguments args))
               "from skeleton:" (first (:_arguments args))
               (fs/copy-tree (str "./" "skel/" (first (:_arguments args)))
                             (second (:_arguments args))))))

(def CONFIGURATION
  {:app         {:command     "dart"
                 :description ["A command-line tool for the D.A.R.T. method by stindrago <email@stindrago.com>"
                               ""
                               "Stop creating the same files and folders over and over again for every new project, automatize the process."
                               "Looks cool, doesn't it?"]
                 :version     "0.0.1"}
   :commands    [{:command     "new"
                  :description ["Create a new project tree from a skeleton."]

                  :runs        create-project}]})

(defn -main [& args]
  (run-cmd *command-line-args* CONFIGURATION))

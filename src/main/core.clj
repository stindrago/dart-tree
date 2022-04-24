(ns main.core
  (:require [medley.core :as m]
            [cli-matic.core :refer [run-cmd]]
            [babashka.fs :as fs]
            [babashka.process :refer [process]]))

(defn create-project
  [params]
  (let [args (:_arguments params)
        first (first args)
        second (second args)]
    (case (count args)
      1 (do (fs/create-dir first)
            (println "Created default directory" first))
      2 (do
          (fs/copy-tree (str "./skel/" first) second)
          (println "Created default directory:" second
                   "from skeleton:" first)))))

(defn print-args
  [args]
  (println args))

(def CONFIGURATION
  {:command "dart"
   :description ["A command line tool 🔨 for the D.A.R.T method to generate a project tree from a skeleton 📂 (template)."
                 "by stindrago <email@stindrago.com>"]
   :version "0.0.1"
   :subcommands [{:command "debug"
                  :description ["Debug."]
                  :runs print-args}
                 {:command "new"
                  :description ["Create a new project tree from a skeleton."]
                  :runs create-project}]})

(defn -main [& args]
  ;; (println (-> (process '[ls]) :out slurp))
  (run-cmd *command-line-args* CONFIGURATION))

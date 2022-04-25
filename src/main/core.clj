(ns main.core
  (:require [medley.core :as m]
            [cli-matic.core :refer [run-cmd]]
            [babashka.fs :as fs]
            [babashka.process :refer [process]]))

(def version "0.0.2")
(def skel-dir (str (fs/expand-home "~") "/" ".config/dart-cli/skel"))

(defn new-project
  [params]
  (let [args (:_arguments params)
        first (first args)
        second (second args)]
    (case (count args)
      1 (do (fs/create-dir first)
            (println "Created default directory" first))
      2 (do
          (fs/copy-tree (str skel-dir "/" first) second)
          (println "Created default directory:" second
                   "from skeleton:" first)))))

(defn show-version [args] (println version))

(def CONFIGURATION
  {:command "dart"
   :description ["A command line tool 🔨 for the D.A.R.T method to generate a project tree from a skeleton 📂 (template)."
                 "by stindrago <email@stindrago.com>"]
   :version version
   :subcommands [{:command "new"
                  :description ["Create a new project tree from a skeleton."]
                  :runs new-project}
                 {:command "version"
                  :description ["Show version details."]
                  :runs show-version}]})

(defn -main [& args]
  ;; (println (-> (process '[ls]) :out slurp))
  (run-cmd *command-line-args* CONFIGURATION))

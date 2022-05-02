(ns main.core
  (:require [cli-matic.core :refer [run-cmd]]
            [babashka.fs :as fs]
            [nano-id.core :refer [nano-id]]
            [main.tree :as t]
            [clojure.java.shell :refer [sh]])

  (:gen-class))

(def version "0.1.1")
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

(defn list-skel
  [args]
  (if (= 0 (count (:_arguments args)))
    (do
      (println "Available skeletons:")
      (run! #(println "-" %) (map fs/file-name (map str (fs/list-dir skel-dir)))))
    (t/pprint-ftree (t/popu-list (str skel-dir "/" (first (:_arguments args)))))))

(defn show-version [args] (println version))

(def CONFIGURATION
  {:command "dart"
   :description ["A command line tool 🔨 for the D.A.R.T method to generate a project tree from a skeleton 📂 (template)."
                 "by stindrago <email@stindrago.com>"]
   :version version
   :subcommands [{:command "new"
                  :description ["Create a new project tree from a skeleton."]
                  :examples    ["dt new my-awesome-book \t - Create an empty project tree"
                                "dt new book my-awesome-book \t - Create a new project tree from the `book' skeleton"]
                  :runs new-project}
                 {:command "list"
                  :description ["List skeletons."]
                  :examples    ["dt list \t - List available skeletons."
                                "dt list book \t - List the `book' skeleton tree."]
                  :runs list-skel}
                 {:command "version"
                  :description ["Show version details."]
                  :runs show-version}]})

(defn -main
  [& args]
  (run-cmd *command-line-args* CONFIGURATION))

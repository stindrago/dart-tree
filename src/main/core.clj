(ns main.core
  (:require [cli-matic.core :refer [run-cmd]]
            [babashka.fs :as fs]
            [nano-id.core :refer [nano-id]]
            [main.tree :as t])
  (:gen-class))

(def version "0.2.0")
(def skel-dir (str (fs/expand-home "~") "/" ".config/dart-cli/skel"))
(def config-file ".dartconfig")

(defn create-config
  "Create a config.edn file."
  [file namespace]
  (spit file {:uuid (nano-id 10)
              :namespace namespace}))

(defn new-project
  "Create a project directory from skeleton."
  [args]
  (let [argv (:_arguments args)
        first (first argv)
        second (second argv)]
    (case (count argv)
      1 (do (fs/create-dir first)
            (create-config (str first "/" config-file) (:namespace args))
            (println "Created default directory:" first))
      2 (do
          (fs/copy-tree (str skel-dir "/" (:namespace args) "/" first) second)
          (create-config (str second "/" config-file) (:namespace args))
          (println "Created default directory:" second
                   "from skeleton:" first)))))

(defn list-skel
  [args]
  (if (= 0 (count (:_arguments args)))
    (do
      (println "Available skeletons:")
      (run! #(println "-" %) (map fs/file-name (map str (fs/list-dir skel-dir)))))
    (t/pprint-ftree (t/popu-list (str skel-dir "/" (first (:_arguments args)))))))


(defn show-version
  "Print the program version"
  [args]
  (println version))

(def CONFIGURATION
  {:command "dt"
   :description ["A command line tool 🔨 for the D.A.R.T method to generate a project tree from a skeleton 📂 (template)."
                 "by stindrago <email@stindrago.com>"]
   :version version
   :subcommands [{:command "new"
                  :description ["Create a new project tree from a skeleton."]
                  :examples    ["dt new my-awesome-book \t\t - Create a new empty project."
                                "dt new book my-awesome-book \t\t - Create a new project tree from the `book' skeleton."
                                "dt new book my-awesome-book -n work \t - Create a new project tree from the `book' skeleton for the `work' namespace."]
                  :opts        [{:as "- Assign a namespace to the project."
                                 :default "private"
                                 :option "namespace"
                                 :short "n"
                                 :type :string}]
                  :runs new-project}
                 {:command "list"
                  :description ["List information about the skeletons and namespaces."]
                  :examples    ["dt list \t\t - List available namespaces."
                                "dt list work \t\t - List available skeletons for the `work' namespace."
                                "dt list work book \t - List the `book' skeleton for the `work' namespace."]
                  :runs list-skel}
                 {:command "version"
                  :description ["Show version details."]
                  :runs show-version}]})

(defn -main
  [& args]
  (run-cmd *command-line-args* CONFIGURATION))

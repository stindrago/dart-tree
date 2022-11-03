(ns main.core
  (:require [cli-matic.core :refer [run-cmd]]
            [babashka.fs :as fs]
            [nano-id.core :refer [nano-id]]
            [main.tree :as t])
  (:gen-class))

(def version "0.3.3")
(def skel-dir (str (fs/expand-home "~") "/" ".config/dart-tree/skel"))
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

(defn remove-unwanted
  "Remove `/` from `str` to prevent unwanted behaviors."
  [str]
  (clojure.string/replace str #"/" ""))

(defn ns-skel-dir?
  "Check if the `ns` and `skel` directories exist."
  ([skel]
   (fs/directory? (str skel-dir "/" (remove-unwanted skel))))
  ([ns skel]
   (fs/directory? (str skel-dir "/" (remove-unwanted ns) "/" (remove-unwanted skel)))))

(defn list-skel
  "List skeletons from namespace."
  [args]
  (let [argv (:_arguments args)
        ns (first argv)
        skel (second argv)]
    (case (count argv)
      ;; Show available namespaces.
      0 (do (println "Listing available namespaces:")
            (run! #(println "-" %) (map fs/file-name (filter fs/directory? (map str (fs/list-dir (str skel-dir)))))))

      ;; Show available skeletons from `ns`.
      1 (if (ns-skel-dir? ns)
          (do (println "Listing available skeletons for the" (clojure.string/upper-case ns) "namespace:")
              (run! #(println "-" %) (map fs/file-name (filter fs/directory? (map str (fs/list-dir (str skel-dir "/" ns)))))))
          (do
            (println "Namespace non found, select one from the list:")
            (run! #(println "-" %) (map fs/file-name (filter fs/directory? (map str (fs/list-dir skel-dir)))))))

      ;; Show the `skel` from `ns`.
      2 (if (ns-skel-dir? ns skel)
          (do
            (println "Listing" (clojure.string/upper-case skel)
                     "skeleton for the" (clojure.string/upper-case ns) "namespace:")
            (t/pprint-ftree (t/popu-list (str skel-dir "/" ns "/" skel)))))

      ;; Anything else.
      (println "Check syntax. See `dt list --help` for usage."))))

(defn show-version
  "Print the program version"
  [args]
  (println version))

(def CONFIGURATION
  {:command "dt"
   :description ["A command line tool 🔨 for the D.A.R.T method to generate a project tree from a skeleton 📂 (template)."
                 "by Costin Dragoi <costin@dragoi.me>"]
   :version version
   :subcommands [{:command "new"
                  :description ["Create a new project tree from a skeleton."]
                  :examples    ["dt new awesome-book \t\t - Create a new empty project."
                                "dt new book awesome-book \t\t - Create a new project tree from the `book' skeleton."
                                "dt new book awesome-book -n work \t - Create a new project tree from the `book' skeleton for the `work' namespace."]
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
  (run-cmd args CONFIGURATION))

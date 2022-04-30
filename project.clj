(defproject dart-cli "0.1.1"
  :description "A command line tool 🛠 for the D.A.R.T method to generate a project tree from a skeleton 📂 (template)."
  :url "https://gitlab.com/stindrago/dart-cli"
  :license {:name "AGPL3"
            :url "https://gitlab.com/stindrago/dart-cli/-/raw/main/LICENSE"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [cli-matic "0.5.3"]
                 [babashka "0.8.1"]
                 [fs "1.3.3"]
                 [nano-id "1.0.0"]]
  :main ^:skip-aot main.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}}
  :repl-options {:init-ns main.core})

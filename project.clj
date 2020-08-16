(defproject goodreads2roam "0.1.0"
  :source-paths ["src/main"]
  :test-paths ["src/test"]
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.773"]
                 [org.clojure/core.async "1.3.610"]
                 [testdouble/clojurescript.csv "0.4.5"]
                 [thheller/shadow-cljs "2.10.19"]
                 [com.andrewmcveigh/cljs-time "0.5.2"]
                 [org.clojure/tools.cli "1.0.195-SNAPSHOT"]
                 ]
  :repositories {"sonatype snapshots" "https://oss.sonatype.org/content/repositories/snapshots"}
  :resource-paths []
  :compile-path nil
  :target-path nil
  :lein-tools-deps/config {:config-files [:install :user :project]
                           :aliases      [:dev :test]})

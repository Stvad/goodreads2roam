(defproject goodreads2roam "0.1.0"
  :source-paths ["src"]
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.773"]
                 [org.clojure/core.async "1.3.610"]
                 [testdouble/clojurescript.csv "0.4.5"]
                 [thheller/shadow-cljs "2.10.19"]
                 [com.andrewmcveigh/cljs-time "0.5.2"]
                 [org.clojure/tools.cli "1.0.194"]
                 ]
  :test-paths []
  :resource-paths []
  :compile-path nil
  :target-path nil
  :plugins [[lein-tools-deps "0.4.1"]]
  :lein-tools-deps/config {:config-files [:install :user :project]
                           :aliases      [:dev :test]})

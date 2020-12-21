(ns goodreads2roam.node)

(def fs (js/require "fs"))
(def process (js/require "process"))

(defn read-file [path]
  (.readFileSync fs path "utf8"))

(defn write-file [path content]
  (.writeFileSync fs path content))

(defn exit [status msg]
  (println msg)
  (.exit process status))

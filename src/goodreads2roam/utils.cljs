(ns goodreads2roam.utils)

(def fs (js/require "fs"))

(defn read-file [path]
  (.readFileSync fs path "utf8"))

(defn write-file [path content]
  (.writeFileSync fs path content))

(defn csv-list->maps [csv-data]
  (map zipmap
       (repeat (first csv-data))
       (rest csv-data)))

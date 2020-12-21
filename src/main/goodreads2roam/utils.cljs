(ns goodreads2roam.utils)

(defn csv-list->maps [csv-data]
  (map zipmap
       (repeat (first csv-data))
       (rest csv-data)))

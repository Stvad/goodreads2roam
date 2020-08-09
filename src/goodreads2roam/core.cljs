(ns goodreads2roam.core
  (:require
    [goodreads2roam.utils :refer [write-file exit]]
    [goodreads2roam.goodreads :refer [read-books book->roam shelves]]
    [clojure.tools.cli :refer [parse-opts]]
    [clojure.string :as string]))

(defn usage [options-summary]
  (->> ["Converts GoodReads csv export into the RoamResearch Markdown."
        ""
        "Usage: goodreads2roam [options] input-file output-file"
        ""
        "Example: goodreads2roam -s read -s fiction export.csv books.md"
        "This would take books that are in 'read' and 'fiction' shelves from 'export.csv' file"
        "and convert only them to Roam Markdown format writing them to 'books.md' file"
        ""
        "Options:"
        options-summary
        ""
        "Please refer to the manual page for more information."]
       (string/join \newline)))

(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (string/join \newline errors)))

(def cli-options
  [["-s" "--shelf SHELF" "Shelves to include (books from all shelves are included by default)"
    :multi true
    :default #{}
    :update-fn conj
    ]
   ["-h" "--help"]
   ]
  )

(defn filter-by-shelves [shelves-to-include books]
  (filter
    #(clojure.set/subset? shelves-to-include (set (shelves %)))
    books)
  )

(defn convert-books [in-file out-file shelves-to-include]
  (let [books (read-books in-file)]
    (write-file out-file
                (->> books
                     (filter-by-shelves shelves-to-include)
                     (map book->roam)
                     (apply str)))))


(defn -main [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)
        [in-file out-file] arguments]
    (cond
      (options :help) (exit 0 (usage summary))
      errors (exit 1 (error-msg errors))
      (and in-file out-file) (convert-books in-file out-file (options :shelf))
      :else (exit 1 (usage summary))))
  )

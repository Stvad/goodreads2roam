(ns goodreads2roam.goodreads
  (:require
    [testdouble.cljs.csv :as csv]
    [cljs-time.format :as time]
    [goodreads2roam.utils :refer [csv-list->maps]]
    [clojure.string :as s]
    ))

(defn to-stringlist [string]
  (s/split string #", "))

(defn page [name]
  (when (not-empty name)
    (str "[[" name "]]")))

(defn tag [name]
  (str "#" (page name)))

(defn bullet
  ([val] (bullet val 1))
  ([val depth]
   (str "\n" (->> " " (repeat (* 2 depth)) (apply str)) "- " val))
  )

(defn authors [book]
  (let [additional-authors (book "Additional Authors")]
    (->>
      (conj (when (not-empty additional-authors) (to-stringlist additional-authors)) (book "Author"))
      (map page)
      (s/join " ")
      ))
  )

(defn attr [name value]
  (when (not-empty value)
    (bullet (str name "::" value))))

(defn shelves [book]
  (->>
    (book "Bookshelves")
    to-stringlist
    (cons (book "Exclusive Shelf"))
    set
    ))

(defn tags [book]
  (->>
    (shelves book)
    (map tag)
    (s/join " ")
    ))

(def goodreads-format (time/formatter "yyyy/MM/dd"))
(def roam-format (time/formatter "[[MMMM do, yyyy]]"))

(defn roam-date [book]
  (let [goodreads-date (book "Date Read")]
    (when (not-empty goodreads-date)
      (->> goodreads-date
           (time/parse goodreads-format)
           (time/unparse roam-format)))))


(defn render-review [review]
  (when (not-empty review)
    (->> (s/split review #"<br/>")
         (filter not-empty)
         (map #(s/replace % #"</?b>" "**"))
         (map #(bullet % 2))
         (apply str)
         ))
  )

(defn parse-title [book]
  (let [[[full-name just-name _ series-name order _]] (re-seq #"(.+) (\(([^,]+),? \#(\d{1,2})\))|(.+)" (book "Title"))]
    {:name   (or just-name full-name)
     :series series-name
     :order  order})
  )

(defn book->roam [book]
  (let [title (parse-title book)]
    (str (bullet (page (title :name)) 0)
         (attr "isa" (page "book"))
         (bullet (tags book) 2)
         (attr "author" (authors book))
         (attr "series" (page (title :series)))
         (attr "reading status" (page (book "Exclusive Shelf")))
         (attr "link" (str "https://www.goodreads.com/book/show/" (book "Book Id")))
         (attr "recommendation" (book "Recommended By"))
         (attr "rating" (book "My Rating"))
         (attr "publication year" (book "Original Publication Year"))
         (attr "read date" (roam-date book))
         (attr "review" (render-review (book "My Review")))
         (attr "private notes" (render-review (book "Private Notes")))
         (attr "read count" (when (not= "0" (book "Read Count"))
                              (book "Read Count")))
         ))
  )

(defn filter-by-shelves [shelves-to-include books]
  (filter
    #(clojure.set/subset? shelves-to-include (set (shelves %)))
    books)
  )

(defn books->roam [books]
  (clj->js (map book->roam books)))

(defn read-books-string [string]
  (csv-list->maps (csv/read-csv string)))


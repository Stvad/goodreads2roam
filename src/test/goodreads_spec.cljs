(ns goodreads-spec
  (:require [cljs.test :refer-macros [deftest is testing]])
  (:require [goodreads2roam.goodreads :refer [parse-title]])
  )

(deftest parse-title-spec
  (testing "no series"
    (let [title "Some random string"]
      (is (= title (->> (parse-title {"Title" title}) :name)))))
  )
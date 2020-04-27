(ns humane-time.test-core
  (:require
    [cljs.test :refer-macros [is are deftest testing use-fixtures]]))

(deftest example
  (is (= 0 1)))

#_(deftest dt-dest
  (is (= (dissoc (datetime-destructure "14-2-1990") :datetime-obj)
         {:month-name "February"
          :month-name-short "Feb"
          :day-of-week 3
          :day-of-month 14
          :day-name "Wednesday" 
          :year 1990
          :month-number 2
          :day-name-short "Wed"})))


;(readable-date "15-06-1984" {:short-names? true})
;(duration-descriptor "1-1-1970")
;(singular->plural "ok" 2)
;(readable-year 2)
;(readable-duration (duration-descriptor "15-5-1001"))
;(readable-duration (duration-descriptor "15-5-1001") "Roughly")
;(duration-descriptor "15-5-1000")


;(readable-moment "15-05-1984")
;(readable-moment "15-05-1984" {:prefix "It happened" :suffix "before"})

;(period-descriptor {:start "15-05-1984"})
; (period-descriptor {:start "15-05-1984" :end "15-05-1986"})
;(readable-period {:start "15-05-1984" :end "15-05-1986" :period-desc {:separator ", "}})

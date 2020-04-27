(ns humane-time.ops
  (:require [cljs.reader :as r]
            [cljs-time.core :as t]))

(def month-index
  {1 "January" 2 "February" 3 "March" 4 "April" 5 "May" 6 "June" 
   7 "July" 8 "August" 9 "September" 10 "October" 11 "November" 12 "December"})

(def month-index-short
  {1 "Jan" 2 "Feb" 3 "Mar" 4 "Apr" 5 "May" 6 "Jun" 
   7 "Jul" 8 "Aug" 9 "Sep" 10 "Oct" 11 "Nov" 12 "Dec"})

(def week-index
  {1 "Monday" 2 "Tuesday" 3 "Wednesday" 4 "Thursday" 5 "Friday" 6 "Saturday" 7 "Sunday"})

(def week-index-short
  {1 "Mon" 2 "Tue" 3 "Wed" 4 "Thu" 5 "Fri" 6 "Sat" 7 "Sun"})

(defn 
  ^{:doc "Takes a date string and returns a map after some destructuring."
    :args-format "dd-mm-yyyy or d-mm-yyyy or dd-m-yyyy or d-m-yyyy"
    :todo {1 "Add input validation - min value of year must be 1000."
           2 "Accept dd-mm-yyyyThh:mm:ss inputs."}}
  datetime-descriptor
  [date-string]
  (let [ds (clojure.string/split date-string #"-")
        y (last ds)
        m (second ds)
        d (first ds)
        dt-obj (t/date-time (r/read-string y) (r/read-string m) (r/read-string d))]
    {:year (r/read-string y)
     :month-number (r/read-string m)
     :month-name (month-index (r/read-string m))
     :month-name-short (month-index-short (r/read-string m))
     :day-of-week (t/day-of-week dt-obj)
     :day-of-month (t/day dt-obj)
     :day-name (week-index (t/day-of-week dt-obj))
     :day-name-short (week-index-short (t/day-of-week dt-obj))
     :datetime-obj dt-obj}))

(defn
  ^{:doc "Returns a map that describes the duration between a start and end-time in different units.
          For each unit, the value semantics is of lower bound. To elaborate:
          If the difference (in years) is 2 years and 10 months, then the value of :years will be 2, not 3. But, the value of :months will be 34.
          Similarly, if the difference in months is 5 months and 3 weeks, the the value of :months will be 5, not 6. But, the value of :weeks will be 23.
          If no end-time is provided, the the current time will be considered as the end-time."
    :args-format "dd-mm-yyyy or d-mm-yyyy or dd-m-yyyy or d-m-yyyy"
    :todo {1 "Add input validation"
           2 "Accept dd-mm-yyyyThh:mm:ss inputs."}}
  duration-descriptor
  [tstring & end-time]
  (let [intrvl (t/interval (:datetime-obj (datetime-descriptor tstring)) (if end-time (:datetime-obj (datetime-descriptor (first end-time))) (t/now)))]
    {:years (t/in-years intrvl)
     :months (t/in-months intrvl)
     :weeks (t/in-weeks intrvl)
     :days (t/in-days intrvl)
     :hours (t/in-hours intrvl)}))

(defn singular->plural
  "Simply adds an 's' at the end of the singular-text arg if val is greater than 1."
  [singular-text val]
  (if (<= val 1) singular-text (str singular-text "s")))

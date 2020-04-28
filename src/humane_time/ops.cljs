(ns 
 ^{:todo {1 "Possibility of accepting '1918-11-11T11:10:50' date-time format."}}
  humane-time.ops
  (:require [cljs.reader :as r]
            [cljs-time.core :as t]
            [humane-time.validators :as validators]
            [cljs.spec.alpha :as s]))

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

(defn datetime-descriptor
  "Takes a date string and returns a map after some destructuring. 
   Accepts date-string only in the form of 'DD-MM-YYYY' and 'YYYY-MM-DD' formats. DD and MM could just be D or M."
  [date-string]
  (let [ds (clojure.string/split date-string #"-")
        y (if (= (count (last ds)) 4) (last ds) (first ds))
        m (second ds)
        d (if (= (count (last ds)) 4) (first ds) (last ds))]
    (if (s/valid? ::validators/date {:day (r/read-string d) :month (r/read-string m) :year (r/read-string y)})
      (let [dt-obj (t/date-time (r/read-string y) (r/read-string m) (r/read-string d))]
        {:year (r/read-string y)
         :month-number (r/read-string m)
         :month-name (month-index (r/read-string m))
         :month-name-short (month-index-short (r/read-string m))
         :day-of-week (t/day-of-week dt-obj)
         :day-of-month (t/day dt-obj)
         :day-name (week-index (t/day-of-week dt-obj))
         :day-name-short (week-index-short (t/day-of-week dt-obj))
         :datetime-obj dt-obj})
      (throw (ExceptionInfo. (str "Invalid date-string: " {:day (r/read-string d) :month (r/read-string m) :year (r/read-string y)}))))))

(defn duration-descriptor
  "Returns a map that describes the duration between a start and end-time in different units.
   For each unit, the value semantics is of lower bound. To elaborate:
   If the difference (in years) is 2 years and 10 months, then the value of :years will be 2, not 3. But, the value of :months will be 34.
   Similarly, if the difference in months is 5 months and 3 weeks, the the value of :months will be 5, not 6. But, the value of :weeks will be 23.
   If no end-time is provided, the the current time will be considered as the end-time."
  [date-string & end-time]
  (let [intrvl (t/interval (:datetime-obj (datetime-descriptor date-string)) (if end-time (:datetime-obj (datetime-descriptor (first end-time))) (t/time-now)))]
    {:years (t/in-years intrvl)
     :months (t/in-months intrvl)
     :weeks (t/in-weeks intrvl)
     :days (t/in-days intrvl)
     :hours (t/in-hours intrvl)}))

(defn singular->plural
  "Simply adds an 's' at the end of the singular-text arg if val is greater than 1."
  [singular-text val]
  (if (<= val 1) singular-text (str singular-text "s")))

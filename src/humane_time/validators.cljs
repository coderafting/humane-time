(ns humane-time.validators
  (:require [cljs.spec.alpha :as s]
            [cljs-time.core :as t]
            [cljs.reader :as r]))

(s/def ::odd-months #{1 3 5 7 8 10 12})
(s/def ::even-months #{4 6 9 11})
(s/def ::feb #{2})
(s/def ::day-odd (s/and pos-int? #(>= % 1) #(<= % 31)))
(s/def ::day-even (s/and pos-int? #(>= % 1) #(<= % 30)))
(s/def ::day-feb (s/and pos-int? #(>= % 1) #(<= % 28)))
(s/def ::day-feb-leap (s/and pos-int? #(>= % 1) #(<= % 29)))
(s/def ::month (s/and pos-int? #(>= % 1) #(<= % 12)))
(s/def ::year (s/and pos-int? #(>= % 1000) #(<= % (t/year (t/time-now)))))

(defn leap-year? 
  "Please refer to the definition of leap year here: https://en.wikipedia.org/wiki/Leap_year.
   The order of the cond block makes the definition work."
  [year]
  (cond (zero? (mod year 400)) true
        (zero? (mod year 100)) false
        (zero? (mod year 4)) true
        :else false))

(defn valid-day-month-year?
  [{:keys [day month year]}]
  (and
   (s/valid? ::year year)
   (cond
     (s/valid? ::odd-months month) (s/valid? ::day-odd day)
     (s/valid? ::even-months month) (s/valid? ::day-even day)
     (s/valid? ::feb month) (if (s/valid? ::leap-year year) 
                              (s/valid? ::day-feb-leap day)
                              (s/valid? ::day-feb day))
     :else false)))

(defn valid-date?
  [date]
  (let [ds (clojure.string/split date #"-")
        y (r/read-string (last ds))
        m (r/read-string (second ds))
        d (r/read-string (first ds))]
    (valid-day-month-year? {:day d :month m :year y})))

(s/def ::leap-year (s/and ::year leap-year?))
(s/def ::date valid-day-month-year?)
(s/def ::date-string valid-date?)

(ns ^{:doc "Contains functions to be exposed to library users."
      :todo {1 "Possibility of making it available for both `clj` and `cljs`."}}
  humane-time.core
  (:require [humane-time.ops :as ops]))

(defn readable-date
  "Accepts date-string only in the form of `DD-MM-YYYY` and `YYYY-MM-DD` formats. `DD` and `MM` could just be `D` or `M`.
   Returns a string similar to: 
   `April 27, 2020` or `Apr 27, 2020` or `Monday, April 27, 2020` or `Mon, April 27, 2020` or `Mon, Apr 27, 2020`.
   The optional options map dictates the return format. It includes the following keys:
    - `:day-name?` - defaults to `true`. 
    - `:short-names?` - defaults to `false`."
  [date-string & opts-map]
  (let [dt (ops/datetime-descriptor date-string)]
    (str (if (or (nil? (:day-name? (first opts-map))) (:day-name? (first opts-map)))
           (if (:short-names? (first opts-map)) 
             (str (:day-name-short dt) ", ") 
             (str (:day-name dt) ", "))
           "")
         (if (:short-names? (first opts-map)) (:month-name-short dt) (:month-name dt))
         " "
         (:day-of-month dt)
         ", "
         (:year dt))))

(defn
  ^{:doc "Returns a human readable string based on the value of number-of-years (val).
          Currently, it uses the US counting system. Please refer: https://www.britannica.com/topic/large-numbers-1765137."
    :todo {1 "Add cases for more than quadrillion years."
           2 "Include an option for non-US counting systems."}}
  readable-year
  [val]
  (cond
    (neg? val) (throw (ExceptionInfo. (str "val (number of years) cannot be negative: " val)))
    (and (>= val 0) (< val 1000)) (str val " " (ops/singular->plural "year" val))
    (and (>= val 1000) (< val 1000000)) (str (int (/ val 1000)) " thousand years")
    (and (>= val 1000000) (< val 1000000000)) (str (int (/ val 1000)) " million years")
    (and (>= val 1000000000) (< val 1000000000000)) (str (int (/ val 1000)) " billion years")
    (and (>= val 1000000000000) (< val 1000000000000000)) (str (int (/ val 1000)) " trillion years")
    (= val 1000000000000000) "a quadrillion years"
    :else "more than a quadrillion years"))

(defn- readable-duration-helper
  "A helper function to be used inside readable-duration fn."
  [time-interval & approximation-string]
  (let [years (:years time-interval)
        months (:months time-interval)
        weeks (:weeks time-interval)
        days (:days time-interval)
        hours (:hours time-interval)
        apprx-word (or (first approximation-string) "about")]
    (cond
      (pos? years) (if (= (readable-year years) "more than a quadrillion years")
                     "more than a quadrillion years"
                     (str apprx-word " " (readable-year years)))
      (pos? months) (str apprx-word " " months " " (ops/singular->plural "month" months))
      (pos? weeks) (str apprx-word " " weeks " " (ops/singular->plural "week" weeks))
      (pos? days) (str apprx-word " " days " " (ops/singular->plural "day" days))
      (pos? hours) (str apprx-word " " hours " " (ops/singular->plural "hour" hours))
      :else "less than an hour")))

(defn
  ^{:doc "Accepts `start` and `end` values (strings) only in the form of `DD-MM-YYYY` and `YYYY-MM-DD` formats. `DD` and `MM` could just be `D` or `M`.
          Returns a readable duration, but only in the highest unit, with lower bound of the value.
          Example: if the duration is between 1 and 2 years (ex: 1 year 10 months), then it will return `1 year`.
          Similarly, if the duration is between 10 to 11 months, then it will return `10 months`.
          Takes an optional `approximation-string`, defaults to `about`."
    :todo {1 "Use `error-margin` to compute approx timeline duration, rather than simply taking the lower bounds.
              Maybe, an alternate return value can be offered with better approximation, with an `about` string attached in each return value."
           2 "Return a string that can describe the duration as: `5 years, 10 months, 2 weeks, 4 days, and 2 hours.`"}} 
  readable-duration
  [{:keys [start end approximation-string]}]
  (cond
    (and start end) (if approximation-string 
                      (readable-duration-helper (ops/duration-descriptor start end) approximation-string)
                      (readable-duration-helper (ops/duration-descriptor start end)))
    (and start (not end)) (if approximation-string
                            (readable-duration-helper (ops/duration-descriptor start) approximation-string)
                            (readable-duration-helper (ops/duration-descriptor start)))
    (and (not start) end) (if approximation-string
                            (readable-duration-helper (ops/duration-descriptor end) approximation-string)
                            (readable-duration-helper (ops/duration-descriptor end)))
    :else (throw (ExceptionInfo. (str "Invalid inputs - :start " start ", :end " end)))))

(defn readable-moment
  "Accepts `date-string` only in the form of `DD-MM-YYYY` and `YYYY-MM-DD` formats. `DD` and `MM` could just be `D` or `M`.
   Describes a moment in histry. Useful for one-time events.
   Takes an optional moment descriptor map with the following keys:
    - `:prefix` - defaults to `Happened`.
    - `:suffix` - defaults to `ago`.
   One or both the keys may be provided in the map."
  [date-string & moement-desc-map]
  (str (or (:prefix (first moement-desc-map)) "Happened")
       " "
       (readable-duration {:start date-string})
       " "
       (or (:suffix (first moement-desc-map)) "ago")))

(defn- period-helper-start
  [start period-desc-map]
  (str (or (:start-desc period-desc-map) "Started")
       " "
       (readable-duration {:start start :approximation-string (or (:approximation-string period-desc-map) "about")})
       " "
       (or (:past-indicator period-desc-map) "ago")))

(defn- period-helper-end
  [end period-desc-map]
  (str (or (:end-desc period-desc-map) "Ended")
       " "
       (readable-duration {:end end :approximation-string (or (:approximation-string period-desc-map) "about")})
       " "
       (or (:past-indicator period-desc-map) "ago")))

(defn- period-helper-active
  [start end period-desc-map]
  (str (or (:period-desc period-desc-map) "Went on for")
       " "
       (readable-duration {:start start :end end :approximation-string (or (:approximation-string period-desc-map) "about")})))

(defn- period-helper-start-end
  [start end period-desc-map]
  (str (period-helper-start start period-desc-map)
       (or (:separator period-desc-map) " | ")
       (period-helper-active start end period-desc-map)
       (or (:separator period-desc-map) " | ")
       (period-helper-end end period-desc-map)))

(defn readable-period
  "Accepts `start` and `end` values (strings) only in the form of `DD-MM-YYYY` and `YYYY-MM-DD` formats. `DD` and `MM` could just be `D` or `M`.
   Takes an optional period description map with the following keys:
    - `:start-desc` - defaults to 'Started'.
    - `:end-desc` - defaults to 'Ended'.
    - `:period-desc` - defaults to 'Went on for'.
    - `:approximation-string` - defaults to 'about'.
    - `:past-indicator` - defaults to 'ago'.
    - `:separator` - defaults to ' | '. Note that there are spaces before and after the separator."
  [{:keys [start end period-desc]}]
  (cond
    (and start (nil? end)) (period-helper-start start period-desc)
    (and (nil? start) end) (period-helper-end end period-desc)
    (and start end) (period-helper-start-end start end period-desc)
    :else (throw (ExceptionInfo. (str "Invalid inputs - :start " start ", :end " end ", :period-desc " period-desc)))))

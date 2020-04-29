(ns
 ^{:todo "Add more tests."} 
  humane-time.test-core
  (:require [cljs.test :refer-macros [is are deftest testing use-fixtures]]
            [clojure.test.check.generators :as gen]
            [clojure.test.check :as tc]
            [clojure.test.check.clojure-test :refer-macros [defspec]]
            [clojure.test.check.properties :as prop :include-macros true]
            [humane-time.core :as ht]
            [humane-time.ops :as ops]
            [humane-time.validators :as hv]
            [cljs-time.core :as t]
            [cljs.reader :as r]))

(defspec datetime-descriptor-test
  2000
  (prop/for-all [v (gen/hash-map :year (gen/large-integer* {:min 1000, :max 9999})
                                 :month (gen/large-integer* {:min 1, :max 12})
                                 :day (gen/large-integer* {:min 1, :max 31}))]
                (try
                  (let [date-string (clojure.string/join "-" [(:year v) (:month v) (:day v)])
                        dt-obj (t/date-time (:year v) (:month v) (:day v))]
                    (= (dissoc (ops/datetime-descriptor date-string) :datetime-obj)
                       {:year (:year v)
                        :month-number (:month v)
                        :month-name (ops/month-index (:month v))
                        :month-name-short (ops/month-index-short (:month v))
                        :day-of-week (t/day-of-week dt-obj)
                        :day-of-month (t/day dt-obj)
                        :day-name (ops/week-index (t/day-of-week dt-obj))
                        :day-name-short (ops/week-index-short (t/day-of-week dt-obj))}))
                  (catch ExceptionInfo e
                    (= (ex-message e)
                       (str "Invalid date-string - :day "
                            (:day v) ", :month " (:month v) ", :year " (:year v)))))))

;(datetime-descriptor-test)

(defspec readable-date-test
  2000
  (prop/for-all [v (gen/hash-map :year (gen/large-integer* {:min 1000, :max 9999})
                                 :month (gen/large-integer* {:min 1, :max 12})
                                 :day (gen/large-integer* {:min 1, :max 31}))
                 opt (gen/hash-map :day-name? gen/boolean :short-names? gen/boolean)]
                (try
                  (let [date-string (clojure.string/join "-" [(:year v) (:month v) (:day v)])
                        dt (ops/datetime-descriptor date-string)]
                    (and
                     (= (ht/readable-date date-string) 
                        (str (:day-name dt) ", " (:month-name dt) " " (:day v) ", " (:year v)))
                     (if (:day-name? opt)
                       (= (ht/readable-date date-string opt)
                          (str (if (:short-names? opt) (:day-name-short dt) (:day-name dt)) 
                               ", " 
                               (if (:short-names? opt) (:month-name-short dt) (:month-name dt))
                               " " (:day v) ", " (:year v)))
                       (= (ht/readable-date date-string opt)
                          (str (if (:short-names? opt) (:month-name-short dt) (:month-name dt))
                               " " (:day v) ", " (:year v))))))
                  (catch ExceptionInfo e
                    (= (ex-message e)
                       (str "Invalid date-string - :day " 
                            (:day v) ", :month " (:month v) ", :year " (:year v)))))))

;(readable-date-test)

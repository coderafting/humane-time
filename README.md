# humane-time
[![Clojars Project](https://img.shields.io/clojars/v/coderafting/humane-time.svg)](https://clojars.org/coderafting/humane-time)
[![cljdoc badge](https://cljdoc.org/badge/coderafting/humane-time)](https://cljdoc.org/d/coderafting/humane-time/CURRENT)

A ClojureScript library to produce human readable date-time and intervals.

## When to use?
When you wish to show to your users something like `World war 1 started x years ago, went on for y years, and ended z years ago` rather than saying `World war 1 started on Aug 16, 2016, and ended on Nov 19, 2019`. Please see an example of its usage on [WhenInHistory](https://wheninhistory.com).

More on this in the **Premise** and **Usage** sections below.

## When not to use?
This is not to be used for date-time related computations in programs. Please use one of [cljs-time](https://github.com/andrewmcveigh/cljs-time) or [tick](https://github.com/juxt/tick) for that purpose.

## Premise
It is often difficult for us to relate to plain numbers without any real-life context to them. It is the context that make any number meaningful to us. For example, it makes little sense to us when we read `this city's population density is xxx`, but if transform this sentence into something like: `the city has capacity to host xxx families, but currently yyy families live there..."`. Suddenly we have some context in which we can understand how densly populated the city is.

So, context matters. However, context is different for different people. The statement `this city's population density is xxx` might be completely fine for someone who is dealing with such numbers on regular basis. Similarly, the statement `the city has capacity to host xxx families, but currently yyy families live there..."` might be interepreted slightly differently by someone living in a village than by someone who has spent the whole life in a densly populated city. But, at least this second statement does help in understanding the population density to people who are not dealing with such numbers on a regular basis - it's another matter that one person might call this city a densly populated, and another might call it sparsely populated. But, with the earlier statement of `this city's population density is xxx`, none of these people can say anything about population density, because they don't have a reference point in their minds to compare to.

This situation holds true for any kind of measurement, including time. The statement `World war 1 started on Aug 16, 2016, and ended on Nov 19, 2019` holds little or no meaning to a normal person, but the statement `World war 1 started x years ago, went on for y years, and ended z years ago` makes much sense.

This library is an attempt to provide some useful functions to compose such meaningful statements, given historical date-times.

This library represents an idea. There are different ways to implement this idea, the implementation provided by this library is just one of them.

While this library focuses on date-time, similar implementations can be achieved for other types of measurements, such as length or weight.

## Usage

### Setup
Add this library as a dependency to your project as:
```clojure
[coderafting/humane-time "0.1.0"]
```
Require `humane-time.core` in your working namespace. It is advisable that you only use `humane-time.core` namespace and its public functions.
```clojure
(ns your-project-namespace
  (:require [humane-time.core :as ht]))
```
### Available functions
Currently, there are 5 public functions available for consumption. Each of these functions have `readable-` prefix in their names. They are:
```clojure
readable-date
readable-year
readable-duration
readable-moment
readable-period
```
We will go through each of them below.

#### readable-date
The function is defined as:
```clojure
(defn readable-date
  [date-string & opts-map]
  ...body...)
```
The function accepts date-string only in the form of `DD-MM-YYYY` and `YYYY-MM-DD` formats. `DD` and `MM` could just be `D` or `M`. It returns a string similar to: `April 27, 2020` or `Apr 27, 2020` or `Monday, April 27, 2020` or `Mon, April 27, 2020` or `Mon, Apr 27, 2020`. An optional options map can be provided to indicate the return format. It includes the following keys:
```clojure
:day-name? - defaults to true
:short-names? - defaults to false
```
Examples below:

```clojure
(readable-date "15-06-1984")
=> ""

(readable-date "1984-06-15" {:short-names? true})
=> ""

(readable-date "1984-6-15" {:day-name? false :short-names? true})
=> ""
```

#### readable-year
The function is defined as:
```clojure
(defn readable-year
  [val]
  ...body...)
```
It returns a human readable string based on the value of number-of-years (`val`). Currently, it uses the US counting system. Please refer: https://www.britannica.com/topic/large-numbers-1765137.

Examples below:

```clojure
(readable-year 2)
=> ""

(readable-year 25678)
=> ""
```

#### readable-duration
The function is defined as:
```clojure
(defn readable-duration
  [{:keys [start end approximation-string]}]
  ...body...)
```

The function accepts `start` and `end` values (strings) only in the form of `DD-MM-YYYY` and `YYYY-MM-DD` formats. `DD` and `MM` could just be `D` or `M`. It returns a readable duration, but only in the highest unit, with lower bound of the value. Example: if the duration is between 1 and 2 years (ex: 1 year 10 months), then it will return `1 year`. Similarly, if the duration is between 10 to 11 months, then it will return `10 months`. It takes an optional approximation-string, defaults to `about`. The customizable approximation-string helps in including words that gel well with the sentences in which the return value might be included.

Examples below:

```clojure
(readable-duration {:start "15-06-1984" :end "15-06-1985"})
=> ""

(readable-duration {:start "15-06-1984" :end "15-06-1985" :approximation-string "roughly"})
=> ""
```

#### readable-moment
The function is defined as:
```clojure
(defn readable-moment
  [date-string & moement-desc-map]
  ...body...)
```

The function accepts date-string only in the form of `DD-MM-YYYY` and `YYYY-MM-DD` formats. `DD` and `MM` could just be `D` or `M`. The return value describes a moment in histry. It is useful for one-time events. It takes an optional moment descriptor map with the following keys (one or both the keys may be provided in the map):
```clojure
:prefix - defaults to 'Happened'
:suffix - defaults to 'ago'
```
Again, the customizable moment descriptors allow appropriate words that gel well with the sentences in which the return value might be included.

Examples below:

```clojure
(readable-moment "15-05-1984")
=> ""
(readable-moment "15-05-1984" {:prefix "It happened" :suffix "before"})
=> ""

```

#### readable-period
The function is defined as:
```clojure
(defn readable-period
  [{:keys [start end period-desc]}]
  ...body...)
```

The function accepts `start` and `end` values (strings) only in the form of `DD-MM-YYYY` and `YYYY-MM-DD` formats. `DD` and `MM` could just be `D` or `M`. It takes an optional period description map with the following keys:
```clojure
:start-desc - defaults to 'Started'.
:end-desc - defaults to 'Ended'.
:period-desc - defaults to 'Went on for'.
:approximation-string - defaults to 'about'.
:past-indicator - defaults to 'ago'.
:separator - defaults to ' | '. Note that there are spaces before and after the separator.
```

Examples below:

```clojure
(readable-period {:start "15-05-1984" :end "15-05-1986"})
=> ""

(readable-period {:start "15-05-1984" :end "15-05-1986" :period-desc {:separator ", "}})
=> ""

(readable-period {:start "15-05-1984"
                  :end "15-05-1986"
                  :period-desc {:start-desc "went"
                                :end-desc "and returned"
                                :period-desc "stayed there for"
                                :separator ", "}})
=> ""

```

## Feedback/Discussions
Github issues are a good way to discuss library related topics. I am also reachable via [CodeRafting](https://www.coderafting.com/).

## License
Distributed under the MIT License.
Copyright (c) 2020 [Amarjeet Yadav](https://www.coderafting.com/)

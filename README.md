# humane-time
[![Clojars Project](https://img.shields.io/clojars/v/coderafting/humane-time.svg)](https://clojars.org/coderafting/humane-time)
[![cljdoc badge](https://cljdoc.org/badge/coderafting/humane-time)](https://cljdoc.org/d/coderafting/humane-time/CURRENT)

A ClojureScript library to help produce meaningful statements out of date-time and intervals.

## When to use?
When you wish to show to your users `World War 1 started about 105 years ago, was fought for about 4 years, and ended about 101 years ago` instead of showing `World war 1 started on July 28, 1914 and ended on November 11, 1918`. Please see an example of its usage on [WhenInHistory](https://wheninhistory.com).

More on this in the **Premise** and **Usage** sections below.

## When not to use?
This library should not be used for date-time related computations in programs. Please use one of [cljs-time](https://github.com/andrewmcveigh/cljs-time) or [tick](https://github.com/juxt/tick) for that purpose.

## Premise
It is difficult for most of us to be able to relate to numbers without any real-life references to relate to. It is the real-life references that make a number meaningful for most of us. For example, this sentence makes little sense to many: `the city's population density is 8000 km-square`. However, this might make much sense: `the city has a capacity to host 2 million families, but currently hosts only about 1 million families`. Based on this, we can infer how densely the city is populated.

So, real-life references matters.

This library represents an idea that can be applied to any measurement context, including time, length, area, or weight. There are different ways to implement this idea, `humane-time` offers just one of them.

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
The function accepts `date-string` only in the form of `DD-MM-YYYY` and `YYYY-MM-DD` formats. `DD` and `MM` could just be `D` or `M`. It returns a string similar to: `April 27, 2020` or `Apr 27, 2020` or `Monday, April 27, 2020` or `Mon, April 27, 2020` or `Mon, Apr 27, 2020`. An optional `opts-map` can be provided to indicate the return format. It includes the following keys:
```clojure
:day-name? - defaults to true
:short-names? - defaults to false
```
Examples below:

```clojure
(ht/readable-date "15-06-1984")
=> "Friday, June 15, 1984"

(ht/readable-date "1984-06-15" {:short-names? true})
=> "Fri, Jun 15, 1984"

(ht/readable-date "1984-6-15" {:day-name? false :short-names? true})
=> "Jun 15, 1984"
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
(ht/readable-year 2)
=> "2 years"

(ht/readable-year 25678)
=> "25 thousand years"
```

#### readable-duration
The function is defined as:
```clojure
(defn readable-duration
  [{:keys [start end approximation-string]}]
  ...body...)
```

The function accepts `start` and `end` values (strings) only in the form of `DD-MM-YYYY` and `YYYY-MM-DD` formats. `DD` and `MM` could just be `D` or `M`. It returns a readable duration, but only in the highest unit, with lower bound of the value. Example: if the duration is between 1 and 2 years (ex: 1 year 10 months), then it will return `1 year`. Similarly, if the duration is between 10 to 11 months, then it will return `10 months`. It takes an optional `approximation-string`, which defaults to `about`. Users can replace the default value with strings such as `roughly` or `approximately`.

Examples below:

```clojure
(ht/readable-duration {:start "15-06-1984" :end "15-06-1987"})
=> "about 3 years"

(ht/readable-duration {:start "15-06-1984" :end "15-06-1985" :approximation-string "roughly"})
=> "roughly 1 year"
```

#### readable-moment
The function is defined as:
```clojure
(defn readable-moment
  [date-string & moement-desc-map]
  ...body...)
```

The function accepts date-string only in the form of `DD-MM-YYYY` and `YYYY-MM-DD` formats. `DD` and `MM` could just be `D` or `M`. The return value describes a moment in history. It is useful for one-time events. It takes an optional moment descriptor map (`moement-desc-map`) with the following keys (one or both the keys may be provided in the map):
```clojure
:prefix - defaults to 'Happened'
:suffix - defaults to 'ago'
```
Again, the customizable moment descriptors allow users to provide their own strings that are appropriate for their use-cases.

Examples below:

```clojure
(ht/readable-moment "15-05-1984")
=> "Happened about 35 years ago"

(ht/readable-moment "15-05-1984" {:prefix "It happened" :suffix "before"})
=> "It happened about 35 years before"

```

#### readable-period
The function is defined as:
```clojure
(defn readable-period
  [{:keys [start end period-desc]}]
  ...body...)
```

The function accepts `start` and `end` values (strings) only in the form of `DD-MM-YYYY` and `YYYY-MM-DD` formats. `DD` and `MM` could just be `D` or `M`. It takes an optional period description map (`period-desc`) with the following keys:
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
(ht/readable-period {:start "15-05-1984" :end "15-05-1986"})
=> "Started about 35 years ago | Went on for about 2 years | Ended about 33 years ago"

(ht/readable-period {:start "15-05-1984" :end "15-05-1986" :period-desc {:separator ", "}})
=> "Started about 35 years ago, Went on for about 2 years, Ended about 33 years ago"

(ht/readable-period {:start "28-7-1914"
                     :end "11-11-1918"
                     :period-desc {:start-desc "World War 1 started"
                                   :end-desc "and ended"
                                   :period-desc "was fought for"
                                   :separator ", "}})
=> "World War 1 started about 105 years ago, was fought for about 4 years, and ended about 101 years ago"
```

## Feedback/Discussions
Github issues are a good way to discuss library related topics. I am also reachable via [CodeRafting](https://www.coderafting.com/).

## License
Distributed under the MIT License. Copyright (c) 2020 [Amarjeet Yadav](https://www.coderafting.com/)

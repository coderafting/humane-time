# humane-time
[![Clojars Project](https://img.shields.io/clojars/v/coderafting/humane-time.svg)](https://clojars.org/coderafting/humane-time)
[![cljdoc badge](https://cljdoc.org/badge/coderafting/humane-time)](https://cljdoc.org/d/coderafting/humane-time/CURRENT)

A ClojureScript library to help produce meaningful statements out of date-time and intervals.

## When to use?
When you wish to show to your users a meaningful description of a date-time or an interval.

For example, if a time-interval has a start (`15-05-1984`) and an end date (`15-05-1986`), this library can help you generate a description as **`Started about 35 years ago, Went on for about 2 years, Ended about 33 years ago`**.

Take a look at some samples below:
```clojure
(ns your-project-namespace
  (:require [humane-time.core :as ht]))

(ht/readable-moment "15-10-1984")
=> "Happened about 35 years ago"

(ht/readable-period {:start "28-7-1914"
                     :end "11-11-1918"
                     :period-desc {:start-desc "World War 1 started"
                                   :end-desc "and ended"
                                   :period-desc "was fought for"
                                   :separator ", "}})
=> "World War 1 started about 105 years ago, was fought for about 4 years, and ended about 101 years ago"
```

More on this in the **Premise** and **Usage** sections below.

## When not to use?
This library should not be used for date-time related operations in programs. Please use one of [cljs-time](https://github.com/andrewmcveigh/cljs-time) or [tick](https://github.com/juxt/tick) for that purpose.

## Premise
Many a times, when we look at a numerical fact, we unconsciously think - **`so?`**. This happens because we want to understand how this is related to our lives and what is its significance? If we can understand its significance, we can empathize with the fact.

For example, **`The World War 2 ended in September 1945`** is very different from **`The World War 2 ended about 75 years ago`**. We can relate to the second statement in a more meaningful manner than the first one. The second statement invokes some emotions, such as - `wow, it's been so long! are we really at peace now!`. Such emotions are not invoked by the first statement.

It is because this is how we communicate and understand things in real-life. Take another example: we normally say **`I returned a week ago from my holidays`**, rather than **`I returned on April 23, 2020`**. In this case, the intention is to let the person know how long it's been since I returned. So, we give a high-level idea of the duration that the person can understand easily.

This idea applies to any measurement context, including **`time`**, **`length`**, **`area`**, or **`weight`**. For example, the sentence **`the city's population density is 8000 people per km-square`** makes little sense to many. However, this one might make much sense: **`the city has a capacity to host 2 million families, but currently hosts only about 1 million families`**. Based on this, we can infer how densely the city is populated.

This library intends to offer a helping hand in providing meanings to facts in the context of **`time`**.

## Usage

### Setup
Add this library as a dependency to your project as:
```clojure
[coderafting/humane-time "0.1.0"]
```
Require `humane-time.core` in your working namespace. It is advisable that you only use `humane-time.core` namespace.
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
It returns a human readable string based on the value of number-of-years (`val`). Currently, it uses the [US counting system](https://www.britannica.com/topic/large-numbers-1765137).

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

## Some high-level TODOs (not in the order of priority)
- `Clj` compatibility
- Accept `1918-11-11T11:10:50` date-time format.
- Option for users to provide custome date-time validators, for faster validation than the default one.
- Include `future` date-time and intervals.
- Some more friendly options, such as `tomorrow` and `yesterday`.
- Option to spell out the numbers. Ex: `2 days ago` and `two days ago`.

## License
Distributed under the MIT License. Copyright (c) 2020 [Amarjeet Yadav](https://www.coderafting.com/).

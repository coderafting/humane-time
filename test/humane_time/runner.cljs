(ns humane-time.runner
  (:require
    [doo.runner :refer-macros [doo-tests]]
    [humane-time.test-core]))

(doo-tests 'humane-time.test-core)

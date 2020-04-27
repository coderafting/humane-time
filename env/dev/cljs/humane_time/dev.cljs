(ns ^:figwheel-no-load humane-time.dev
  (:require
    [humane-time.test-page :as test-page]
    [devtools.core :as devtools]))

(devtools/install!)

(enable-console-print!)

(test-page/init!)

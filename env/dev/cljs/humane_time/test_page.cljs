(ns humane-time.test-page
  (:require [humane-time.core :as core]
            [reagent.core :as r]))

(defn home-page []
  [:p "Hello World!"])

(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))

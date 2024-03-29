(defproject coderafting/humane-time "0.1.0"
  :author "Amarjeet Yadav <https://coderafting.com/>"
  :description "A ClojureScript library to produce human readable date-time and intervals."
  :url "https://github.com/coderafting/humane-time"
  :license {:name "The MIT License"
            :url "https://opensource.org/licenses/MIT"}
  :repositories [["clojars" {:sign-releases false}]]
  :dependencies
  [[org.clojure/clojure "1.10.1" :scope "provided"]
   [org.clojure/clojurescript "1.10.597" :scope "provided"]
   [com.andrewmcveigh/cljs-time "0.5.2"]]
  :plugins
  [[lein-cljsbuild "1.1.8"]
   [lein-figwheel "0.5.19"]
   [cider/cider-nrepl "0.24.0"]
   [lein-doo "0.1.10"]]

  :clojurescript? true
  :jar-exclusions [#"\.swp|\.swo|\.DS_Store"]
  :clean-targets ^{:protect false}
  [:target-path
   [:cljsbuild :builds :app :compiler :output-dir]
   [:cljsbuild :builds :app :compiler :output-to]]

  :profiles
  {:dev
   {:dependencies
    [[ring-server "0.5.0"]
     [ring-webjars "0.2.0"]
     [ring "1.8.0"]
     [ring/ring-defaults "0.3.2"]
     [metosin/reitit "0.3.10"]
     [metosin/reitit-ring "0.3.10"]
     [hiccup "1.0.5"]
     [nrepl "0.7.0"]
     [binaryage/devtools "0.9.11"]
     [cider/piggieback "0.4.2"]
     [figwheel-sidecar "0.5.19"]
     [reagent "0.9.0-rc4"]
     [org.clojure/test.check "1.0.0"]]

    :source-paths ["src" "env/dev/clj" "env/dev/cljs"]
    :resource-paths ["resources" "env/dev/resources" "target/cljsbuild"]

    :figwheel
    {:server-port      3450
     :nrepl-port       7000
     :nrepl-middleware [cider.piggieback/wrap-cljs-repl
                        cider.nrepl/cider-middleware]
     :css-dirs         ["resources/public/css" "env/dev/resources/public/css"]
     :ring-handler     humane-time.server/app}
    :cljsbuild
    {:builds
     {:app
      {:source-paths ["src" "env/dev/cljs"]
       :figwheel     {:on-jsload "humane-time.test-page/mount-root"}
       :compiler     {:main          humane-time.dev
                      :asset-path    "/js/out"
                      :output-to     "target/cljsbuild/public/js/app.js"
                      :output-dir    "target/cljsbuild/public/js/out"
                      :source-map-timestamp true
                      :source-map    true
                      :optimizations :none
                      :pretty-print  true}}}}}
   :test
   {:cljsbuild
    {:builds
     {:test
      {:source-paths ["src" "test"]
       :compiler     {:main          humane-time.runner
                      :output-to     "target/test/core.js"
                      :target        :nodejs
                      :optimizations :none
                      :source-map    true
                      :pretty-print  true}}}}
    :doo {:build "test"}}}
  :aliases
  {"test"
   ["do"
    ["clean"]
    ["with-profile" "test" "doo" "node" "once"]]
   "test-watch"
   ["do"
    ["clean"]
    ["with-profile" "test" "doo" "node"]]})

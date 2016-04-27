(defproject clj-brnolib "0.1.0-SNAPSHOT"
  :description "Utility library for our projects"
  :url "https://github.com/kajism/clj-brnolib"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.8.40"]
                 [org.clojure/tools.nrepl "0.2.12"]
                 [clj-time "0.11.0"]
                 [com.andrewmcveigh/cljs-time "0.4.0"]
                 [com.cognitect/transit-clj "0.8.285"]
                 [com.cognitect/transit-cljs "0.8.237"]
                 [hiccup "1.0.5"]
                 [ring "1.4.0"]
                 [ring/ring-defaults "0.2.0"]
                 [prismatic/schema "1.1.0"]
                 [re-frame "0.7.0"]
                 [com.taoensso/timbre "4.3.1"]
                 [com.taoensso/truss "1.2.0"]
                 [re-com "0.8.1"]
                 [org.clojure/tools.namespace "0.3.0-alpha3"]]
  :profiles {:dev {:dependencies [[com.cemerick/piggieback "0.2.1"]
                                  [com.datomic/datomic-free "0.9.5350"]
                                  [org.clojure/java.jdbc "0.5.8"]                                  ]
                   :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}}})

(defproject cljockwork "0.1.0-SNAPSHOT"
  :description "A simple REST API to cron4j"
  :url https://github.com/oliyh/cljockwork
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [ring "1.2.0"]
                 [ring-server "0.2.8" :exclusions [[org.clojure/clojure]
                                                   [ring]]]
                 [ring/ring-json "0.2.0"]
                 [compojure "1.1.5" :exclusions [[org.clojure/clojure] [ring/ring-core]]]
                 [cheshire "5.2.0"]
                 [clj-http "0.7.6"]
                 [org.clojars.gmazelier/cron4j "2.2.5"]]
  :plugins [[lein-ring "0.8.3" :exclusions [org.clojure/clojure]]]
  :profiles {:production
             {:ring {:open-browser? false, :stacktraces? false, :auto-reload? false}}}
  :ring {:handler cljockwork.ring/war-handler
         :init cljockwork.ring/init
         :destroy cljockwork.ring/destroy}
  :main cljockwork.server)

(ns cljockwork.ring
  (:require [cljockwork.routes :as routes]
            [ring.middleware.json :as ring-json]
            [compojure.handler :as handler]
            [cljockwork.scheduler :as scheduler]))

(defn init []
  (println "Cljockwork is starting")
  (scheduler/start))

(defn destroy []
  (println "Cljockwork is stopping")
  (scheduler/stop))

(def app routes/all-routes)

(defn get-handler [app]
  (-> (handler/api app)
      (ring-json/wrap-json-body {:keywords? true})
      (ring-json/wrap-json-response)))

(def war-handler (get-handler app))

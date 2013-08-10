(ns cljockwork.ring
  (:require [cljockwork.routes :as routes]
            [ring.middleware.json :as ring-json]))

(defn init []
  (println "Cljockwork is starting"))

(defn destroy []
  (println "Cljockwork is stopping"))

(def app routes/all-routes)

(defn get-handler [app]
  (-> app
      (ring-json/wrap-json-response app)))

(def war-handler (get-handler app))

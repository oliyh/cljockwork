(ns cljockwork.ring
  (:require [cljockwork.routes :as routes]))

(defn init []
  (println "Cljockwork is starting"))

(defn destroy []
  (println "Cljockwork is stopping"))

(def app routes/all-routes)

(defn get-handler [app]
  (-> app
      ;; middleware wrappers go here
      ))

(def war-handler (get-handler app))

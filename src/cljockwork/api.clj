(ns cljockwork.api
  (:use [ring.util.response])
  (:require [cljockwork.scheduler :as scheduler]))

(defn index []
  (response {:response "Hello World"}))

(defn current-status []
  (response (scheduler/status)))

(defn start []
  (scheduler/start)
  (current-status))

(defn stop []
  (scheduler/stop)
  (current-status))

(defn list-all-tasks []
  (response []))

(defn view-task [id]
  (response (scheduler/view id)))

(defn schedule-task [cron task-endpoint]
  (response (scheduler/schedule cron #(println "Running task for" task-endpoint))))

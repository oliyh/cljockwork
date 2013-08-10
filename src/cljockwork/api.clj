(ns cljockwork.api
  (:use [ring.util.response])
  (:require [cljockwork.scheduler :as scheduler]))

(defn index []
  (slurp "resources/public/index.html"))

(defn current-status []
  (response (scheduler/status)))

(defn start []
  (scheduler/start)
  (current-status))

(defn stop []
  (scheduler/stop)
  (current-status))

(defn list-all-tasks []
  (response (scheduler/view-all)))

(defn view-task [id]
  (response (scheduler/view id)))

(defn schedule-task [desc cron task-endpoint]
  (response (scheduler/schedule desc cron task-endpoint)))

(defn unschedule-task [id]
  (response (scheduler/unschedule id)))

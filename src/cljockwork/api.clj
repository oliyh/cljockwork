(ns cljockwork.api
  (:use [ring.util.response])
  (:require [cljockwork.scheduler :as scheduler]
            [clj-http.client :as client]))

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

(defn validate-endpoint [endpoint]
  (try (= 200 (:status (client/get endpoint)))
       (catch Exception _ false)))

(defn validate-task [cron endpoint]
  (response {:schedule (scheduler/validate-schedule cron)
             :endpoint (validate-endpoint endpoint)}))

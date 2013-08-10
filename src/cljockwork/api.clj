(ns cljockwork.api
  (:use [ring.util.response])
  (:require [cljockwork.scheduler :as scheduler]))

(defn index []
  (response {:response "Hello World"}))

(defn list-all-jobs []
  (response []))

(defn view-job [id]
  (scheduler/view id))

(defn schedule-task [cron task-endpoint]
  (scheduler/schedule cron #(println "Running task for " task-endpoint)))

(ns cljockwork.api
  (:use [ring.util.response])
  (:require [cljockwork.scheduler :as scheduler]
            [clj-http.client :as client]
            [clj-time.format :as format]))

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

(defn schedule-task [{:keys [desc schedule endpoint method]}]
  (response (scheduler/schedule desc schedule endpoint method)))

(defn unschedule-task [id]
  (response (scheduler/unschedule id)))

(defn pause-task [id]
  (response (scheduler/pause id)))

(defn activate-task [id]
  (response (scheduler/activate id)))

(defn validate-endpoint [method endpoint]
  (try (= 200 (:status (client/request {:method (keyword method) :url endpoint})))
       (catch Exception _ false)))

(defn validate-task [{:keys [schedule method endpoint]}]
  (response {:schedule (scheduler/validate-schedule schedule)
             :endpoint (validate-endpoint method endpoint)}))

(defn string-time [event]
  (assoc event :time (format/unparse (format/formatters :date-hour-minute-second) (:time event))))

(defn recent-events []
  (map string-time (scheduler/recent-events)))

(defn config []
  (response {:methods (scheduler/supported-methods)}))

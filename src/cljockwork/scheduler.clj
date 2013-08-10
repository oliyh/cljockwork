(ns cljockwork.scheduler
  (:import [it.sauronsoftware.cron4j Scheduler]))

(defonce scheduler (Scheduler.))

(defn start []
  (.start scheduler)
  (println "Started scheduler"))

(defn stop []
  (.stop scheduler)
  (println "Stopped scheduler"))

(defn status []
  (let [started? (.isStarted scheduler)]
    {:status (if started? :running :stopped)
     :running-tasks (if started? (count (.getExecutingTasks scheduler)) 0)
     :timezone (-> scheduler .getTimeZone .getID)}))

(defn schedule [cron f]
  (let [id (.schedule scheduler cron f)]
    {:id id
     :cron cron}))

(defn view [id]
  (.getTask scheduler id))

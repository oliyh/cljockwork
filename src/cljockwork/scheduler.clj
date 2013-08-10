(ns cljockwork.scheduler
  (:import [it.sauronsoftware.cron4j Scheduler Task SchedulingPattern]))

(defonce scheduler (Scheduler.))
(def tasks (atom {}))

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

(defn task-for [endpoint]
  (proxy [Task] [] (execute [ctx] (println "Running task for" endpoint))))

(defn schedule [desc scheduling-pattern endpoint]
  (let [task {:id (.schedule scheduler scheduling-pattern (task-for endpoint))
              :desc desc
              :endpoint endpoint
              :schedule scheduling-pattern}]
    (swap! tasks assoc (:id task) task)
    task))

(defn view [id]
  (get @tasks id))

(defn view-all []
  (map val @tasks))

(ns cljockwork.scheduler
  (:import [it.sauronsoftware.cron4j Scheduler Task SchedulingPattern TaskCollector TaskTable]
           [java.util UUID]))

(defonce scheduler (Scheduler.))
(defonce tasks (atom {}))

(defn task-for [endpoint]
  (proxy [Task] [] (execute [ctx] (println "Running task for" endpoint))))

(def task-collector
  (reify TaskCollector
    (getTasks [this]
      (let [table (TaskTable.)]
        (doseq [task (map val @tasks)]
          (.add table (SchedulingPattern. (:schedule task)) (task-for (:endpoint task))))
        table))))

(defn start []
  (doto scheduler
    (.addTaskCollector task-collector)
    (.start))
  (println "Started scheduler"))

(defn stop []
  (.stop scheduler)
  (println "Stopped scheduler"))

(defn status []
  (let [started? (.isStarted scheduler)]
    {:status (if started? :running :stopped)
     :running-tasks (if started? (count (.getExecutingTasks scheduler)) 0)
     :timezone (-> scheduler .getTimeZone .getID)}))

(defn schedule [desc scheduling-pattern endpoint]
  (let [task {:id (str (UUID/randomUUID))
              :desc desc
              :endpoint endpoint
              :schedule scheduling-pattern}]
    (swap! tasks assoc (:id task) task)
    task))

(defn unschedule [id]
  (let [task (get @tasks id)]
    (swap! tasks dissoc id)
    task))

(defn view [id]
  (get @tasks id))

(defn view-all []
  (map val @tasks))

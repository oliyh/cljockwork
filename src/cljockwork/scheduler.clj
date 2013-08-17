(ns cljockwork.scheduler
  (:require [clj-http.client :as client]
            [clj-time.core :as time])
  (:import [it.sauronsoftware.cron4j
            Scheduler Task SchedulingPattern TaskCollector TaskTable Predictor]
           [java.util UUID]
           [org.joda.time DateTime]))

(defonce tasks (atom {}))
(defonce events (atom []))

(defn task-for [{:keys [id endpoint]}]
  (proxy [Task] [] (execute [ctx] (do
                                    (swap! events (fn [old]
                                                    (cons {:id id :time (time/now)} (take 4 old))))
                                    (println "Running task" id ":" endpoint)
                                    (client/get endpoint)))))

(def task-collector
  (reify TaskCollector
    (getTasks [this]
      (let [table (TaskTable.)]
        (doseq [task (filter #(= :active (:state %)) (map val @tasks))]
          (println task)
          (.add table (SchedulingPattern. (:schedule task)) (task-for task)))
        table))))
(defonce scheduler (doto (Scheduler.)
                     (.addTaskCollector task-collector)))

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

(defn schedule [desc scheduling-pattern endpoint]
  (let [task {:id (str (UUID/randomUUID))
              :desc desc
              :endpoint endpoint
              :schedule scheduling-pattern
              :state :active}]
    (swap! tasks assoc (:id task) task)
    task))

(defn unschedule [id]
  (let [task (get @tasks id)]
    (swap! tasks dissoc id)
    task))

(defn pause [id]
  (let [task (get @tasks id)]
    (swap! tasks assoc id (assoc task :state :paused))
    task))

(defn add-prediction [now task]
  (let [prediction (.nextMatchingTime (Predictor. (:schedule task)))]
    (assoc task
      :prediction prediction
      :interval (time/in-secs (time/interval now (DateTime. prediction))))))

(defn view [id]
  (add-prediction (time/now) (get @tasks id)))

(defn view-all []
  (let [now (time/now)]
    (map #(add-prediction now (val %)) @tasks)))

(defn validate-schedule [schedule]
  (SchedulingPattern/validate schedule))

(defn recent-events []
  @events)

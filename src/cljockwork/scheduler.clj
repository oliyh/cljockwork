(ns cljockwork.scheduler
  (:require [clj-http.client :as client]
            [clj-time.core :as time])
  (:import [it.sauronsoftware.cron4j
            Scheduler Task SchedulingPattern TaskCollector TaskTable Predictor]
           [java.util UUID]
           [org.joda.time DateTime]))

(defonce tasks (atom {}))
(defonce events (atom []))

(defmulti hit-endpoint (fn [{method :method}] method))

(defmethod hit-endpoint :get [{endpoint :endpoint}]
  (client/get endpoint))

(defmethod hit-endpoint :post [{endpoint :endpoint}]
  (client/post endpoint))


(defn supported-methods []
  (keys (methods hit-endpoint)))

(defn task-for [{:keys [id endpoint] :as task}]
  (proxy [Task] [] (execute [ctx] (do
                                    (swap! events (fn [old]
                                                    (cons {:id id :time (time/now)} (take 4 old))))
                                    (println "Running task" id ":" endpoint)
                                    (hit-endpoint task)))))

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

(defn started? []
  (.isStarted scheduler))

(defn status []
  (let [started? (started?)]
    {:status (if started? :running :stopped)
     :running-tasks (if started? (count (.getExecutingTasks scheduler)) 0)
     :timezone (-> scheduler .getTimeZone .getID)
     :registered-tasks (count @tasks)}))

(defn schedule [desc scheduling-pattern endpoint method]
  (let [task {:id (str (UUID/randomUUID))
              :desc desc
              :endpoint endpoint
              :schedule scheduling-pattern
              :state :active
              :method (keyword method)}]
    (swap! tasks assoc (:id task) task)
    task))

(defn unschedule [id]
  (let [task (get @tasks id)]
    (swap! tasks dissoc id)
    task))

(defn- set-state [id state]
  (let [task (get @tasks id)]
    (swap! tasks assoc id (assoc task :state state))
    task))

(defn pause [id]
  (set-state id :paused))

(defn activate [id]
  (set-state id :active))

(defn add-prediction [now task]
  (if (or (not (started?)) (= :paused (:state task)))
    task
    (let [prediction (.nextMatchingTime (Predictor. (:schedule task)))]
      (assoc task
        :prediction prediction
        :interval (time/in-secs (time/interval now (DateTime. prediction)))))))

(defn view [id]
  (add-prediction (time/now) (get @tasks id)))

(defn view-all []
  (let [now (time/now)]
    (map #(add-prediction now (val %)) @tasks)))

(defn validate-schedule [schedule]
  (SchedulingPattern/validate schedule))

(defn recent-events []
  @events)

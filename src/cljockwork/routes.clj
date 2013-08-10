(ns cljockwork.routes
  (:require [compojure.core
             :as c-core
             :refer [defroutes GET POST PUT DELETE HEAD OPTIONS PATCH ANY]]
            [compojure.route :as c-route]
            ;; Public APIs
            [cljockwork.api :as api]))

(defroutes site
  (GET "/" [] (api/index))

  (GET "/stop" [] (api/stop))
  (GET "/start" [] (api/start))
  (GET "/status" [] (api/current-status))

  (GET "/tasks/" [] (api/list-all-tasks))
  (GET "/tasks/:id" [id] (api/view-task id))
  (GET "/tasks/add/:endpoint" [endpoint] (api/schedule-task (str endpoint " cron job") "* * * * *" endpoint))
  (GET "/tasks/remove/:id" [id] (api/unschedule-task id)))


(defroutes app-routes
  (c-route/resources "/")
  (c-route/not-found "404 Page not found."))

(def all-routes
  (c-core/routes site app-routes))

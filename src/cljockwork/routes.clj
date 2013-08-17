(ns cljockwork.routes
  (:require [compojure.core
             :as c-core
             :refer [defroutes GET POST PUT DELETE HEAD OPTIONS PATCH ANY]]
            [compojure.route :as c-route]
            ;; Public APIs
            [cljockwork.api :as api]))

(defroutes site
  (GET "/" [] (api/index))

  (POST "/stop" [] (api/stop))
  (POST "/start" [] (api/start))
  (GET "/status" [] (api/current-status))
  (GET "/config" [] (api/config))

  (GET "/tasks/" [] (api/list-all-tasks))
  (GET "/tasks/:id" [id] (api/view-task id))
  (POST "/tasks/validate" {body :body} (api/validate-task body))
  (PUT "/tasks/add" {body :body} (api/schedule-task body))
  (DELETE "/tasks/:id/remove" [id] (api/unschedule-task id))
  (POST "/tasks/:id/pause" [id] (api/pause-task id))
  (POST "/tasks/:id/activate" [id] (api/activate-task id))

  (GET "/events/" [] (api/recent-events)))

(defroutes app-routes
  (c-route/resources "/")
  (c-route/not-found "404 Page not found."))

(def all-routes
  (c-core/routes site app-routes))

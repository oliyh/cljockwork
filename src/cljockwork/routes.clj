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

  (GET "/tasks/" [] (api/list-all-tasks))
  (GET "/tasks/:id" [id] (api/view-task id))
  (POST "/tasks/validate" {body :body} (api/validate-task (:schedule body) (:endpoint body)))
  (PUT "/tasks/add" {body :body} (api/schedule-task (:desc body) (:schedule body) (:endpoint body)))
  (DELETE "/tasks/remove/:id" [id] (api/unschedule-task id)))

(defroutes app-routes
  (c-route/resources "/")
  (c-route/not-found "404 Page not found."))

(def all-routes
  (c-core/routes site app-routes))

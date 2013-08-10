(ns cljockwork.routes
  (:require [compojure.core
             :as c-core
             :refer [defroutes GET POST PUT DELETE HEAD OPTIONS PATCH ANY]]
            [compojure.route :as c-route]
            ;; Public APIs
            [cljockwork.api :as api]))

(defroutes site
  (GET "/" [] (api/index))
  (GET "/jobs/" [] (api/list-all-jobs))
  (GET "/jobs/:id" [id] (api/view-job id))
  (GET "/jobs/add/:endpoint" [endpoint] (api/schedule-task "* * * * *" endpoint)))

(defroutes app-routes
  (c-route/resources "/")
  (c-route/not-found "404 Page not found."))

(def all-routes
  (c-core/routes site app-routes))

(ns cljockwork.server
  (:require [cljockwork.ring :as handler]
            [ring.server.standalone :as ring-server]))

(defn start-server
  "used for starting the server in development mode from REPL, e.g. (def server (start-server))"
  ([] (start-server (Integer. (or (get (System/getenv) "PORT") 8080))))
  ([port]
     (let [server (ring-server/serve (handler/get-handler #'handler/app)
                                     {:port port
                                      :init handler/init
                                      :auto-reload? true
                                      :destroy handler/destroy
                                      :join true
                                      :open-browser? false})]
       (println (str "You can view the site at http://localhost:" port "/"))
       server)))

(defn stop-server [server]
  (when server
    (.stop server)
    server))

(defn restart-server [server]
  (when server
    (doto server
      (.stop)
      (.start))))

(defn -main [port]
  (let [server (start-server (Integer. port))]
    server))

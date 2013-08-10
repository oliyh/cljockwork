(ns cljockwork.api
  (:use [ring.util.response]))

(defn index []
  (response {:response "Hello World"}))

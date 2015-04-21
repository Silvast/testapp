(ns testapp.dbs
   (:require [monger.core :as mg])
   (:import [com.mongodb MongoOptions ServerAddress]))

;;(let [conn (mg/connect)
  ;;     db   (mg/get-db conn "monger-test")])

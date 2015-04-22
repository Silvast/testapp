(ns testapp.dbs
   (:require [monger.core :as mg]
     [monger.collection :as mc]
     [clj-time.core :as t]
     [clj-time.coerce :as c]
     [testapp.xmlstuff :as xmlstuff])
   (:import [com.mongodb MongoOptions ServerAddress]))

;; We are using same db for weather observations, so I guess we can define it at the beginning

(def conn1 (mg/connect "weatherobs"))
(def db (mg/get-db conn1 "weatherobs"))

;; when core.clj calls this function, it stores an observation to db if it is not there already
;; Each city has its own collection, so collections are "Tampere", "London" and "Durham NC"
;; Each collection has values :date :weather :wind and : temperature
;; In real life there are multiple observations per day though..


;; looks into db if an observation for that city at that date already exists
(defn getobs [date city]
 (mc/find-maps db city {:date (c/to-long date)})
 )

;; stores observations to our little database
(defn storeobs [date city observation]
  (mc/insert-and-return db city {:weather observation :date (c/to-long date)})
 )



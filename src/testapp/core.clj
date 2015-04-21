(ns testapp.core
  (:gen-class :main true)
  (:use noir.core)
  (:use hiccup.form)
  (:use clj-xpath.core)
  (:require [clojure.tools.cli :refer [cli]]
   	    [compojure.handler :as handler]
	    [compojure.route :as route]
   	    [compojure.core :refer [GET POST defroutes]]
   	    [noir.server :as server]
            [noir.response :as response]
            [hiccup.page :as page]
	    [clj-http.client :as http]
	    [monger.core :as mg :refer [server-address mongo-options]]
	    [monger.collection :as mc]
	    [monger.result :refer [ok? has-error?]]
	    [monger.conversion :refer [from-db-object]]
	    [monger util result]
	    [clojure.data.zip.xml :as zip-xml]
	    [clojure.zip :as zip]
	    [clojure.data.zip.xml :as zx]
            [clojure.data.xml :as xml]
            [testapp.xmlstuff :as xmlstuff]
	    )
  (:import
	   org.bson.types.ObjectId
   	   [com.mongodb MongoOptions])

(:import [org.bson.types ObjectId]
    [com.mongodb DB WriteConcern]))

;; Calls to do with api. Codes reside in xmlstuff

;;for testing 
(def apikeytest "c381dc34ce8ce50f")

(def apikey (atom {}))

;; First, get the country codes for our cities
(defn getcountry [city]
  (cond 
     (= city "Tampere") "Finland"
     (= city "London") "UK"
     (= city "Durham") "NC" ))

;; Give quick weather forecast on city 

(defn weather [city apikey]
  (xmlstuff/getweather (getcountry city) city apikeytest))

;; Stores weather observation to db
;; (defn storeweather [city]
  ;;(dbs/))

(defpage "/" []
  (page/html5
   [:head
    [:title "My weather App"]]
   [:section
   [:h2 "The weather now in Tampere"]
   [:p (str (weather "Tampere" apikey))]]))
    


;; Give key to wunderground api in commandline (I dont want to give mine to github) 

(defn -main [& args]
 
 ;; take argument that we got from commandline (user's api key) and swap it to our atom @apikey
 (swap! apikey conj {:apikey args})
;;that atom should now look like {:apikey "xxx"} where xxx is the apikey

 ;;these are just for testing
 (println @apikey)
 (println (str (first (:apikey @apikey))))
;;(println (xmlstuff/create-url "Finland" "Tampere" (str (first (:apikey @apikey))))) 
(println (weather "Tampere" apikey))
(server/start 8080))
   
  
  

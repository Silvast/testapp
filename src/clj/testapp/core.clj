(ns testapp.core
  (:gen-class :main true)
  (:use noir.core)
  (:use hiccup.form)
  (:use clj-xpath.core)
  (:use ring.util.response)
  (:require [clojure.tools.cli :refer [cli]]
   	    [clojure.java.io :as io]
;;	    [qbits.jet.server :refer [run-jetty]]
   	    [compojure.handler :as handler]
;;	    [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
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
	    [clj-time.core :as t]
	    [clj-time.coerce :as c]
            [testapp.xmlstuff :as xmlstuff]
	    [testapp.dbs :as dbs])
  (:import
	   org.bson.types.ObjectId
   	   [com.mongodb MongoOptions])

(:import [org.bson.types ObjectId]
    [com.mongodb DB WriteConcern]))

(defn handler [request]
 {:body "My weather!"})

(defn home
   [req]
   (render (io/resource "index.html") req))

(defroutes resources-routes
     (route/resources "/"))

(defroutes routes
   (GET "/" [] home)
   (GET "/index.html" [] (resource-response "index.html" {:root "/"}))
   ;;(GET "app.js" [] (resource-response "app.js" {:root "/public/js"}))
   ;;(route/resources "/public")
   (GET "/js.html" [] (resource-response "js.html" {:root "/public"}))
   (route/not-found "<h1>Page not found</h1>"))

(def app
    (compojure.core/routes
            resources-routes
	           routes))

;;(def app (-> routes 
;;	  (wrap-resource "resources/public"))

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

;; Get weather conditions either today or 1-10 days into the future
(defn weather [city apikey date]
 (if (= date (c/to-long (t/today)))
      (xmlstuff/getweathernow (getcountry city) city apikeytest)
      (xmlstuff/getweatherfuture (getcountry city) city apikeytest date)))

;; Asks, using function getobs, whether or not there already is an observation with this date & city combination
;; If there is not, it stores weather observation to db by calling another function, storeobs
(defn getconditions [date city apikey]
 (if (empty? (dbs/getobs date city))
    (dbs/storeobs date city (weather city apikey))
    (dbs/getobs date city)))

(defpage "/" []
  (page/html5
   [:head
    [:title "My weather App"]]
   [:section
   [:h2 "The weather now in Tampere"]
   [:p (str (weather "Tampere" apikey (c/to-long (t/today))))]]))


(defn -main [& args]
 ;; take argument that we got from commandline (user's api key) and swap it to our atom @apikey
(swap! apikey conj {:apikey args})
(println (weather "Tampere" apikey (c/to-long (t/today))))
(server/start 8080))
   
  
  

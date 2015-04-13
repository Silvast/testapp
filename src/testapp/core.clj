(ns testapp.core
  (:use noir.core)
  (:use hiccup.form)
  (:use clj-xpath.core)
  (:require [compojure.handler :as handler]
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
	    )
  (:import
	   org.bson.types.ObjectId
   	   [com.mongodb MongoOptions])

   (:import [org.bson.types ObjectId]
            [com.mongodb DB WriteConcern])
  )


(def base_url
;; "http://api.wunderground.com/api/#{api_key}/"
"http://api.wunderground.com/api/c381dc34ce8ce50f/geolookup/conditions/forecast/q"
 ) 

(defn- create-url
 "Create api url depending on which city you want to see"
 [country city]
 (str base_url "/" country "/" city ".xml"))

(defn api-call
 "Make an api call to wunderground API with country & city"
 [country city]
 (-> (create-url country city)
     (http/get {:headers {"Accept-Encoding" ""}} )
     :body
     ;;parse
  )
 )

(let [conn (mg/connect)
       db   (mg/get-db conn "monger-new")]
(println (mc/find-maps db "documents")))


(def posts (atom [{:title "foo" :content "bar"}
                  {:title "quux" :content "ref ref"}]))


 (defn add-dropdown []  
   [:section
   [:h2 "Choose a city"]
   [:p (drop-down "drop" ["Tampere" "London" "Durham NC"] )]
   (submit-button "Show weather")
   ])


(defpage "/" []
  (page/html5
   [:head
    [:title "My weather App"]]
   [:section
    (add-dropdown)]))

(defpage [:post "/"] {:keys [title content]}
  (swap! posts #(conj % {:title title :content content}))
  (response/redirect "/"))

(defn -main [& args]
  (println "> Start my app")
  (server/start 8080))

(ns testapp.core
  (:gen-class :main true)
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
    [com.mongodb DB WriteConcern])
  )

;; Calls to do with api. Codes reside in xmlstuff

;; First, get the country codes for our cities
(defn country [city]
  (cond 
     (= city "Tampere") "Finland"
     (= city "London") "UK"
     (= city "Durham") "NC" ))

;; Give wheather quick weather forecast on city 
(defn weather [city]
  (xmlstuff/getweather (country city) city)
   )

;; Things to do with mongodb, unfinished
(let [conn (mg/connect)
       db   (mg/get-db conn "monger-new")])

       ;;(println (mc/find-maps db "documents")))


 (defn add-dropdown []  
   [:section
   [:h1 "My Weather App"]
   [:h2 "Choose a city"]
   [:p (drop-down "drop" ["Tampere" "London" "Durham NC"] )]
   (submit-button "Show weather")
   ])

(defpage "/foo" []
 (page/html5
  [:section
  (str (weather "Tampere"))
  ])
 )

(defpage "/" []
  (page/html5
   [:head
    [:title "My weather App"]]
   [:section
   [:h2 "The weather now in Tampere"]
    [:p (str (weather "Tampere"))]
   [:h2 "And how about London?"]
   [:p (str (weather "London"))]
   [:h2 "Let's not forget Durham NC, the best of all Durhams!"]
   [:p (str (weather "Durham"))]
    ]
    ))

(defn foo2 []
(weather "Tampere")
;;"ansku" 
)

(defroutes home-routes
(GET "/foo2" [] (foo2))
(POST "/foo" []  (weather "Tampere"))
)

(def app
  (handler/site home-routes))

;;(defpage [:post "/"] {:keys [title content]}
 ;; (swap! posts #(conj % {:title title :content content}))
  ;;(response/redirect "/"))

(defn -main [& args]
  (println "> Start my app")
  (println "Let's see what the weather in Tampere is like:")
  (println (weather "Tampere"))
  (println "And how about London?")
  (println (weather "London"))
  (println "Let's not forget Durham NC, the best of all Durhams!")
  (println (weather "Durham"))
  (server/start 8080)
  )























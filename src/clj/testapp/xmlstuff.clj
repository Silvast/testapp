 (ns testapp.xmlstuff
     (:require
	 [clojure.zip :as zip]
	 [clojure.data.zip.xml :as zx] 
	 [clojure.data.xml :as xml])
  ;;	 [clojure.string :only (join split) :as str]
     )



 (defn base_url [apikey]
   (clojure.string/join "/" ["http://api.wunderground.com/api" apikey "geolookup/conditions/forecast/q" ]))

  (defn create-url
      "A helper function to create an api url depending on which city you want to see"
         [country city apikey]
	    (str (base_url apikey) "/" country "/" city ".xml"))


;; Get data from api
 (defn dataa [country city apikey]
   (xml/parse (java.io.StringReader. (slurp (create-url country city apikey)))))

;; Make zipper of the data and navigate to current_observation (I know this is stupid, but just couldnt figure out a better way)
 (defn currento [country city apikey] 
 		(-> (zip/xml-zip (dataa country city apikey)) zip/down zip/right zip/right zip/right zip/right zip/node))

 ;; This was my attempt to try to do the same for future values as I was able to do for current_observation. For some reason, this
 ;; doesn't quite work.
 (defn futuro [country city apikey]
                   (-> (zip/xml-zip (dataa country city apikey)) zip/down zip/right zip/right zip/right zip/right zip/right zip/node))

 ;; Get the weather info from current time (from API)

(defn getweathernow [country city apikey]
(let [currento-zip (zip/xml-zip (currento country city apikey))]
      (zip/node (zip/down
		 (zx/xml1-> currento-zip :weather)))))
 
 ;; If the observation that was asked is in the future, try to navigate to <forecast> --> <txt_forecast>
 ;;  --> <forecastdays> --> and then to each future days observation <period>1</period>
 ;; this doesn't do it properly though..

(defn getweatherfuture [country city apikey date]
    (let [futuro-zip (zip/xml-zip (futuro country city apikey))]
          (zip/node (zip/down
		    (zx/xml1-> futuro-zip :fcttext_metric)))))


;; This was the old version that worked. It only retrieved current observation 

 (defn getweather [country city apikey] 
  (let [currento-zip (zip/xml-zip (currento country city apikey))]
      (zip/node (zip/down
		      	(zx/xml1-> currento-zip :weather)))))



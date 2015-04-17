 (ns testapp.xmlstuff
     (:require
	 [clojure.zip :as zip]
	 [clojure.data.zip.xml :as zx] 
	 [clojure.data.xml :as xml])
  ;;	 [clojure.string :only (join split) :as str]
     )


 (defn base_url [apikey]
   (clojure.string/join "/" ["http://api.wunderground.com/api" apikey "geolookup/conditions/forecast/q" ])
  ;; "http://api.wunderground.com/api/apikey/geolookup/conditions/forecast/q"
    ;;"http://api.wunderground.com/api/c381dc34ce8ce50f/geolookup/conditions/forecast/q"
      )

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

;; For some reason this wont work if I dont re-zipper the currento data
;; Get the weather info from current time (from API)

 (defn getweather [country city apikey] 
  (let [currento-zip (zip/xml-zip (currento country city apikey))]
      (zip/node (zip/down
		      	(zx/xml1-> currento-zip :weather)))))



(defproject testapp "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
  		[org.clojure/clojurescript "0.0-3211"]
  		[org.clojure/tools.cli "0.3.1"]
  		[compojure "1.3.3" :exclusions [ring/ring-core]]
  		[noir "1.3.0-beta7"]
		[hiccup "1.0.5"]
		[com.novemberain/monger "2.0.1"]
		[org.clojure/data.xml "0.0.8"]
		[org.clojure/data.zip "0.1.1"]
		[clj-http "0.2.1"]
		[clj-time "0.8.0"]
		[com.github.kyleburton/clj-xpath "1.4.4"]
		;;[ring/ring-core "1.3.2" :exclusions [javax.servlet/servlet-api]]
	        ;;[ring/ring-servlet "1.3.2" :exclusions [javax.servlet/servlet-api]]
	        ;;[ring/ring-defaults "0.1.2" :exclusions [javax.servlet/servlet-api]]
		[ring "1.3.2"]
		[metosin/ring-http-response "0.6.1"]
	;;	[javax.servlet/servlet-api "3.0"]
		[ring/ring-jetty-adapter "1.3.2"]]
		;;[cc.qbits/jet "0.6.2"]]
 		
 :plugins [[lein-cljsbuild "1.0.4"]
  	    [lein-ring "0.9.3"]]
  :ring {:handler testapp.core/app}    
  :cljsbuild {:builds
            [{
              :source-paths ["src/cljs"]
              :compiler {
		         :output-to "resources/public/js/app.js"
                         :output-dir "resources/public/js/out"
                         :source-map true
                         :optimizations :none
			;; :main testapp.hello
                        ;; :asset-path "out/testapp"
                         :pretty-print true}}]}
  ;; :main ^:skip-aot testapp.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :source-paths ["src/clj"])

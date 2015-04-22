(defproject testapp "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
  		[org.clojure/tools.cli "0.3.1"]
  		[compojure "1.3.3"]
  		[noir "1.3.0-beta7"]
		[hiccup "1.0.5"]
		[com.novemberain/monger "2.0.1"]
		[org.clojure/data.xml "0.0.8"]
		[org.clojure/data.zip "0.1.1"]
		[clj-http "0.2.1"]
		[clj-time "0.8.0"]
		[com.github.kyleburton/clj-xpath "1.4.4"]]
  :main ^:skip-aot testapp.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})

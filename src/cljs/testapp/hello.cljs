(ns testapp.hello)

(defn ^:export main []
  (.write js/document "<p>Hello, ClojureScript compiler!</p>"))

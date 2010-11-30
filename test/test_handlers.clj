(ns test-handlers
  (:use anansi.handlers))

(def simple-handler
  (handle :get "/" request
          "simple"))

(defn test-simple-handler
  []
  (= (simple-handler {:request-method :get :uri "/"})
     {:status 200, :headers {"Content-Type" "text/html"}, :body "simple"}))

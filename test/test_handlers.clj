(ns test-handlers
  (:require [anansi.handlers :refer [handle]]
            [clojure.test :refer [deftest is]]))

(def simple-handler
  (handle :get "/" request
          "simple"))

(deftest test-simple-handler
  []
  (is (= (simple-handler {:request-method :get :uri "/"})
         {:status 200, :headers {"Content-Type" "text/html; charset=utf-8"}, :body "simple"})))

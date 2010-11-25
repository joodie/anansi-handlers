(ns anansi.handlers
  (:use clout.core
        compojure.response))

(defn- method-matches
  [method request]
  (let [request-method (request :request-method)
        form-method    (get-in request [:form-params "_method"])]
    (or (nil? method)
        (if (and form-method (= request-method :post))
          (= (.toUpperCase (name method)) form-method)
          (= method request-method)))))

(defn- assoc-route-params
  "Associate route parameters with the request map."
  [request params]
  (merge-with merge request {:route-params params,
                             :params (zipmap (map keyword (keys params)) (vals params))}))

(defn- prepare-route
  "Pre-compile the route."
  [route]
  (cond
    (string? route)
      (route-compile route)
    (vector? route)
      (route-compile
        (first route)
        (apply hash-map (rest route)))
    :else
      `(if (string? ~route)
         (route-compile ~route)
         ~route)))

(defmacro handle [method path binding & body]
  `(let [route# (#'prepare-route ~path)]
     (fn [request#]
       (if (#'method-matches ~method request#)
         (if-let [route-params# (route-matches route# request#)]
           (let [request# (#'assoc-route-params request# route-params#)]
             (let [~binding request#]
               (render request# (do ~@body)))))))))

(defmacro handlers [& more]
  (vec (map (fn [h] `(handle ~@h)) more)))

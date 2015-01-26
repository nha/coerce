(ns coerce.core
  "coerce"
    (:import (java.util Date)))

;; avoid name collision
(def DateTime java.util.Date)

(defn coerce-single
  [obj schema path]
  (let [type (get-in schema path)
        value (get-in obj path)]
    (if (instance? type value)
      obj
      {} )))



(defn coerce
  "
  Coerces a map from a JSON object
  Keys not in the schema definition are to be thrown away
  "
  [obj schema]
  (println "obj" obj "schema" schema)
  (if (empty? schema)
    {}
    (coerce-single obj schema [:name])))




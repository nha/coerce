(ns coerce.core)





(defn coerce-single
  [obj schema path]
  (println "coerce single obj" obj "schema" schema "path" path)
  (let [type (get-in schema path)
        value (get-in obj path)]
    (println "type" type "value" value)
    (println (instance? type value))
    (if (instance? type value)
      obj
      {} ;; TODO throw error
      )
    ))



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




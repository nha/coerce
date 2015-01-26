(ns coerce.core)

(defn coerce
  "
  Coerces a map from a JSON object
  Keys not in the schema definition are to be thrown away
  "
  [obj schema]
  (println "obj" obj "schema" schema)
  {})

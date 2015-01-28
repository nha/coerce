(ns coerce.core
  "coerce"
  (:require [clj-time.core :as t]
            [clj-time.format :as f]))


;; Straightforward conversion functions

(defn str->int [s]
  "Parse a string into an int."
  (println "to int")
  (Integer/parseInt s))

(defn str->double [s]
  "Parse a string into an double."
  (println "to double")
  (Double/parseDouble s))

(defn str->date [s]
  "parse a string using the basic clj-time formatter"
  (println "to date")
  (f/parse (f/formatters :basic-date-time) s))

(defn str->bool [s]
  "string to boolean conversion"
  (println "to bool")
  (case s
    "true" true
    "false" false
    ((throw (Exception. (str "Cannot convert to boolean: " s))))))


;; Use conversion functions

(defn coerce-single
  "convert a value into a desired type if possible"
  [desired-type value]

  (if (nil? value)
    (throw (Exception. "No value given for type" desired-type)))

  (if (instance? desired-type value)
    value
    (do (println "need to convert" value "to" desired-type)
      (condp = desired-type
        String (str value)
        Integer (str->int value)
        Double (str->double value)
        java.util.Date (str->date value)
        java.lang.Boolean (str->bool value)
        ; let it crash if not in the list
        ))))


;; reducing function
(defn coerce-kv
  "reducing function that builds the coerced object
   by merging/coercing each pair
   of key/value of the object into the schema"
  [acc current obj]
  (let [path (vec (first current))
        type (second current)
        value (get-in obj path)]
    (if (nil? type)
      {}
      (assoc-in acc path (coerce-single type value)))))


;; slightly modified from amalloy
;; http://stackoverflow.com/questions/21768802/how-can-i-get-the-nested-keys-of-a-map-in-clojure/21769786#21769786
;; the general pattern seems to be : (fn paths [node] (if (done? node) '(()) (for [choice (choices node), subpath (paths (make-choice node choice))] (cons choice path))))
(defn flatten-keys [m]
  "get a list of [path to a value, value] for every element of a map"
  (if (or (not (map? m))
        (empty? m))
    '(())
    (for [[k v] m
          subkey (flatten-keys v)]
      [(cons k subkey) v])))


;; Note : in a real situation (ie. not a test) I would probably have used prismatic/schema.
(defn coerce
  "Coerces a map from a JSON object
  Keys not in the schema definition are thrown away"
  [obj schema]
  (let [flat-schema (flatten-keys schema)]
    (reduce #(coerce-kv %1 %2 obj) schema flat-schema)))




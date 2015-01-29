(ns coerce.core
  "coerce"
  (:require [clj-time.core :as t]
            [clj-time.format :as f])
  (:import [org.joda.time DateTime]))


(declare coerce)

;; Conversion functions

(defn str->int [s]
  "Parse a string into an int."
  (Integer/parseInt s))

(defn str->double [s]
  "Parse a string into an double."
  (Double/parseDouble s))

(defn str->date [s]
  "parse a string using the basic clj-time formatter"
  ; in Chrome, new Date.toJSON() gives a :date-time format
  (f/parse (f/formatters :date-time) s))

(defn str->bool [s]
  "string to boolean conversion"
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

  (cond
    (vector? desired-type) (coerce (zipmap (range) desired-type) value)
    (instance? desired-type value) value
    :else (condp = desired-type
            String (str value)
            Integer (str->int value)
            Double (str->double value)
            DateTime (str->date value)
            java.lang.Boolean (str->bool value))))
            ; let it crash if not in the list
             


;; reducing function
(defn coerce-kv
  "reducing function that builds the coerced object
   by merging/coercing each pair
   of key/value of the object into the schema"
  [acc current-schema obj]

  (let [path (vec (first current-schema))
        type (second current-schema)
        value (get-in obj path)]

    (if (nil? type)
      acc
      (assoc-in acc path (coerce-single type value)))))


;; http://stackoverflow.com/questions/21768802/how-can-i-get-the-nested-keys-of-a-map-in-clojure/21769786#21769786
;; Amalloy : the general pattern is : (fn paths [node] (if (done? node) '(()) (for [choice (choices node), subpath (paths (make-choice node choice))] (cons choice path))))
(defn keys-in
  "get a list of [path to a value, value] for every element of a map"
  [m]
  (if (or (not (map? m))
        (empty? m))
    '(())
    (for [[k v] m
          subkey (keys-in v)]
      (cons k subkey))))


(defn get-kv [acc path m]
  "returns a single [path value]"
  (let [v (get-in m path)]
    (if-not (empty? path)
      (conj acc [path v])
      acc)))


(defn flat-path
  "get the a list of [path value] of a map"
  [m]
  (reduce #(get-kv %1 %2 m) '() (keys-in m)))


;; Note : in a real situation (ie. not a test)
;; I would probably have used prismatic/schema for this task.
(defn coerce
  "Coerces a map from a JSON object
  Keys not in the schema definition are thrown away"
  [obj schema]
  (let [flat-schema (flat-path schema)]
    (reduce #(coerce-kv %1 %2 obj) schema flat-schema)))




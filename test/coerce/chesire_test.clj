(ns coerce.chesire-test
  (:use midje.sweet)
  (:use [coerce.core])
  (:require [clj-time.core :as t]
            [clj-time.format :as f]
            [cheshire.core :refer :all])
  (:import [org.joda.time DateTime]))


(def schema {
              :a Integer
              :b Double
              :c String
              :d DateTime
              :e Boolean
              })

(def json {
              :a "123"
              :b "123"
              :c "hello"
              :d "2015-01-29T23:36:30.226Z"
              :e "true"
              })

(def expected {
            :a (int 123)
            :b (double 123)
            :c "hello"
            :d (t/date-time 2015 1 29 23 36 30 226)
            :e true
            })

(def jsonStr (generate-string  json))
(println jsonStr)

(facts "It works with Chesire JSON"

  (fact "JSON containing only String value can be coerced"
    (coerce json schema) => expected ))

(ns coerce.core-test
  (:use midje.sweet)
  (:use [coerce.core]))

(facts "about `coerce`"

  (fact "coerces empty elements correctly"
    (coerce {} {}) => {})

  (fact "Empty schemas always yield empty results"
    (coerce {} {}) => {}
    (coerce {:key "value"} {}) => {}
    (coerce {:oneKey "oneValue", :anotherKey "anotherValue"} {}) => {})

  (fact "coerces depth one maps for basic types"
    (coerce {:name "a string"} {:name String}) => {:name "a string"}))
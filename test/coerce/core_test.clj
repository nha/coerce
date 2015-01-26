(ns coerce.core-test
  (:use midje.sweet)
  (:use [coerce.core]))

(def date1 (java.util.Date.))
(def date2 (java.util.Date.))

(facts "about `coerce`"

  (fact "coerces empty elements correctly"
    (coerce {} {}) => {})

  (fact "Empty schemas always yield empty results"
    (coerce {} {}) => {}
    (coerce {:key "value"} {}) => {}
    (coerce {:oneKey "oneValue", :anotherKey "anotherValue"} {}) => {})

  (fact "coerces single key maps for basic types"
    (coerce {:name "Nicolas"} {:name String}) => {:name "Nicolas"}
    (coerce {:name 123} {:name Number}) => {:name 123}
    (coerce {:name true} {:name Boolean}) => {:name true}
    (coerce {:name date1} {:name DateTime}) => {:name date1})

  (fact "reject incorrect String"
    (coerce {:name 123} {:name String}) => {})

  (fact "reject incorrect Number"
    (coerce {:name "not a Number"} {:name Number}) => {})

  (fact "reject incorrect Boolean"
    (coerce {:name "not a Boolean"} {:name Boolean}) => {})

  (fact "reject incorrect Date"
    (coerce {:name "not a date"} {:name DateTime}) => {}))
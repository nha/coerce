(ns coerce.core-test
  (:use midje.sweet)
  (:use [coerce.core]))


(def Date java.util.Date)

(def date1 (java.util.Date.))
(def date2 (java.util.Date.))


(facts "about `flatten-keys`"

  (fact "returns the keys of the a map"
    (flatten-keys {}) => '(()))
  ;(flatten-keys {:a {:b {:c 1}}}) => ([(:a (:b (:c) 1) {:c 1}) {:b {:c 1}}]))
  )



(facts "about `coerce`"

 (fact "Empty schemas always yield empty results"
    (coerce {} {}) => {}
    (coerce {:key "value"} {}) => {}
    (coerce {:a "a", :b "b"} {}) => {}
    (coerce {:a "a", :b {:c "c"}} {}) => {})

  (fact "coerces single key maps for basic types"
    
    ;; String
    (coerce {:s "abc"} {:s String}) => {:s "abc"}
    (coerce {:s 123} {:s String}) => {:s "123"}
    (coerce {:s 12.3} {:s String}) => {:s "12.3"}
    
    ;; Boolean
    (coerce {:activated true} {:activated Boolean}) => {:activated true}
    (coerce {:activated "true"} {:activated Boolean}) => {:activated true}
    (coerce {:activated "false"} {:activated Boolean}) => {:activated false}
    
    ;; Numbers
    
    
    ;; DateTime
    
    
    )
    ;(coerce {:name date1} {:name DateTime}) => {:name date1})



;  (fact "reject incorrect Number"
;    (coerce {:name "not a Number"} {:name Number}) => {})

  (fact "reject incorrect Boolean"
    (coerce {:b "not a Boolean"} {:b Boolean}) => (throws Exception))

  (fact "reject incorrect Date"
    (coerce {:name "not a date" :key "there"} {:name Date :key String}) => (throws Exception)))



;(println "strok" (coerce-single String "this is ok"))
;(println "bool false"  (coerce-single Boolean "false"))
;(println "error"  (coerce-single Integer "not an int"))
;(println "123 "  (coerce-single Integer "456"))
;(println "doublo ok " (coerce-single Double "12.3"))
;(println "date" (coerce-single Date "2012-02-01"))
;(println "date ko" (coerce-single Date "not a date"))
; Number? would that be feasible ?
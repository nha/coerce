(ns coerce.core-test
  (:use midje.sweet)
  (:use [coerce.core])
  (:require [clj-time.core :as t]
            [clj-time.format :as f])
  (:import [org.joda.time DateTime]))


(def Date java.util.Date)

(def date1 (t/date-time 1986 10 14 4 3 27 456))
(def date2 (t/date-time 2015 9 12))
(def date3 (t/date-time 2015 1 29 23 36 30 226))



;; schema from specification
(def schema {
              :_id  Double
              :name String
              :url  String
              :date DateTime
              :meta {
                      :image-url String
                      :content   String
                      :extern {
                                :sample Boolean
                                :frame  Boolean
                                :id     Double
                                }}
              :order [{
                        :language String
                        :price    Double
                        }]
              :active Boolean
              :i18n [{
                       :name String
                       :description String
                       }]
              })
;; example valid data
(def valid {
             :_id  (double 123456)
             :name "Nicolas"
             :url  "nurl"
             :date (t/now)
             :meta {
                     :image-url "iurl"
                     :content   "some content"
                     :extern {
                               :sample false
                               :frame  false
                               :id     (double 123456)
                               }}
             :order [{
                       :language "FR"
                       :price    45.0
                       }]
             :active true
             :i18n [{
                      :name "i18"
                      :description "a desc"
                      }]
             })

;; example invalid data
(def invalid {
               :_id  (int 123456)
               :name "NotNicolas"
               :url  "nurl"
               :date "2015-01-29T23:36:30.226Z"
               :meta {
                       :image-url "iurl"
                       :content   "some content"
                       :extern {
                                 :sample false
                                 :frame  false
                                 :id     (double 123456)
                                 }}
               ;; missing order
               ;:order [{
               ;          :language "FR"
               ;          :price    45.0
               ;          }]
               :active true
               :i18n [{
                        :name "i18"
                        :description "a desc"
                        }]
               })



(facts "about `keys-in`"

  (fact "returns all the keys in a map needed to access all elements"
    (keys-in {}) => '(()))
    (keys-in {:a {:b 1}}) => '((:a :b)))



(facts "about `flat-path`"

  (fact "returns the list of [path value] of a map"
    (flat-path {}) => '()
    (flat-path {:a {:b {:c 1}}}) => '([(:a :b :c) 1])))



(facts "about `coerce`"

  
 (fact "Empty schemas always yield empty results"
    (coerce {} {}) => {}
    (coerce {:key "value"} {}) => {}
    (coerce {:a "a", :b "b"} {}) => {}
    (coerce {:a "a", :b {:c "c"}} {}) => {}
    (coerce {:a "a" :b "b"} {:a String}) => {:a "a"}
    (coerce {:a "a" :b "b" :c "c"} {:a String :b String}) => {:a "a" :b "b"})
  
  
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
    (coerce {:_id (int 123)} {:_id Integer}) => {:_id 123}
    (coerce {:_id "123"} {:_id Integer}) => {:_id 123}
    
    (coerce {:_id 123.456} {:_id Double}) => {:_id 123.456}
    (coerce {:_id "123.456"} {:_id Double}) => {:_id 123.456}

    ;; DateTime
    (coerce {:date date1} {:date DateTime}) => {:date date1}
    (coerce {:date "2015-01-29T23:36:30.226Z"} {:date DateTime}) => {:date date3})



  (fact "throws exception on incoercibles values"
    
    ;; Boolean
    (coerce {:b "not a Boolean"} {:b Boolean}) => (throws Exception)
    
    ;; Numbers
    (coerce {:name "not a Number"} {:name Number}) => (throws Exception)
    
    ;; DateTime
    (coerce {:name "not a date" :key "there"} {:name Date :key String}) => (throws Exception))


  (fact "keys missing in map are detected"
    (coerce {} {:a String}) => (throws Exception)
    (coerce {:b "a is missing"} {:a String, :b String}) => (throws Exception))
  
  
  (fact "works for maps with arrays"
    (coerce {:a [:b "b" :c 123]} {:a [:b String :c Integer]}) => {:a [:b "b" :c 123]})
  
  (fact "passes the spec"
    (coerce valid schema) => valid))


;; TODO test chesire
;; TODO: 
;; Numbers/Long
;;
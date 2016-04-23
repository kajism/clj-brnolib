(ns clj-brnolib.time-test
  (:require [clj-brnolib.time :as sut]
            #?(:clj [clojure.test :as t]
               :cljs [cljs.test :as t :include-macros true])))

(t/deftest to-format-test
  (t/is (thrown? #?(:cljs js/TypeError :clj ClassCastException) (sut/to-format "" sut/ddMMyyyy)))
  (t/is (= "" (sut/to-format nil sut/ddMMyyyy)))
  (t/is (= "22.11.2015" (sut/to-format #inst"2015-11-22" sut/ddMMyyyy)))
  (t/is (= "2.2.2012" (sut/to-format #inst"2012-02-02" sut/dMyyyy))))

(t/deftest from-format-test
  (t/is (nil? (sut/from-format "" sut/ddMMyyyy)))
  (t/is (nil? (sut/from-format " \t" sut/ddMMyyyy)))
  (t/is (thrown? #?(:cljs js/Error :clj Exception) (sut/from-format "abc" sut/ddMMyyyy)))
  (t/is (= #inst "2015-12-03" (sut/from-format "03.12.2015" sut/ddMMyyyy)))
  #_(t/is (= #inst "2015-12-03T04:05:33" (sut/from-format "3.12.2015 4:05:33" sut/dMyyyyHmmss))))

(t/deftest min->sec-test
  (t/is (nil? (sut/min->sec nil)))
  (t/is (= 120 (sut/min->sec 2))))

(t/deftest hour->sec-test
  (t/is (nil? (sut/hour->sec nil)))
  (t/is (= (* 120 60) (sut/hour->sec 2)))
  (t/is (= (* 90 60.0) (sut/hour->sec 1.5))))

(t/deftest hour->millis-test
  (t/is (nil? (sut/hour->millis nil)))
  (t/is (= (* 60 60 1000) (sut/hour->millis 1))))

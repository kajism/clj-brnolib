(ns clj-brnolib.time-test
  (:require [clj-brnolib.time :as ut]
            #?@(:cljs [[cljs.test :refer-macros [deftest is run-tests testing]]]
                :clj [[clojure.test :refer [deftest is run-tests testing]]])))

(deftest to-format-test
  (is (thrown? #?(:cljs js/TypeError :clj ClassCastException) (ut/to-format "" ut/ddMMyyyy)))
  (is (= "" (ut/to-format nil ut/ddMMyyyy)))
  (is (= "22.11.2015" (ut/to-format #inst"2015-11-22" ut/ddMMyyyy)))
  (is (= "2.2.2012" (ut/to-format #inst"2012-02-02" ut/dMyyyy))))

(deftest from-format-test
  (is (nil? (ut/from-format "" ut/ddMMyyyy)))
  (is (nil? (ut/from-format " \t" ut/ddMMyyyy)))
  (is (thrown? #?(:cljs js/Error :clj Exception) (ut/from-format "abc" ut/ddMMyyyy)))
  (is (= #inst "2015-12-03" (ut/from-format "03.12.2015" ut/ddMMyyyy)))
  #_(is (= #inst "2015-12-03T04:05:33" (ut/from-format "3.12.2015 4:05:33" ut/dMyyyyHmmss))))


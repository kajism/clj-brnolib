(ns clj-brnolib.cljs.util-test
  (:require [clj-brnolib.cljs.util :as ut]
            [cljs.test :refer-macros [deftest is run-tests testing]]
            [cognitect.transit :as transit]))

(deftest sort-by-locale-test
  (is (= ["ahoj" "Chovanec" "Czisárová" "Děda" "Malý" "mávat" "ťulík" "Žižka"] ;;nevím jak správně třídit CH...
         (ut/sort-by-locale identity ["Žižka" "mávat" "Chovanec" "ťulík" "Malý" "ahoj" "Czisárová" "Děda"]))))

(deftest remove-spaces-test
  (is (= "abc" (ut/remove-spaces "a b c")))
  (is (= "" (ut/remove-spaces nil))))

(deftest parse-int-test
  (is (= nil (ut/parse-int nil)))
  (is (= nil (ut/parse-int "a")))
  (is (= 10 (ut/parse-int "10")))
  (is (= 12 (ut/parse-int "12.34"))))

(deftest parse-float-test
  (is (= nil (ut/parse-float nil)))
  (is (= nil (ut/parse-float "a")))
  (is (= 10 (ut/parse-float "10")))
  (is (= 12.34 (ut/parse-float "12.34"))))

#_(deftest parse-bigdec
  (is (= nil (ut/parse-bigdec nil)))
  (is (= nil (ut/parse-bigdec "a")))
  (is (= (transit/bigdec "10") (ut/parse-bigdec "10")))
  (is (= (transit/bigdec "12.34") (ut/parse-bigdec "12.34"))))



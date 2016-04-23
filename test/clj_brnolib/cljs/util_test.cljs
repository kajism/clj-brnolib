(ns clj-brnolib.cljs.util-test
  (:require [clj-brnolib.cljs.util :as sut]
            [cljs.test :as t :include-macros true]
            [cognitect.transit :as transit]))

(t/deftest sort-by-locale-test
  (t/is (= ["ahoj" "Chovanec" "Czisárová" "Děda" "Malý" "mávat" "ťulík" "Žižka"] ;;nevím jak správně třídit CH...
         (sut/sort-by-locale identity ["Žižka" "mávat" "Chovanec" "ťulík" "Malý" "ahoj" "Czisárová" "Děda"]))))

(t/deftest remove-spaces-test
  (t/is (= "abc" (sut/remove-spaces "a b c")))
  (t/is (= "" (sut/remove-spaces nil))))

(t/deftest parse-int-test
  (t/is (= nil (sut/parse-int nil)))
  (t/is (= nil (sut/parse-int "a")))
  (t/is (= 10 (sut/parse-int "10")))
  (t/is (= 12 (sut/parse-int "12.34"))))

(t/deftest parse-float-test
  (t/is (= nil (sut/parse-float nil)))
  (t/is (= nil (sut/parse-float "a")))
  (t/is (= 10 (sut/parse-float "10")))
  (t/is (= 12.34 (sut/parse-float "12.34"))))

(t/deftest parse-bigdec-test
  (t/is (= nil (sut/parse-bigdec nil)))
  (t/is (= nil (sut/parse-bigdec "a")))
  (t/is (= (transit/bigdec "10") (sut/parse-bigdec "10")))
  (t/is (= (transit/bigdec "12.34") (sut/parse-bigdec "12.34"))))

(t/deftest boolean->text-test
  (t/is (= "Ne" (sut/boolean->text nil)))
  (t/is (= "Ne" (sut/boolean->text false)))
  (t/is (= "Ano" (sut/boolean->text true))))

(t/deftest money->text-text
  (t/is (= "" (sut/money->text nil)))
  (t/is (= "1 234" (sut/money->text 1234)))
  (t/is (= "1 234,56" (sut/money->text 1234.56)))
  (t/is (= "1 234 567,089" (sut/money->text 1234567.089))))

(t/deftest bigdec->str-test
  (t/is (nil? (sut/bigdec->str nil)))
  (t/is (= "1234.56" (sut/bigdec->str (transit/bigdec "1234.56")))))

(t/deftest bigdec->float-test
  (t/is (nil? (sut/bigdec->float nil)))
  (t/is (= 1234.56 (sut/bigdec->float (transit/bigdec "1234.56")))))

(t/deftest file-size->text-test
  (t/is (= "" (sut/file-size->text nil)))
  (t/is (= "-" (sut/file-size->text -1)))
  (t/is (= "0" (sut/file-size->text 0)))
  (t/is (= "24.0 B" (sut/file-size->text 24)))
  (t/is (= "1.0 kB" (sut/file-size->text 1024)))
  (t/is (= "1.0 MB" (sut/file-size->text (* 1024 1024)))))

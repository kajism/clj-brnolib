(ns clj-brnolib.validation-test
  (:require [clj-brnolib.validation :as sut]
            #?(:clj [clojure.test :as t]
               :cljs [cljs.test :as t :include-macros true])))

(t/deftest valid-name?-test
  (t/is (not (sut/valid-name? nil)))
  (t/is (not (sut/valid-name? "")))
  (t/is (not (sut/valid-name? "hello")))
  (t/is (not (sut/valid-name? "Hello World")))
  (t/is (sut/valid-name? "Hello"))
  (t/is (sut/valid-name? "Ťuňťa")))

(t/deftest valid-email?-test
  (t/is (not (sut/valid-email? nil)))
  (t/is (not (sut/valid-email? "")))
  (t/is (not (sut/valid-email? "hello")))
  (t/is (not (sut/valid-email? "hello@world")))
  (t/is (sut/valid-email? "hello@world.com"))
  (t/is (sut/valid-email? "hello.world@some.domain.cz")))

(t/deftest valid-phone?-test
  (t/is (not (sut/valid-phone? nil)))
  (t/is (not (sut/valid-phone? "")))
  (t/is (not (sut/valid-phone? "123")))
  (t/is (not (sut/valid-phone? "1234hello")))
  (t/is (sut/valid-phone? "123456789"))
  (t/is (sut/valid-phone? "123 456 789"))
  (t/is (sut/valid-phone? "+420123456789"))
  (t/is (sut/valid-phone? "+420 123 456 789"))
  (t/is (sut/valid-phone? "00420123456789"))
  (t/is (sut/valid-phone? "00 420 123 456 789")))

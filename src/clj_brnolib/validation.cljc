(ns clj-brnolib.validation
  (:require [clojure.string :as str]))

(defn validni-jmeno [jmeno] ;http://stackoverflow.com/questions/3617797/regex-to-match-only-letters
  (and (not (str/blank? jmeno))
       (re-matches #"^\p{L}+$" jmeno)))

(defn validni-email [email] ;http://stackoverflow.com/questions/33736473/how-to-validate-email-in-clojure
  (let [pattern #"[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?\.)+[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?"]
    (and (not (str/blank? email))
         (re-matches pattern email))))

(defn validni-telefon [telefon]
  (and (not (str/blank? telefon))
       (re-matches #"^\+?\d{9,14}$" telefon)))

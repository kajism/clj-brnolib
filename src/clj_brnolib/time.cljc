(ns clj-brnolib.time
  (:require #?@(:cljs [[cljs-time.coerce :as time-coerce]
                       [cljs-time.core :as time]
                       [cljs-time.format :as time-format]]
                :clj  [[clj-time.coerce :as time-coerce]
                       [clj-time.core :as time]
                       [clj-time.format :as time-format]])
            [clojure.string :as str]))

(defn to-date
  "Prevede z cljs.time date objektu do java.util.Date resp. js/Date"
  [date]
  (time-coerce/to-date date))

(defn from-date
  "Prevede z js Date do cljs.time date"
  [date]
  (time-coerce/from-date date))

(def formatter-ddMMyyyy (time-format/formatter "d.M.yyyy"))

(defn date-to-str [date]
  (if (nil? date)
    ""
    (time-format/unparse formatter-ddMMyyyy (from-date date))))

(defn str-to-date [str]
  (when-not (str/blank? str)
    (to-date (time-format/parse formatter-ddMMyyyy str))))

(def formatter-ddMMyyyyHHmm (time-format/formatter "d.M.yyyy H:mm:ss" #?(:clj (time/default-time-zone))))

(defn datetime-to-str [date]
  (if (nil? date)
    ""
    (->> date
         from-date
         #?(:cljs time/to-default-time-zone)
         (time-format/unparse formatter-ddMMyyyyHHmm))))

(defn str-to-datetime [str]
  (when-not (str/blank? str)
    (->> str
         (time-format/parse formatter-ddMMyyyyHHmm)
         #?(:cljs time/from-default-time-zone)
         to-date)))

;;(datetime-to-str (str-to-datetime "27.01.2016 15:16"))

(def formatter-HHmm (time-format/formatter "HH:mm" #?(:clj (time/default-time-zone))))

(defn time-to-str [date]
  (if (nil? date)
    ""
    (->> date
         from-date
         #?(:cljs time/to-default-time-zone)
         (time-format/unparse formatter-HHmm))))

(defn time-plus-hours [date n]
  (time/plus (from-date date) (time/hours n)))

(defn time-plus-hours-to-str [date n]
  (if (nil? date)
    ""
    (time-format/unparse formatter-HHmm (time-plus-hours date n))))

(defn min->sec [min]
  (* min 60))

(defn hour->sec [hour]
  (* hour (min->sec 60)))

(defn hour->millis [hour]
  (* (hour->sec hour) 1000))

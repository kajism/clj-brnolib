(ns clj-brnolib.jdbc-common
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.string :as str])
  (:import java.util.Date))

(defn esc
  "Prevadi (escapuje) nazvy DB tabulek a sloupcu z keywordu do stringu ohraniceneho uvozovkami.
   Diky tomu muzeme mit nazvy tabulek a sloupcu v Clojure tvaru s pomlckou.
   Umi prevadet samostatne keywordy, mapy s keywordovymi klici (rows) a taky keywordy v retezcich - sql dotazech,
   kde navic retezce umi spojujit, takze neni potreba pouzit (str)"
  ([to-esc]
   (cond
     (keyword? to-esc)
     (format "\"%s\"" (name to-esc))
     (map? to-esc)
     (->> to-esc
          (map (fn [[k v]]
                 [(esc k) v]))
          (into {}))
     :default
     (esc to-esc "")))
  ([s & ss]
   (str/replace (str s (apply str ss)) #":([a-z0-9\-]+)" "\"$1\"")))

(defn where
  ([where-m]
   (where "" where-m))
  ([prefix where-m]
   (let [prefix (if (str/blank? prefix) "" (str prefix "."))]
     (->> where-m
          (map (fn [[k v]] (str "AND " prefix (esc k)
                                (if (coll? v)
                                  (str " IN (" (str/join "," (take (count v) (repeat "?"))) ")")
                                  " = ?"))))
          (into [" WHERE 1=1"])
          (str/join " ")))))

(defn select
  [db-spec table-kw where-m]
  (jdbc/query db-spec (into [(str "SELECT * FROM " (esc table-kw) (where where-m))]
                            (flatten (vals where-m)))))

(defn- h2-inserted-id [insert-result]
  (-> insert-result
      first
      vals
      first))

(defn insert! [db-spec table-kw row]
  (let [row (assoc row :created (java.util.Date.))]
    (->
     (jdbc/insert! db-spec
                   (esc table-kw)
                   (esc row))
     h2-inserted-id)))

(defn update! [db-spec table-kw row]
  (jdbc/update! db-spec
                (esc table-kw)
                (esc row)
                ["\"id\" = ?" (:id row)])
  (:id row))

(defn save! [db-spec table-kw row]
  (let [row (assoc row :modified (java.util.Date.))
        id (if (:id row)
             (update! db-spec table-kw row)
             (insert! db-spec table-kw row))]
    (first (select db-spec table-kw {:id id}))))

(defn delete! [db-spec table-kw id]
  (jdbc/delete! db-spec (esc table-kw) ["\"id\" = ?" id]))

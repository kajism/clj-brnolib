(ns clj-brnolib.datomic-common
  (:require [datomic.api :as d]
            [taoensso.timbre :as timbre]))

(defn transact [conn uid tx]
  (timbre/info "transacting uid:" uid "tx:" (vec tx))
  @(d/transact conn (conj tx {:db/id (d/tempid :db.part/tx)
                              :tx/uid uid})))

(defn add-where [query where]
  (let [where (into {} (map-indexed
                        (fn [idx [k v]]
                          [k (symbol (str "?v" idx))])
                        where))]
    (-> query
        (update :where #(into % (->> where
                                     (filter (fn [[k v]] (not= k :db/id)))
                                     (map (fn [[k v]]
                                            ['?e k v])))))
        (update :in #(into % (map (fn [[k v]] (if (= k :db/id) '?e v)) where))))))

(defn select [db ent-type where]
  (apply d/q (add-where '{:find [[(pull ?e [*]) ...]]
                          :in [$ ?type]
                          :where [[?e :ent/type ?type]]}
                        where)
         db
         ent-type
         (vals where)))

(defn save! [conn uid ent-type ent]
  (let [id (:db/id ent)
        ent (cond-> ent
              true
              (assoc :ent/type ent-type)
              (not id)
              (assoc :db/id (d/tempid :db.part/user)))
        tx-result (transact conn uid [ent])]
    (if id
      id
      (d/resolve-tempid (:db-after tx-result) (:tempids tx-result) (:db/id ent)))))

(defn select-created-date [db id attr]
  (ffirst (d/q '[:find ?created
                 :in $ ?e ?attr
                 :where
                 [?e ?attr _ ?tx]
                 [?tx :db/txInstant ?created]]
               db
               id
               attr)))


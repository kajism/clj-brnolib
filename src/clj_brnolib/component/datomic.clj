(ns clj-brnolib.component.datomic
  (:require [com.stuartsierra.component :as component]
            [datomic.api :as d]))

(defrecord Datomic [uri conn]
  component/Lifecycle
  (start [component]
    (let [db (d/create-database uri)
          conn (d/connect uri)]
      (assoc component :conn conn)))
  (stop [component]
    (assoc component :conn nil)))

(defn datomic [uri]
  (map->Datomic {:uri uri}))
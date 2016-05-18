(ns clj-brnolib.component.sente
  (:require [com.stuartsierra.component :as component]
            [compojure.core :refer [GET POST routes]]
            [taoensso.sente :as sente]
            [taoensso.timbre :as timbre]))

#_(defn sente-routes [{{ring-ajax-post :ring-ajax-post
                      ring-ajax-get-or-ws-handshake :ring-ajax-get-or-ws-handshake} :sente}]
  (routes
   (GET  "/chsk" req (ring-ajax-get-or-ws-handshake req))
   (POST "/chsk" req (ring-ajax-post                req))))

(defmulti event-msg-handler (fn [db-spec {id :id}] id))

(defmethod event-msg-handler :default
  [db-spec {:keys [event id ?data ring-req ?reply-fn send-fn] :as ev-msg}]
  (timbre/debugf "Unhandled event: %s" event)
  {:umatched-event-as-echoed-from-from-server event})

(defmethod event-msg-handler :user/auth
  [db-spec {:keys [event id ?data ring-req ?reply-fn send-fn] :as ev-msg}]
  (get-in ring-req [:session :user]))

(defn make-event-msg-handler [db-spec]
  (fn [{:keys [event id ?data ring-req ?reply-fn send-fn] :as ev-msg}]
    (timbre/debugf "Handling event: %s" event)
    (let [result (event-msg-handler db-spec ev-msg)]
      (when ?reply-fn
        (?reply-fn result)))))

(defrecord ChannelSocketServer
           [db ring-ajax-post ring-ajax-get-or-ws-handshake ch-chsk chsk-send! connected-uids router web-server-adapter handler options]
  component/Lifecycle
  (start [component]
    (let [{:keys [ch-recv send-fn ajax-post-fn ajax-get-or-ws-handshake-fn connected-uids]}
          (sente/make-channel-socket-server! web-server-adapter options)]
      (assoc component
             :ring-ajax-post ajax-post-fn
             :ring-ajax-get-or-ws-handshake ajax-get-or-ws-handshake-fn
             :ch-chsk ch-recv
             :chsk-send! send-fn
             :connected-uids connected-uids
             :router (atom (sente/start-chsk-router! ch-recv (make-event-msg-handler (:spec db)))))))
  (stop [component]
    (if-let [stop-f (and router @router)]
      (assoc component :router (stop-f))
      component)))

(defn new-channel-socket-server
  ([web-server-adapter]
   (new-channel-socket-server nil web-server-adapter {}))
  ([event-msg-handler web-server-adapter]
   (new-channel-socket-server event-msg-handler web-server-adapter {}))
  ([event-msg-handler web-server-adapter options]
   (map->ChannelSocketServer {:web-server-adapter web-server-adapter
                              :handler event-msg-handler
                              :options options})))


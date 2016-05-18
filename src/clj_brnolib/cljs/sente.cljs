(ns clj-brnolib.cljs.sente
  (:require [re-frame.core :as re-frame]
            [taoensso.sente :as sente]
            [taoensso.timbre :as timbre]))

(let [;; Serializtion format, must use same val for client + server:
      packer :edn ; Default packer, a good choice in most cases
      ;; (sente-transit/get-flexi-packer :edn) ; Experimental, needs Transit dep

      {:keys [chsk ch-recv send-fn state]}
      (sente/make-channel-socket-client!
       "/chsk" {:type :auto
                :packer packer})]

  (def chsk       chsk)
  (def ch-chsk    ch-recv) ; ChannelSocket's receive channel
  (def chsk-send! send-fn) ; ChannelSocket's send API fn
  (def chsk-state state)   ; Watchable, read-only atom
  )

;;;; Sente event handlers

(defmulti -event-msg-handler
  "Multimethod to handle Sente `event-msg`s"
  :id ; Dispatch on event-id
  )

(defn event-msg-handler
  "Wraps `-event-msg-handler` with logging, error catching, etc."
  [{:as ev-msg :keys [id ?data event]}]
  (-event-msg-handler ev-msg))

(defmethod -event-msg-handler
  :default ; Default/fallback case (no other matching handler)
  [{:as ev-msg :keys [event]}]
  (timbre/debugf "Unhandled event: %s" event))

(defmethod -event-msg-handler :chsk/state
  [{:as ev-msg :keys [?data]}]
  (if (:first-open? ?data)
    (do
      (timbre/debugf "Channel socket successfully established!")
      (re-frame/dispatch [:init-app]))
    (timbre/debugf "Channel socket state change: %s" ?data)))

(defmethod -event-msg-handler :chsk/recv
  [{:as ev-msg :keys [?data]}]
  (timbre/debugf "Push event from server: %s" ?data))

(defmethod -event-msg-handler :chsk/handshake
  [{:as ev-msg :keys [?data]}]
  (let [[?uid ?csrf-token ?handshake-data] ?data]
    (timbre/debugf "Handshake: %s" ?data)))

;; TODO Add your (defmethod -event-msg-handler <event-id> [ev-msg] <body>)s here...

;;;; Sente event router (our `event-msg-handler` loop)

(defonce router_ (atom nil))
(defn  stop-router! [] (when-let [stop-f @router_] (stop-f)))
(defn start-router! []
  (stop-router!)
  (reset! router_
          (sente/start-client-chsk-router! ch-chsk event-msg-handler)))

(defn server-call
  ([req-msg resp-msg]
   (server-call req-msg resp-msg nil))
  ([req-msg resp-msg rollback-db]
   (chsk-send! req-msg
               5000
               (fn [reply]
                 (if (sente/cb-success? reply)
                   (when resp-msg (re-frame/dispatch (conj resp-msg reply)))
                   (re-frame/dispatch [:set-msg :error
                                       (or (str reply)
                                           "Server je nedostupný")
                                       rollback-db]))))
   nil))

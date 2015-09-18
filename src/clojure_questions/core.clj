
(ns clojure-questions.core
  (:require ;[clojure-questions.shortener :refer [shorten]]
            [clojure.string :as str]
            [twitter.oauth :as to]
            [twitter.api.restful :as tr]
            [clj-http.client :as http]
            [http.async.client :as ac]
            [cheshire.core :as json]
            [net.cgrand.enlive-html :as html] [environ.core :refer [env]]))

(def biostars-url "https://www.biostars.org/?sort=creation&limit=today")

(def creds (to/make-oauth-creds (env :clojureqs-app-consumer-key)
                                (env :clojureqs-app-consumer-secret)
                                (env :clojureqs-user-access-token)
                                (env :clojureqs-user-access-token-secret)))


(defn fetch-url [url]
  (html/html-resource (java.net.URL. url)))

(def selector [:div.post-title :a])

(defn raw-questions [] 
  (html/select (fetch-url biostars-url) selector))

(defn title-url [e]
  (let [base-url "biostars.org"]
    {:title (:content e) :link (str base-url (:href (:attrs e)))}))

(def questions (comp (partial map title-url) raw-questions))

(defn get-last-tweet-content []
  (->> (tr/statuses-user-timeline :oauth-creds creds
                                  :params {:count 20})
       :body first :text))

(defn filter-used [qs]
  (let [used (into [] (map #(first (butlast (str/split %  #" "))) [(get-last-tweet-content)]))]
    (doall (remove #(contains? used (:title %)) qs))))

(defn send-tweet []
  (let [msg (str (first (:title t)) \space (:link t))]
    (println "Tweeting: " msg)
    (tr/statuses-update :oauth-creds creds
                        :params {:status msg})))
(def new-questions (comp filter-used questions))

(defn doseq-sleep
    [f coll interval]
    (doseq [x coll]
          (f x)
          (Thread/sleep interval)))

(defn -main [& _]
  (doseq-sleep                 ;ten minutes
    send-tweet (new-questions) (* 10 60 1000)))

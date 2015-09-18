(ns clojure-questions.core
  (:require ;[clojure-questions.shortener :refer [shorten]]
            [clojure.string :as str]
            [clj-http.client :as http]
            [net.cgrand.enlive-html :as html] [environ.core :refer [env]]))

(defn make-tag [url] (->> url 
  (re-find #"(?:.*\.)?([^\.]+)\.(?:com|net|org)")
  second
  (str \#)))

(def websites {
  :biostars ["http://www.biostars.org/?sort=creation&limit=today" [:div.post-title :a] "biostars.org"]
  :seqanswers ["http://seqanswers.com/forums/search.php?searchid=2814437" [:.alt1 :div :a] "seqanswers.com/forums/"]
})

(def get-nodes (comp html/html-snippet :body http/get)) 

(defn extract-node [url n]
  (let [{title :content {href :href} :attrs} n]
    {:title (str/join title) :link (str url href)})) ;ug-o
    ;[title href]))

(defn get-questions [url selector base-url ] 
  (map (partial extract-node base-url)
       (html/select (get-nodes url) selector)))

(defn make-tweet [{:keys [title link]}]
  (let [hashtag (make-tag link)
       ;shorten url
       msg (str/join \space [title link hashtag])]
       msg))

(defn send-tweet [msg]
    (println "Tweeting: " msg)
    (tr/statuses-update :oauth-creds creds
                        :params {:status msg}))

(def tweet (comp send-tweet make-tweet) )

;;i.e.
;(apply get-questions (:seqanswers websites)) 
;(apply get-questions (:biostars websites)) 
;(map tweet (apply get-questions (:seqanswers websites)) )

(defn run 
  (let [site (:seqanswers websites)]
  ;filter questions
  (doseq-sleep                 ;ten minutes
   tweet (apply site get-questions) (* 10 60 1000)))) 

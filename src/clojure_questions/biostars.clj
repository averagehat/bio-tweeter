(def url https://www.biostars.org/?sort=creation&limit=today)


(let [doc (slurp url)]

(j
(def selector [:div.post-title :a ])

 "https://falkor-api.herokuapp.com/api/query?url=https://www.biostars.org/?sort=creation&limit=today&query=div.post-title%20a"

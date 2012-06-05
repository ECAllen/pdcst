(ns pdcst.core
  (:require 
      [clojure.zip :as zip]
      [clojure.xml :as xml]
      [clojure.data.zip :as zf]
      [clojure.data.zip.xml :as zxml]
      [clojure.string :as string]
      [clj-http.client :as client]))

(import '(java.io File))


(def subscriptions "/home/ethan/projects/pdcst/src/pdcst/subs.conf")

(defn get-urls []
  (string/split-lines (slurp subscriptions)))

; (def url "http://reason.tv/podcast/index.xml")
(def url "index.xml") 

(defn get-items [url]
  (for [x (xml-seq (xml/parse url))
        :when (= :item (:tag x))]
    (:content x)))

(defn get-pdcst-info [item]
  (let [[{title :tag [title-txt] :content} 
       {enclosure :tag {url :url} :attrs } 
       {desc :tag [desc-txt] :content}] item ] 
          into {title title-txt enclosure url desc desc-txt}))

(def pdcst-info (map get-pdcst-info (get-items url)))

; file structure 
; ~/.pdcst
; ~/.pdcst/pdcst.conf
; ~/.pdcst/downloads
; ~/.pdcst/downloads/<pdcst name>

(defn create-dir [dirname]
  (let [f (File. dirname)]
    (.mkdir f)))

(defn create-conf-dir [] 
  (let [conf-dir (str (System/getProperty "user.home") "/.pdcst")]
    (create-dir conf-dir)))

(defn create-download-dir [] 
  (let [conf-dir (str (System/getProperty "user.home") "/.pdcst/download")]
    (create-dir conf-dir)))

(defn kind [filename]
  (let [f (File. filename)]
    (cond
      (.isFile f)      "file"
      (.isDirectory f) "directory"
      (.exists f)      "other" 
      :else            "non-existent")))

(defn check-dir [dir]
  (let [f (kind dir)]
    (cond
      (= f "file") "is file create dir handle this?"
      (= f "non-existent") (create-conf-dir) 
      :else "ok")))



(ns pdcst.core
  (:require [clojure.data.zip :as zip])
  (:require [clojure.data.zip.xml :as zf])
  (:require [clojure.xml :as xml]))

(defn walker [zipper]
 (loop [zipper loc]
  (if (zip/end? loc)
   (zip/node loc)
   (prnln (zip/node))
  (recur (zip/next loc)))))

(ns doublets.solver
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]
            [clojure.set :as set]))

(def words (-> "words.edn"
               (io/resource)
               (slurp)
               (read-string)))

(def pairs
  "Create a sequence of vectors containing adjacent words"
  (let [diff-of-one?  #(= 1 (count (filter (fn [[c1 c2]] (not= c1 c2)) (map vector %1 %2))))]
      (for [words-of-a-size (vals (group-by count (map seq words)))
            w1 words-of-a-size
            w2 words-of-a-size
            :when (diff-of-one? w1 w2)]
        [(apply str w1) (apply str w2)])))

; Here I could have just loaded the pairs into a graph lib like Loom
; and use a built-in breadth first search, but let's cook our own.

(def adjacent-words
  "Create a map where all the words are keys leading to an empty set then load the vector
   pairs into it."
  (reduce (fn [m [k v]] (update m k conj v)) (into {} (map #(vector % #{}) words)) pairs))

(defn generate-chains [chains]
  "Lazily generate a sequence of vectors (chains) containing adjacent words.
   It proceeds like a breadth first search, generating the shortest chains first."
  (when-let [chain (first chains)]
    (let [extensions (set/difference (adjacent-words (last chain)) (set chain))
          extended-chains (map #(conj chain %) extensions)]
      (lazy-seq (cons chain (generate-chains (concat (rest chains) extended-chains)))))))

(defn doublets [word1 word2]
  "Find the first (and thus shortest) chain of adjacent words starts with word1 and ends with word2."
  (or (first (filter #(= word2 (last %)) (generate-chains [[word1]]))) []))

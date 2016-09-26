(ns fox-goose-bag-of-corn.puzzle
  (:require [clojure.set :as set]))

(def start-pos [[#{:fox :goose :corn :you} #{:boat} #{}]])
(def end-pos [#{} #{:boat} #{:fox :goose :corn :you}])

(defn move [state key]
  (let [[l m r] state
        piece (if key #{:you key} #{:you})]
    (cond
      (set/subset? piece l) (list [(set/difference l piece) (set/union m piece) r] [(set/difference l piece) m (set/union r piece)])
      (set/subset? piece r) (list [l (set/union m piece) (set/difference r piece)] [(set/union l piece) m (set/difference r piece)]))))

(defn valid? [[l _ r]]
  (and (not= l #{:fox :goose :corn})
       (not= l #{:fox :goose})
       (not= l #{:goose :corn})
       (not= r #{:fox :goose})
       (not= r #{:goose :corn})))

(defn explore [state]
  (let [last (peek state)]
    (if (= last end-pos)
      state
      (first (sequence
               (comp
                 (map (partial move last))
                 (filter (fn [[_ sec]] (and (some? sec) (valid? sec) (not-any? #(= sec %) state))))
                 (map (fn [[f s]] (explore (conj (conj state f) s))))
                 (filter some?))
               [:fox :goose :corn nil])))))

(defn river-crossing-plan [] (explore start-pos))

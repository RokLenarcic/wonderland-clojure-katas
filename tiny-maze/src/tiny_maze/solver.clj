(ns tiny-maze.solver)

(defn pos [maze v]
  (first (for [[row-idx row] (map-indexed vector maze)
               [col-idx val] (map-indexed vector row)
               :when (= val v)]
           [row-idx col-idx])))

(defn neighbour-coord [maze [row col]]
  (->> (list [(inc row) col] [(dec row) col] [row (inc col)] [row (dec col)])
       (filter #(or (= 0 (get-in maze %)) (= :E (get-in maze %))))))

(defn expand [maze points]
  (if-let [p (first points)]
    (let [neighs (neighbour-coord maze p)]
      (expand (reduce #(assoc-in %1 %2 p) maze neighs) (concat (next points) neighs)))
    maze))

(def m [[:S 0 1]
        [1  0 1]
        [1  0 :E]])

(defn solve-maze [maze]
  (let [solved (expand maze [(pos maze :S)])]
    (reduce #(assoc-in %1 %2 :x) maze (take-while vector? (iterate #(get-in solved %) (pos maze :E))))))

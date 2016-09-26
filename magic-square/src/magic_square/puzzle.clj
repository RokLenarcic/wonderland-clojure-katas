(ns magic-square.puzzle)

(def values [1.0 1.5 2.0 2.5 3.0 3.5 4.0 4.5 5.0])

(defn permutations [coll]
  (if (= 1 (count coll))
    (list coll)
    (for [idx (range (count coll))
          :let [after-drop (drop idx coll)]
          permutation (permutations (concat (take idx coll) (rest after-drop)))]
      (cons (first after-drop) permutation))))

(defn magic-square [values]
  (let [dim (int (Math/sqrt (count values)))
        index-sum-fn (fn [idxs] #(reduce + (map % idxs)))
        row-sum-fns (map index-sum-fn (partition dim (range (count values))))
        col-sum-fns (map index-sum-fn (partition dim (for [col (range dim) row (range dim)] (+ col (* dim row)))))
        diag1-sum-fn (index-sum-fn (map #(* % (inc dim)) (range dim)))
        diag2-sum-fn (index-sum-fn (map #(- (* (inc %) dim) (inc %)) (range dim)))]
    (->> (permutations values)
         (map vec)
         (filter #(apply = ((apply juxt (concat row-sum-fns col-sum-fns [diag1-sum-fn diag2-sum-fn])) %)))
         (first)
         (partition dim)
         (into [] (map vec)))))

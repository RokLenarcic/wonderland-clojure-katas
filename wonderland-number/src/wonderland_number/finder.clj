(ns wonderland-number.finder)

(defn wonderland-number []
  (first (for [candidate (range 100000 166667)              ; 6 * 166666 is the last 6 digit number
               :let [sorted-vals (map (comp sort str #(* candidate %)) (range 1 7))]
               :when (apply = sorted-vals)]
           candidate)))
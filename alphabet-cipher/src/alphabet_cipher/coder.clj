(ns alphabet-cipher.coder)

(defn charno [c] (- (int c) (int \a)))

(defn mod26alpha [op c1 c2] (char (+ (mod (op (charno c1) (charno c2)) 26) (int \a))))

(defn encode [keyword message] (apply str (map #(mod26alpha + %1 %2) (cycle keyword) message)))

(defn decode [keyword message] (apply str (map #(mod26alpha - %2 %1) (cycle keyword) message)))

(defn decipher [cipher message]
  (let [keystream (apply str (map #(mod26alpha - %1 %2) cipher message))]
    (first (filter #(= message (decode % cipher)) (map #(subs keystream 0 %) (range 1 (count keystream)))))))

(ns card-game-war.game
  (:import (java.util List)))

;; feel free to use these cards or use your own data structure
(def ^List suits [:spade :club :diamond :heart])
(def ^List ranks [2 3 4 5 6 7 8 9 10 :jack :queen :king :ace])

(defn card-power [[suit rank]] (+ (* 4 (.indexOf ranks rank)) (.indexOf suits suit)))

(def cards
  (for [suit suits
        rank ranks]
    [suit rank]))

(defn shuffle-and-cut []
  (map vec (partition 26 (shuffle cards))))

(defn play-round [player1-card player2-card]
  (if (> (card-power player1-card) (card-power player2-card))
    [[player1-card player2-card] []]
    [[] [player2-card player1-card]]))

(defn play-game [player1-cards player2-cards]
  (lazy-seq
    (cons [player1-cards player2-cards]
          (let [player1-card (first player1-cards)
                player2-card (first player2-cards)
                round-winnings (and player1-card player2-card (play-round player1-card player2-card))]
            (when round-winnings
              (play-game
                (into (subvec player1-cards 1) (round-winnings 0))
                (into (subvec player2-cards 1) (round-winnings 1))))))))

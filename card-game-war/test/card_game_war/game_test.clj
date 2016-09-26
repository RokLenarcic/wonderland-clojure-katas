(ns card-game-war.game-test
  (:require [clojure.test :refer :all]
            [card-game-war.game :refer :all]))

(deftest test-power-function
  (testing "suit power calculation"
    (is (= 3 (card-power [:heart 2])))
    (is (= 0 (card-power [:spade 2])))
    (is (= 48 (card-power [:spade :ace])))
    (is (= 47 (card-power [:heart :king]))))
  (testing "comparative ranks"
    (is (= 1 (- (card-power [:spade 3]) (card-power [:heart 2]))))
    (is (= 3 (- (card-power [:heart 3]) (card-power [:spade 3]))))))

;; fill in  tests for your game
(deftest test-play-round
  (testing "the higher rank wins"
    (is (= [[[:heart 3] [:heart 2]] []]) (play-round [:heart 3] [:heart 2]))
    (is (= [[[:heart 3] [:spade 2]] []]) (play-round [:heart 3] [:spade 2]))
    (is (not= [[] [[:heart 3] [:spade :ace]]] (play-round [:heart 3] [:spade :ace])))
    (is (not= [[] [[:heart :king] [:spade :ace]]] (play-round [:heart :king] [:spade :ace]))))
  (testing "the higher suit wins when same rank"
    (is (not= [[] [[:heart 2] [:spade 3]]] (play-round [:heart 2] [:spade 3])))
    (is (= [[[:heart 3] [:spade 3]] []]) (play-round [:heart 3] [:spade 3]))
    (is (= [[[:heart :ace] [:spade :ace]] []]) (play-round [:heart :ace] [:spade :ace])))
  (testing "queens are higher rank than jacks"
    (is (= [[[:heart :queen] [:spade :jack]] []] (play-round [:heart :queen] [:spade :jack]))))
  (testing "kings are higher rank than queens"
    (is (= [[[:heart :king] [:spade :queen]] []] (play-round [:heart :king] [:spade :queen]))))
  (testing "aces are higher rank than kings"
    (is (= [[[:heart :ace] [:spade :king]] []] (play-round [:heart :ace] [:spade :king]))))
  (testing "if the ranks are equal, clubs beat spades"
    (is (= [[[:club :ace] [:spade :ace]] []] (play-round [:club :ace] [:spade :ace]))))
  (testing "if the ranks are equal, diamonds beat clubs"
    (is (= [[[:club :diamonds] [:spade :clubs]] []] (play-round [:club :diamonds] [:spade :clubs]))))
  (testing "if the ranks are equal, hearts beat diamonds"
    (is (= [[[:club :hearts] [:spade :diamonds]] []] (play-round [:club :hearts] [:spade :diamonds])))))

(deftest test-play-game
  (let [card-comparator #(< (card-power %1) (card-power %2))
        [player1-cards player2-cards] (shuffle-and-cut)
        [player1-last-state player2-last-state] (last (play-game player1-cards player2-cards))]
    (testing "the player loses when they run out of cards"
      (is (or (empty? player1-last-state) (empty? player2-last-state))))
    (let [winner (if (empty? player1-last-state) player2-last-state player1-last-state)]
      (testing "the player who wins has all the cards"
        (is (= (sort card-comparator winner) (sort card-comparator winner))))
      (testing "the correct player wins"
        (is (some #(= [:heart :ace] %) winner)))))
  (let [[player1-cards player2-cards] [[[:heart 4][:spade :ace][:club 2]] [[:diamond :queen][:heart 9][:club :jack]]]
        results (play-game player1-cards player2-cards)]
    (testing "the game runs the correct course"
      (is (= [[player1-cards
               player2-cards]
              [[[:spade :ace] [:club 2]]
               [[:heart 9] [:club :jack] [:diamond :queen] [:heart 4]]]
              [[[:club 2] [:spade :ace] [:heart 9]]
               [[:club :jack] [:diamond :queen] [:heart 4]]]
              [[[:spade :ace] [:heart 9]]
               [[:diamond :queen] [:heart 4] [:club :jack] [:club 2]]]
              [[[:heart 9] [:spade :ace] [:diamond :queen]]
               [[:heart 4] [:club :jack] [:club 2]]]
              [[[:spade :ace] [:diamond :queen] [:heart 9] [:heart 4]]
               [[:club :jack] [:club 2]]]
              [[[:diamond :queen] [:heart 9] [:heart 4] [:spade :ace] [:club :jack]]
               [[:club 2]]]
              [[[:heart 9] [:heart 4] [:spade :ace] [:club :jack] [:diamond :queen] [:club 2]]
               []]]
             results)))))


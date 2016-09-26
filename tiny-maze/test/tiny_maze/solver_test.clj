(ns tiny-maze.solver-test
  (:require [clojure.test :refer :all]
            [tiny-maze.solver :refer :all]))

(deftest test-start-pos
  (testing "can find start position")
   (let [maze [[:S 0 1]
               [1  0 1]
               [1  0 :E]]
         maze2 [[0 1 :S]]]
     (is (= [0 0] (pos maze :S)))
     (is (= [0 2] (pos maze2 :S)))))

(deftest test-solve-maze
  (testing "can find way to exit with 3x3 maze"
    (let [maze [[:S 0 1]
                [1  0 1]
                [1  0 :E]]
          sol [[:x :x 1]
               [1  :x 1]
               [1  :x :x]]]
      (is (= sol (solve-maze maze)))))

    (testing "can find way to exit with 4x4 maze"
    (let [maze [[:S 0 0 1]
                [1  1 0 0]
                [1  0  0 1]
                [1  1  0 :E]]
          sol [[:x :x :x 1]
                [1  1 :x 0]
                [1  0 :x 1]
                [1  1  :x :x]]]
     (is (= sol (solve-maze maze))))))

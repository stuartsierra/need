;;; Assertions to test the 'need' macro.

(ns one)
(need/need [clojure.set union intersection])
(assert (= #'clojure.set/union (resolve 'union)))
(assert (= #'clojure.set/intersection (resolve 'intersection)))
(assert (nil? (resolve 'difference)))

(ns two)
(need/need [clojure.set :rename {union un, intersection in}])
(assert (= #'clojure.set/union (resolve 'un)))
(assert (= #'clojure.set/intersection (resolve 'in)))
(assert (nil? (resolve 'union)))
(assert (nil? (resolve 'intersection)))

(ns three)
(need/need [clojure.set :all])
(assert (= #'clojure.set/union (resolve 'union)))
(assert (= #'clojure.set/intersection (resolve 'intersection)))
(assert (= #'clojure.set/difference (resolve 'difference)))

(ns four)
(need/need [clojure.set :exclude [union]])
(assert (nil? (resolve 'union)))
(assert (= #'clojure.set/intersection (resolve 'intersection)))
(assert (= #'clojure.set/difference (resolve 'difference)))

(ns five)
(need/need [clojure.set :all :rename {union un, intersection in}])
(assert (= #'clojure.set/union (resolve 'un)))
(assert (= #'clojure.set/intersection (resolve 'in)))
(assert (= #'clojure.set/difference (resolve 'difference)))
(assert (nil? (resolve 'union)))
(assert (nil? (resolve 'intersection)))

(ns six)
(need/need [clojure.set :as set])
(assert (= #'clojure.set/union (resolve 'set/union)))
(assert (= #'clojure.set/intersection (resolve 'set/intersection)))
(assert (nil? (resolve 'union)))
(assert (nil? (resolve 'intersection)))

(println "All assertions passed.")

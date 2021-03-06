(ns chessground.chess
  "Immutable board data. Does not implement chess rules"
  (:require [chessground.common :refer [pp]]
            [chessground.fen :as forsyth]))

; Representation of a chess game:
; {:pieces {"a1" {:color "white" :role "rook"}
;           "b1" {:color "white" :role "knight"}
;          }}

(def colors ["white" "black"])
(def roles ["pawn" "rook" "knight" "bishop" "queen" "king"])

(def clear {:pieces {}})

(defn make [fen]
  {:pieces (forsyth/parse (when (not= fen "start") fen))})

(defn get-piece [chess key] (get-in chess [:pieces key]))

(defn get-pieces [chess] (:pieces chess))

(defn remove-piece [chess key] (update-in chess [:pieces] dissoc key))

(defn put-piece [chess key piece] (assoc-in chess [:pieces key] piece))

(defn set-pieces [chess changes]
  (update-in chess [:pieces]
             (fn [pieces] (reduce (fn [ps [key p]]
                                    (if p (assoc ps key p) (dissoc ps key)))
                                  pieces changes))))

(defn- count-pieces [chess] (count (:pieces chess)))

(defn move-piece [chess orig dest]
  "Return nil if orig and dest make no sense"
  (when (not= orig dest)
    (when-let [piece (get-piece chess orig)]
      (-> chess
          (remove-piece orig)
          (put-piece dest piece)))))

(defn owner-color [chess key]
  "Returns the color of the piece on this square key, or nil"
  (:color (get-piece chess key)))

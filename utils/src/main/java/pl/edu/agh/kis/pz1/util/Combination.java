package pl.edu.agh.kis.pz1.util;

public enum Combination {
    ROYAL_FLUSH,     // A,K,Q,J,10 in the same suit
    STRAIGHT_FLUSH,  // sequence in the same suit
    FOUR_OF_THE_KIND,
    FULL_HOUSE,      // 3 *(Rank1) + 2*(Rank2)
    FLUSH,          // 5 cards of the same suit
    STRAIGHT,       // sequence of cards in random suits
    THREE_OF_THE_KIND,
    TWO_PAIRS,
    ONE_PAIR,
    NO_PAIR
}

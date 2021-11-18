package pl.edu.agh.kis.pz1.util;

public enum Combination {
    RoyalFlush,     // A,K,Q,J,10 in the same suit
    StraightFlush,  // sequence in the same suit
    FourOfTheKind,
    FullHouse,      // 3 *(Rank1) + 2*(Rank2)
    Flush,          // 5 cards of the same suit
    Straight,       // sequence of cards in random suits
    ThreeOfTheKind,
    TwoPairs,
    OnePair,
    NoPair
}

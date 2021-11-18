package pl.edu.agh.kis.pz1.util;

import java.util.Objects;

public class Card {
    public Suit suit = Suit.club;
    public Rank rank = Rank._2;

    public Card() {}

    public Rank getRank() {
        return rank;
    }
    public Suit getSuit() {
        return suit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return suit == card.suit && rank == card.rank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(suit, rank);
    }

    public Card(Suit s, Rank r) {
        suit = s;
        rank = r;
    }

    public void displayCard() {
        System.out.print("(" + suit + ", " + rank + ") ");
    }
    public String getString() {
        return "(" + String.valueOf(suit) + "," + String.valueOf(rank)+") ";
    }
}
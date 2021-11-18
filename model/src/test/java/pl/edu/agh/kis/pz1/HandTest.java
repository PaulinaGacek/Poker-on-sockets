package pl.edu.agh.kis.pz1;

import org.junit.Test;
import static junit.framework.TestCase.*;
import static junit.framework.TestCase.assertEquals;

import pl.edu.agh.kis.pz1.util.Card;
import pl.edu.agh.kis.pz1.util.Combination;
import pl.edu.agh.kis.pz1.util.Rank;
import pl.edu.agh.kis.pz1.util.Suit;

import java.util.ArrayList;
import java.util.Arrays;

public class HandTest {
    Combination combinationFlush = Combination.Flush;
    Combination combinationFullHouse = Combination.FullHouse;
    Combination combinationFourOfKind = Combination.FourOfTheKind;
    Card heart2 = new Card(Suit.heart, Rank._2);
    Card heart3 = new Card(Suit.heart, Rank._3);
    Card heartQ = new Card(Suit.heart, Rank.Q);
    Card heartA = new Card(Suit.heart, Rank.A);
    Card heartK = new Card(Suit.heart, Rank.K);
    Card clubQ = new Card(Suit.club, Rank.Q);
    Card clubA = new Card(Suit.club, Rank.A);
    Card spadeA = new Card(Suit.spade, Rank.A);

    @Test
    public void mapHandToPointsTest(){
        Hand hand = new Hand();
        assertEquals(hand.mapHandToPoints(combinationFlush, heartQ), 102);
        assertEquals(hand.mapHandToPoints(combinationFlush, heartA), 104);
        assertEquals(hand.mapHandToPoints(combinationFullHouse, heartQ), 117);
        assertEquals(hand.mapHandToPoints(combinationFullHouse, heart2), 107);
        assertEquals(hand.mapHandToPoints(combinationFourOfKind, heart2), 122);
    }

    @Test
    public void findCombinationTest(){
        Hand hand = new Hand();
        // Full
        ArrayList<Card> cardsFullHand = new ArrayList<>(
                Arrays.asList(spadeA, heartQ, heartA, clubA, clubQ)
        );
        assertEquals(hand.findCombinationInCards(cardsFullHand), Combination.FullHouse);

        // Two pairs
        ArrayList<Card> cardsTwoPairs = new ArrayList<>(
                Arrays.asList(spadeA, heartQ, heart2, clubA, clubQ)
        );
        assertEquals(hand.findCombinationInCards(cardsTwoPairs), Combination.TwoPairs);

        // Three
        ArrayList<Card> cardsThreeOfTheKind= new ArrayList<>(
                Arrays.asList(spadeA, heartQ, heartA, clubA, heart2)
        );
        assertEquals(hand.findCombinationInCards(cardsThreeOfTheKind), Combination.ThreeOfTheKind);

        // Flush
        ArrayList<Card> cardsFlush= new ArrayList<>(
                Arrays.asList(heart3, heartQ, heartA, heartK, heart2)
        );
        assertEquals(hand.findCombinationInCards(cardsFlush), Combination.Flush);
    }
}

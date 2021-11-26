package pl.edu.agh.kis.pz1;

import org.testng.annotations.Test;
import static junit.framework.Assert.*;

import pl.edu.agh.kis.pz1.util.Card;
import pl.edu.agh.kis.pz1.util.Combination;
import pl.edu.agh.kis.pz1.util.Rank;
import pl.edu.agh.kis.pz1.util.Suit;

import java.util.ArrayList;
import java.util.Arrays;

// 97% coverage

public class HandTest {
    Combination combinationFlush = Combination.Flush;
    Combination combinationFullHouse = Combination.FullHouse;
    Combination combinationFourOfKind = Combination.FourOfTheKind;
    Card heart2 = new Card(Suit.heart, Rank._2);
    Card heart3 = new Card(Suit.heart, Rank._3);
    Card heart9 = new Card(Suit.heart, Rank._9);
    Card heart10 = new Card(Suit.heart, Rank._10);
    Card heartJ = new Card(Suit.heart, Rank.J);
    Card heartQ = new Card(Suit.heart, Rank.Q);
    Card heartK = new Card(Suit.heart, Rank.K);
    Card heartA = new Card(Suit.heart, Rank.A);
    Card diamondA = new Card(Suit.diamond, Rank.A);
    Card clubQ = new Card(Suit.club, Rank.Q);
    Card clubA = new Card(Suit.club, Rank.A);
    Card spadeA = new Card(Suit.spade, Rank.A);

    @Test
    public void mapHandToPointsTest(){
        Hand hand = new Hand();
        assertEquals(hand.mapHandToPoints(combinationFlush, heartQ.getRank()), 102);
        assertEquals(hand.mapHandToPoints(combinationFlush, heartA.getRank()), 104);
        assertEquals(hand.mapHandToPoints(combinationFullHouse, heartQ.getRank()), 117);
        assertEquals(hand.mapHandToPoints(combinationFullHouse, heart2.getRank()), 107);
        assertEquals(hand.mapHandToPoints(combinationFourOfKind, heart2.getRank()), 122);
    }

    @Test
    public void findCombinationTest(){
        Hand hand = new Hand();
        // Royal Flush
        ArrayList<Card> cardsRoyalFlush = new ArrayList<>(
                Arrays.asList(heartJ, heartQ, heartA, heart10, heartK)
        );
        assertEquals(hand.findCombinationInCards(cardsRoyalFlush), Combination.RoyalFlush);
        // Straight flush
        ArrayList<Card> cardsStraightFlush = new ArrayList<>(
                Arrays.asList(heartJ, heartQ, heart9, heart10, heartK)
        );
        assertEquals(hand.findCombinationInCards(cardsStraightFlush), Combination.StraightFlush);
        // Four of the kind
        ArrayList<Card> cards4ofTheKind = new ArrayList<>(
                Arrays.asList(heartA, diamondA, spadeA, clubA, heartK)
        );
        assertEquals(hand.findCombinationInCards(cards4ofTheKind), Combination.FourOfTheKind);

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

        // One pair
        ArrayList<Card> cardsOnePair = new ArrayList<>(
                Arrays.asList(spadeA, heart3, heart2, clubA, clubQ)
        );
        assertEquals(hand.findCombinationInCards(cardsOnePair), Combination.OnePair);

        // No pair
        ArrayList<Card> cardsNoPair = new ArrayList<>(
                Arrays.asList(heartK, heart3, heart2, clubA, clubQ)
        );
        assertEquals(hand.findCombinationInCards(cardsNoPair), Combination.NoPair);

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

    @Test
    public void whoWinsTest(){
        Hand hand = new Hand();
        ArrayList<Card> cardsRoyalFlush = new ArrayList<>(
                Arrays.asList(heartJ, heartQ, heartA, heart10, heartK)
        );
        ArrayList<Card> cardsFullHand = new ArrayList<>(
                Arrays.asList(spadeA, heartQ, heartA, clubA, clubQ)
        );
        ArrayList<Card> cardsFlush= new ArrayList<>(
                Arrays.asList(heart3, heartQ, heartA, heartK, heart2)
        );
        ArrayList<Card> cardsThreeOfTheKind= new ArrayList<>(
                Arrays.asList(spadeA, heartQ, heartA, clubA, heart2)
        );
        ArrayList<Card> cardsTwoPairs = new ArrayList<>(
                Arrays.asList(spadeA, heartQ, heart2, clubA, clubQ)
        );

        ArrayList<ArrayList<Card>> flush_rFlush_2pairs = new ArrayList<>(
                Arrays.asList(cardsFlush, cardsRoyalFlush, cardsTwoPairs)
        );
        ArrayList<ArrayList<Card>> flush_3ofKind_2pairs = new ArrayList<>(
                Arrays.asList(cardsFlush, cardsThreeOfTheKind, cardsTwoPairs)
        );
        ArrayList<ArrayList<Card>> full_flush_3ofKind_2pairs = new ArrayList<>(
                Arrays.asList(cardsFullHand,cardsFlush, cardsThreeOfTheKind, cardsTwoPairs)
        );

        assertEquals(1, hand.indexWhoWins(flush_rFlush_2pairs));
        assertEquals(0, hand.indexWhoWins(flush_3ofKind_2pairs));
        assertEquals(0, hand.indexWhoWins(full_flush_3ofKind_2pairs));
    }

    @Test
    public void isOnlyOneWinnerTest(){
        Hand hand = new Hand();
        ArrayList<Card> cardsRoyalFlush = new ArrayList<>(
                Arrays.asList(heartJ, heartQ, heartA, heart10, heartK)
        );
        ArrayList<Card> cardsFullHand = new ArrayList<>(
                Arrays.asList(spadeA, heartQ, heartA, clubA, clubQ)
        );
        ArrayList<Card> cardsFlush= new ArrayList<>(
                Arrays.asList(heart3, heartQ, heartA, heartK, heart2)
        );

        ArrayList<ArrayList<Card>> royalFlushx2_flush =  new ArrayList<>(
                Arrays.asList(cardsFlush, cardsRoyalFlush, cardsRoyalFlush)
        );
        ArrayList<ArrayList<Card>> full_flush_royalFlush = new ArrayList<>(
                Arrays.asList(cardsFullHand,cardsFlush, cardsRoyalFlush)
        );
        assertFalse(hand.isOnlyOneWinner(royalFlushx2_flush));
        assertTrue(hand.isOnlyOneWinner(full_flush_royalFlush));
    }

    @Test
    public void indexSecondWinner(){
        Hand hand = new Hand();
        ArrayList<Card> cardsRoyalFlush = new ArrayList<>(
                Arrays.asList(heartJ, heartQ, heartA, heart10, heartK)
        );
        ArrayList<Card> cardsFullHand = new ArrayList<>(
                Arrays.asList(spadeA, heartQ, heartA, clubA, clubQ)
        );
        ArrayList<Card> cardsFlush= new ArrayList<>(
                Arrays.asList(heart3, heartQ, heartA, heartK, heart2)
        );

        ArrayList<ArrayList<Card>> royalFlushx2_flush =  new ArrayList<>(
                Arrays.asList(cardsFlush, cardsRoyalFlush, cardsRoyalFlush)
        );
        assertEquals(hand.indexSecondWinner(royalFlushx2_flush),2);
    }
}

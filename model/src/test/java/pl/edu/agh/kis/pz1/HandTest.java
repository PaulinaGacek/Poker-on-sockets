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
    Combination combinationFlush = Combination.FLUSH;
    Combination combinationFullHouse = Combination.FULL_HOUSE;
    Combination combinationFourOfKind = Combination.FOUR_OF_THE_KIND;
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
    Card spade2 = new Card(Suit.spade, Rank._2);
    Card club2 = new Card(Suit.club, Rank._2);
    Card diamond2 = new Card(Suit.diamond, Rank._2);

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
        assertEquals(hand.findCombinationInCards(cardsRoyalFlush), Combination.ROYAL_FLUSH);
        // Straight flush
        ArrayList<Card> cardsStraightFlush = new ArrayList<>(
                Arrays.asList(heartJ, heartQ, heart9, heart10, heartK)
        );
        assertEquals(hand.findCombinationInCards(cardsStraightFlush), Combination.STRAIGHT_FLUSH);
        // Four of the kind
        ArrayList<Card> cards4ofTheKind = new ArrayList<>(
                Arrays.asList(heartA, diamondA, spadeA, clubA, heartK)
        );
        assertEquals(hand.findCombinationInCards(cards4ofTheKind), Combination.FOUR_OF_THE_KIND);

        // Full
        ArrayList<Card> cardsFullHand = new ArrayList<>(
                Arrays.asList(spadeA, heartQ, heartA, clubA, clubQ)
        );
        assertEquals(hand.findCombinationInCards(cardsFullHand), Combination.FULL_HOUSE);

        // Two pairs
        ArrayList<Card> cardsTwoPairs = new ArrayList<>(
                Arrays.asList(spadeA, heartQ, heart2, clubA, clubQ)
        );
        assertEquals(hand.findCombinationInCards(cardsTwoPairs), Combination.TWO_PAIRS);

        // One pair
        ArrayList<Card> cardsOnePair = new ArrayList<>(
                Arrays.asList(spadeA, heart3, heart2, clubA, clubQ)
        );
        assertEquals(hand.findCombinationInCards(cardsOnePair), Combination.ONE_PAIR);

        // No pair
        ArrayList<Card> cardsNoPair = new ArrayList<>(
                Arrays.asList(heartK, heart3, heart2, clubA, clubQ)
        );
        assertEquals(hand.findCombinationInCards(cardsNoPair), Combination.NO_PAIR);

        // Three
        ArrayList<Card> cardsThreeOfTheKind= new ArrayList<>(
                Arrays.asList(spadeA, heartQ, heartA, clubA, heart2)
        );
        assertEquals(hand.findCombinationInCards(cardsThreeOfTheKind), Combination.THREE_OF_THE_KIND);

        // Flush
        ArrayList<Card> cardsFlush= new ArrayList<>(
                Arrays.asList(heart3, heartQ, heartA, heartK, heart2)
        );
        assertEquals(hand.findCombinationInCards(cardsFlush), Combination.FLUSH);
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

    @Test
    public void getHighestRankInCombinationTest(){
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
        ArrayList<Card> cardsOnePair = new ArrayList<>(
                Arrays.asList(spadeA, heart3, heart2, spade2, clubQ)
        );
        ArrayList<Card> cards4ofTheKind = new ArrayList<>(
                Arrays.asList(heart2, diamond2, spade2, club2, heartK)
        );
        assertEquals(hand.getHighestRankInCombination(cardsRoyalFlush), Rank.A);
        assertEquals(hand.getHighestRankInCombination(cardsFullHand), Rank.A);
        assertEquals(hand.getHighestRankInCombination(cardsFlush), Rank.A);
        assertEquals(hand.getHighestRankInCombination(cardsThreeOfTheKind), Rank.A);
        assertEquals(hand.getHighestRankInCombination(cardsTwoPairs), Rank.A);
        assertEquals(hand.getHighestRankInCombination(cards4ofTheKind), Rank._2);
        assertEquals(hand.getHighestRankInCombination(cardsOnePair), Rank._2);

    }
}

package pl.edu.agh.kis.pz1;
import pl.edu.agh.kis.pz1.util.Card;
import pl.edu.agh.kis.pz1.util.Deck;
import pl.edu.agh.kis.pz1.util.Rank;
import pl.edu.agh.kis.pz1.util.Suit;

import java.util.ArrayList;

import static junit.framework.Assert.*;
import org.testng.annotations.Test;

public class PlayerTest {
    public Player player1 = new Player("a");
    public static ArrayList<Card> get5CardDeck(){
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new Card(Suit.heart, Rank._2));
        cards.add(new Card(Suit.club, Rank._3));
        cards.add(new Card(Suit.club, Rank._4));
        cards.add(new Card(Suit.club, Rank._5));
        cards.add(new Card(Suit.club, Rank._6));
        return cards;
    }
    @Test
    public void payTest(){
        int pool = player1.getPool();
        assertTrue(pool > 0);
        int amountToPay = pool/10;
        player1.pay(amountToPay);
        assertEquals(pool-amountToPay, player1.getPool());
        assertTrue(player1.pay(amountToPay));
        assertEquals(pool-2*amountToPay, player1.getPool());
        int currentPool = player1.getPool();
        assertFalse(player1.pay(pool));
        assertEquals(currentPool,player1.getPool());
    }

    @Test
    public void addCardTest(){
        ArrayList<Card> cards = get5CardDeck();
        assertEquals(player1.getCards().size(), 0);
        for(int i = 0; i < 5; ++i){
            player1.addCard(cards.get(i));
            assertEquals(player1.getCards().size(), i+1);
        }
        player1.addCard(cards.get(0));
        assertEquals(player1.getCards().size(), 5);
    }

    @Test
    public void displayCardsTestDeckIsEmpty(){
        Player newPlayer = new Player("a");
        assertEquals(newPlayer.displayCards(), "You do not have any cards yet!");
    }

    @Test
    public void displayCardsTestNotEmptyDeck(){
        Player newPlayer = new Player("a");
        newPlayer.addCard(get5CardDeck().get(0));
        assertEquals(newPlayer.displayCards(),"(heart,_2) ");
        newPlayer.addCard(get5CardDeck().get(1));
        assertEquals(newPlayer.displayCards(),"(heart,_2) (club,_3) ");
    }

    @Test
    public void swapCardsTestImproperIndex(){
        //Deck sortedDeck = new Deck();
        //assertFalse(player1.swapCard(-1,sortedDeck));
        //assertFalse(player1.swapCard(player1.getCards().size(),sortedDeck));
    }

    @Test
    public void swapCardsTest(){
        Player newPlayer = new Player("a");
        newPlayer.addCard(new Card(Suit.heart, Rank.J));
        newPlayer.addCard(new Card(Suit.spade, Rank._2));
        newPlayer.addCard(new Card(Suit.club, Rank._2));
        newPlayer.addCard(new Card(Suit.club, Rank.Q));
        newPlayer.addCard(new Card(Suit.club, Rank.A));
        Card givenCard = new Card(Suit.heart, Rank.Q);
        Deck sortedDeck = new Deck();
        Card topCard = sortedDeck.getCard(0);
        ArrayList<Card> cardsBeforeSwap = newPlayer.getCards();
        assertTrue(newPlayer.swapCard(0,sortedDeck));
        for(int i = 1; i < cardsBeforeSwap.size(); ++i){
            assertEquals(newPlayer.getCards().get(i), cardsBeforeSwap.get(i));
        }
        //assertTrue(topCard.equals(newPlayer.getCards().get(0)));
    }
}

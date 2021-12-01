package pl.edu.agh.kis.pz1.util;
import static junit.framework.Assert.*;
import org.testng.annotations.Test;

// 100% coverage
public class DeckTest {

    @Test
    public void constructorsTest(){
        Deck sortedDeck = new Deck();
        Deck shuffledDeck = new Deck(false);
        assertEquals(sortedDeck.getSize(),shuffledDeck.getSize());
    }

    @Test
    public void shuffleTest(){
        Deck sortedDeck = new Deck();
        Deck shuffledDeck = new Deck(false);
        int sortedCards = 0;
        for(int i = 0; i < sortedDeck.getSize(); ++i){
            if(sortedDeck.getCard(i)==shuffledDeck.getCard(0)){
                sortedCards += 1;
            }
        }
        assertTrue(sortedCards < 10);
    }

    @Test
    public void getCardsTest(){
        Deck sortedDeck = new Deck();
        Deck shuffledDeck = new Deck(false);
        Card card = sortedDeck.getCard(0);
        assertEquals(sortedDeck.getSize(), shuffledDeck.getSize());
        assertEquals(card, new Card(Suit.club, Rank._2));
    }

    @Test
    public void dealOutCardTest(){
        Deck sortedDeck = new Deck();
        sortedDeck.displayDeck();
        Deck shuffledDeck = new Deck(false);
        Card card = sortedDeck.dealOutCard();
        assertEquals(sortedDeck.getSize(), 51);
        assertEquals(card, new Card(Suit.club, Rank._2));

    }

    @Test
    public void dealOutNewDeckTest(){
        Deck sortedDeck = new Deck();
        sortedDeck.dealOutNewDeck();
        assertEquals(sortedDeck.getSize(), 52);
    }
}

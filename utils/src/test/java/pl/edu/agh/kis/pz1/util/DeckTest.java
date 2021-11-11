package pl.edu.agh.kis.pz1.util;
import static junit.framework.Assert.*;
import org.testng.annotations.Test;
public class DeckTest {
    Deck sortedDeck = new Deck();
    Deck shuffledDeck = new Deck(false);

    @Test
    public void constructorsTest(){
        assertEquals(sortedDeck.getSize(),shuffledDeck.getSize());
    }

    @Test
    public void shuffleTest(){
        int unshuffledCards = 0;
        for(int i = 0; i < sortedDeck.getSize(); ++i){
            if(sortedDeck.getCard(i)==shuffledDeck.getCard(0)){
                unshuffledCards += 1;
            }
        }
        assertTrue(unshuffledCards < 10);
    }
}

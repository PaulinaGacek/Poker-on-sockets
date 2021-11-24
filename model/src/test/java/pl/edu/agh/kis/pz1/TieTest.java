package pl.edu.agh.kis.pz1;

import static junit.framework.Assert.*;
import static junit.framework.Assert.assertEquals;

import org.testng.annotations.Test;
public class TieTest {
    @Test
    public void gameOverTest(){
        Tie tie = new Tie();
        assertFalse(tie.isGameOver());
        tie.setGameOver();
        assertTrue(tie.isGameOver());
    }

    @Test
    public void addToCommonPoolTest(){
        Tie tie = new Tie();
        assertEquals(tie.getCommonPool(),0);
        tie.addToCommonPool(100);
        assertEquals(tie.getCommonPool(),100);
        tie.addToCommonPool(0);
        assertEquals(tie.getCommonPool(),100);
    }

    @Test
    public void addPlayerTest(){
        Tie tie = new Tie();
        assertEquals(tie.players.size(), 0);
        Player newPlayer = new Player("ala");
        tie.addPlayer(newPlayer);
        assertEquals(tie.players.size(), 1);
    }

    @Test
    public void poolInCurrentBettingTest(){
        Tie tie = new Tie();
        assertEquals(tie.getPoolInCurrentBetting(), 0);
        tie.setPoolInCurrentBetting(100);
        assertEquals(tie.getPoolInCurrentBetting(),100);
        assertEquals(tie.getCommonPool(),0);
    }

    @Test
    public void displayPlayersInGameTest() {
        Tie tie = new Tie();
        assertEquals(tie.displayPlayersInGame(),"");
    }
}

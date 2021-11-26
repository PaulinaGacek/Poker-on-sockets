package pl.edu.agh.kis.pz1;

import static junit.framework.Assert.*;
import static junit.framework.Assert.assertEquals;

// coverage 100%

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
        Player newPlayer1 = new Player("asia");
        tie.addPlayer(newPlayer1);
        assertEquals(tie.displayPlayersInGame(),"asia, ");
        Player newPlayer2 = new Player("basia");
        tie.addPlayer(newPlayer2);
        assertEquals(tie.displayPlayersInGame(),"asia, basia, ");
    }

    @Test
    public void prepareFOrNextBettingTest(){
        Tie tie = new Tie();
        Player newPlayer1 = new Player("asia");
        tie.addPlayer(newPlayer1);
        Player newPlayer2 = new Player("basia");
        tie.addPlayer(newPlayer2);
        Player newPlayer3 = new Player("basia");
        tie.addPlayer(newPlayer3);
        for(Player player: tie.players){
            player.setPoolInCurrentBetting(tie.getAnte());
        }
        tie.prepareForNextBetting();
        for(Player player: tie.players){
            assertEquals(player.getPoolInCurrentBetting(),0);
        }
    }

    @Test
    public void restartTest(){
        Tie tie = new Tie();
        Player newPlayer1 = new Player("asia");
        tie.addPlayer(newPlayer1);
        Player newPlayer2 = new Player("basia");
        tie.addPlayer(newPlayer2);
        Player newPlayer3 = new Player("basia");
        tie.addPlayer(newPlayer3);
        for(Player player: tie.players){
            player.setPoolInCurrentBetting(tie.getAnte());
        }

        tie.restart();
        assertEquals(tie.getCommonPool(),0);
        assertFalse(tie.isGameOver());
        assertTrue(tie.players.isEmpty());
        assertEquals(tie.getDeck().getSize(),52);
    }
}

package pl.edu.agh.kis.pz1;

import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static junit.framework.Assert.*;

public class GameTest {
    Game game;
    ServerSocket serverSocket;
    Socket socket;

    public void prepare() throws IOException {
        game = new Game();
        serverSocket = new ServerSocket(1233);
        socket = new Socket("localhost", 1233);
    }

    public void clean() throws IOException {
        socket.close();
        serverSocket.close();
    }

    @Test
    public void restartGame() throws IOException {
        prepare();
        ClientHandler  clientHandler1 = new ClientHandler(socket, game, "ala");
        game.initPlayersInGameArray();
        game.restartGame();
        assertEquals(Game.clientHandlers.size(),0);
        clientHandler1.removeClientHandler();
        clean();
    }

    @Test
    public void checkIfGameOverTest() throws IOException {
        prepare();
        ClientHandler  clientHandler1 = new ClientHandler(socket, game, "ala");
        game.initPlayersInGameArray();
        assertTrue(game.checkIfGameOver());
        ClientHandler  clientHandler2 = new ClientHandler(socket, game, "a");
        game.tie.setGameNotOver();
        game.updatePlayersInGame();
        assertFalse(game.checkIfGameOver());
        clientHandler1.removeClientHandler();
        clientHandler2.removeClientHandler();
        clean();
    }

    @Test
    public void handlePassAndWaitTest() throws IOException {
        prepare();
        ClientHandler  clientHandler1 = new ClientHandler(socket, game, "ala");
        game.initPlayersInGameArray();
        game.handleMove(1);
        assertFalse(clientHandler1.player.getHasPassed());
        assertEquals(0,clientHandler1.player.getPoolInCurrentBetting());
        game.handleMove(2);
        assertTrue(clientHandler1.player.getHasPassed());
        clientHandler1.removeClientHandler();
        clean();
    }

    @Test
    public void isMovePossibleTest() throws IOException {
        prepare();
        ClientHandler  clientHandler1 = new ClientHandler(socket, game, "c");
        game.initPlayersInGameArray();
        assertTrue(game.isMovePossible(1));
        assertTrue(game.isMovePossible(2));
        assertTrue(game.isMovePossible(3));
        clientHandler1.removeClientHandler();
        clean();
    }

    @Test
    public void isRoundCompleteTest() throws IOException {
        prepare();
        ClientHandler  clientHandler1 = new ClientHandler(socket, game, "b");
        game.initPlayersInGameArray();
        assertTrue(game.checkIfRoundIsComplete()); // only one player

        ClientHandler  clientHandler2 = new ClientHandler(socket, game, "a");
        ClientHandler  clientHandler3 = new ClientHandler(socket, game, "c");
        game.updatePlayersInGame();
        assertTrue(game.checkIfRoundIsComplete());
        clientHandler3.player.setHasPassed();
        game.removePassedPlayers();
        game.updatePlayersInGame();
        game.removePassedPlayers();
        game.tie.setPoolInCurrentBetting(100);
        assertFalse(game.checkIfRoundIsComplete());

        clientHandler1.player.setPoolInCurrentBetting(100);
        clientHandler2.player.setHasPassed();
        assertTrue(game.checkIfRoundIsComplete());

        clientHandler1.removeClientHandler();
        clientHandler2.removeClientHandler();
        clientHandler3.removeClientHandler();
        clean();
    }

    @Test
    public void prepareGameTest() throws IOException {
        prepare();
        ClientHandler  clientHandler1 = new ClientHandler(socket, game, "z");
        ClientHandler  clientHandler2 = new ClientHandler(socket, game, "w");
        ClientHandler  clientHandler3 = new ClientHandler(socket, game, "j");
        game.prepareGame();
        assertEquals(game.getCurrentPlayer(),Game.playersInGame.get(0));
        clientHandler1.removeClientHandler();
        clientHandler2.removeClientHandler();
        clientHandler3.removeClientHandler();
        clean();
    }

    @Test
    public void thankUNextTest() throws IOException {
        prepare();
        ClientHandler  clientHandler1 = new ClientHandler(socket, game, "z");
        ClientHandler  clientHandler2 = new ClientHandler(socket, game, "w");
        ClientHandler  clientHandler3 = new ClientHandler(socket, game, "j");
        game.initPlayersInGameArray();
        assertEquals(game.getCurrentPlayer(),Game.playersInGame.get(0));
        game.thankUNext();
        assertEquals(game.getCurrentPlayer(),Game.playersInGame.get(1));
        game.thankUNext();
        assertEquals(game.getCurrentPlayer(),Game.playersInGame.get(2));
        game.thankUNext();
        assertEquals(game.getCurrentPlayer(),Game.playersInGame.get(0));
        game.showWhoseTurn();
        game.displayPlayersInGame();
        clientHandler1.removeClientHandler();
        clientHandler2.removeClientHandler();
        clientHandler3.removeClientHandler();
        clean();
    }

    @Test
    public void playTest() throws IOException {
        prepare();
        ClientHandler  clientHandler1 = new ClientHandler(socket, game, "m");
        ClientHandler  clientHandler2 = new ClientHandler(socket, game, "n");
        ClientHandler  clientHandler3 = new ClientHandler(socket, game, "y");
        game.tie.setGameOver();
        game.play();
        assertTrue(game.tie.isGameOver());
        clientHandler1.removeClientHandler();
        clientHandler2.removeClientHandler();
        clientHandler3.removeClientHandler();
        clean();
    }

}

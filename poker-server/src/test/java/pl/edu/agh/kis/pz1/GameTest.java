package pl.edu.agh.kis.pz1;

import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

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
    /*
    @Test
    public void restartGame() throws IOException {
        prepare();
        ClientHandler  clientHandler1 = new ClientHandler(socket, game, "ala");
        game.initPlayersInGameArray();
        game.restartGame();
        assertEquals(Game.clientHandlers.size(),0);
        clean();
    }
    */
    @Test
    public void checkIfGameOverTest() throws IOException {
        prepare();
        ClientHandler  clientHandler1 = new ClientHandler(socket, game, "ala");
        game.initPlayersInGameArray();
        assertTrue(game.checkIfGameOver());
        clientHandler1.removeClientHandler();
        clean();
    }
}

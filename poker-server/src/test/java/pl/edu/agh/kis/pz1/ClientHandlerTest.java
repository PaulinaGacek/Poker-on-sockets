package pl.edu.agh.kis.pz1;

import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.stream.Collectors;

import static junit.framework.Assert.*;

public class ClientHandlerTest {
    Game game;
    ServerSocket serverSocket;
    Socket socket;

    public void prepare() throws IOException {
        game = new Game();
        serverSocket = new ServerSocket(1234);
        socket = new Socket("localhost", 1234);
    }

    @Test
    public void constructorAndRemoveTest() throws IOException {
        prepare();
        ClientHandler clientHandler1 = new ClientHandler(socket, game, "ala");
        assertEquals(ClientHandler.clientHandlers.size(),1);
        System.out.println(clientHandler1.getClientUsername());
        clientHandler1.removeClientHandler();
        assertEquals(ClientHandler.clientHandlers.size(),0);
        socket.close();
        serverSocket.close();
    }

    @Test
    public void closeEverythingTest() throws IOException {
        prepare();
        ClientHandler clientHandler1 = new ClientHandler(socket, game, "ala");
        clientHandler1.closeEverything(clientHandler1.getSocket(), clientHandler1.getBufferedReader(), clientHandler1.bufferedWriter);
        assertEquals(ClientHandler.clientHandlers.size(),0);
        socket.close();
        serverSocket.close();
    }

    @Test
    public void isSyntaxCorrectTest() throws IOException {
        prepare();
        ClientHandler clientHandler1 = new ClientHandler(socket, game, "ala");
        assertTrue(clientHandler1.isChoiceSyntaxOk("1",1,2));
        assertFalse(clientHandler1.isChoiceSyntaxOk("cs20",1,5));
        //clean up
        clientHandler1.closeEverything(clientHandler1.getSocket(), clientHandler1.getBufferedReader(), clientHandler1.bufferedWriter);
        socket.close();
        serverSocket.close();
    }

    @Test
    public void broadcastMessageTest() throws IOException {
        prepare();
        ClientHandler clientHandler1 = new ClientHandler(socket, game, "a");
        ClientHandler clientHandler2 = new ClientHandler(socket, game, "b");
        game.initPlayersInGameArray();
        game.updatePlayersArray();
        clientHandler1.broadcastMessageToAll("example");
        assertNotNull(clientHandler1.getBufferedReader().lines().toString());
        clientHandler1.broadcastMessageToOthers("example");
        assertNotNull(clientHandler1.getBufferedReader().lines().toString());
        clientHandler1.broadcastMessageToItself("example");
        assertNotNull(clientHandler1.getBufferedReader().lines().toString());
        //clean up
        clientHandler1.closeEverything(clientHandler1.getSocket(), clientHandler1.getBufferedReader(), clientHandler1.bufferedWriter);
        clientHandler2.closeEverything(clientHandler1.getSocket(), clientHandler1.getBufferedReader(), clientHandler1.bufferedWriter);
        socket.close();
        serverSocket.close();
    }
}

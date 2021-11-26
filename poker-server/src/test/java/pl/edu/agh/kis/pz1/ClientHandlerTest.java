package pl.edu.agh.kis.pz1;

import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static junit.framework.Assert.*;

public class ClientHandlerTest {

    @Test
    public void constructorAndRemoveTest() throws IOException {
        Game game = new Game();
        ServerSocket serverSocket = new ServerSocket(1234);
        Socket socket = new Socket("localhost", 1234);
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
        Game game = new Game();
        ServerSocket serverSocket = new ServerSocket(1233);
        Socket socket = new Socket("localhost", 1233);
        ClientHandler clientHandler1 = new ClientHandler(socket, game, "ala");
        clientHandler1.closeEverything(clientHandler1.getSocket(), clientHandler1.getBufferedReader(), clientHandler1.bufferedWriter);
        assertEquals(ClientHandler.clientHandlers.size(),0);
        socket.close();
        serverSocket.close();
    }

    @Test
    public void isSyntaxCorrectTest() throws IOException {
        Game game = new Game();
        ServerSocket serverSocket = new ServerSocket(1233);
        Socket socket = new Socket("localhost", 1233);
        ClientHandler clientHandler1 = new ClientHandler(socket, game, "ala");
        assertTrue(clientHandler1.isChoiceSyntaxOk("1",1,2));
        assertFalse(clientHandler1.isChoiceSyntaxOk("cs20",1,5));
        //clean up
        clientHandler1.closeEverything(clientHandler1.getSocket(), clientHandler1.getBufferedReader(), clientHandler1.bufferedWriter);
        socket.close();
        serverSocket.close();
    }
}

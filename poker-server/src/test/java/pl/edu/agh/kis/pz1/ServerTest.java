package pl.edu.agh.kis.pz1;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;


public class ServerTest {
    @Test
    public void constructorTest() throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        assertEquals(0,server.getNrOfPlayers());
    }
}



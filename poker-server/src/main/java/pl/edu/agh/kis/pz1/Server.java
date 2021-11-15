package pl.edu.agh.kis.pz1;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final ServerSocket serverSocket; // responsible for handling communication
    private static int nrOfPlayers = ClientHandler.clientHandlers.size();

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    /**
     * Keeps server running while server socket is not closed
     */
    private void startServer(){
        try{
            Game game = new Game();
            game.deck.displayDeck();
            System.out.println("Poker server is Running...");
            while(!serverSocket.isClosed()){
                Socket socket = serverSocket.accept(); // returns socket to specific Client
                if(nrOfPlayers < 3){
                    ClientHandler clientHandler = new ClientHandler(socket, game);
                    System.out.println("A new player has joined the game!");
                    updateNrOfPlayers();
                    System.out.println("Now there are "+ nrOfPlayers + " players in game");
                    waitForMorePlayers(clientHandler);
                    Thread thread = new Thread(clientHandler);
                    thread.start();
                    if(nrOfPlayers==3){
                        game.play();
                    }
                }
                else{
                    System.out.println("Nr of players exceeded!");
                }
            }
        }
        catch(IOException e){
            closeServerSocket();
        }
    }

    private void closeServerSocket(){
        try{
            if(serverSocket != null){
                serverSocket.close();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        server.startServer();
    }

    //game
    private void updateNrOfPlayers(){
        nrOfPlayers = ClientHandler.clientHandlers.size();
    }
    public int getNrOfPlayers(){return nrOfPlayers;}

    private void waitForMorePlayers(ClientHandler clientHandler) {
        if(nrOfPlayers<3)
            clientHandler.broadcastMessageToAll("We are still waiting for " + (3-nrOfPlayers)+ " players...");
        else if(nrOfPlayers==3)
            clientHandler.broadcastMessageToAll("All players joined, lets start the game!");
    }
}
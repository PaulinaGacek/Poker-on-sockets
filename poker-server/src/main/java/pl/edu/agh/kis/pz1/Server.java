package pl.edu.agh.kis.pz1;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private final ServerSocket serverSocket; // responsible for handling communication
    private static int nrOfPlayers = ClientHandler.clientHandlers.size();
    private boolean gameFinished = false;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
    private int requiredPlayers = 0;

    /**
     * Keeps server running while server socket is not closed
     */
    private void startServer(){
        try{
            Scanner scanner = new Scanner(System.in);
            while(requiredPlayers<2 || requiredPlayers>4){
                logMessage("Enter nr of players: ");
                requiredPlayers = scanner.nextInt();
            }
            Game game = new Game();
            game.tie.getDeck().displayDeck();
            logMessage("Poker server is Running...");
            while(!serverSocket.isClosed()){
                Socket socket = serverSocket.accept(); // returns socket to specific Client
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                if(nrOfPlayers < requiredPlayers){
                    ClientHandler clientHandler = new ClientHandler(socket, game, bufferedReader.readLine());
                    logMessage("A new player has joined the game!");
                    updateNrOfPlayers();
                    logMessage("Now there are "+ nrOfPlayers + " players in game");
                    waitForMorePlayers(clientHandler);
                    Thread thread = new Thread(clientHandler);
                    thread.start();
                    if(nrOfPlayers==requiredPlayers){
                        game.play();
                        if(!game.areEagerForNextRound()){
                            game.restartGame();
                            ClientHandler.clientHandlers.clear();
                            updateNrOfPlayers();
                            game.tie.getDeck().displayDeck();
                            logMessage("Poker server is Running...");
                        }
                        else{
                            logMessage("Next round is up!");
                            game.tie.getDeck().displayDeck();
                            logMessage("Poker server is Running...");
                            game.play();
                        }
                    }
                }
                else{
                    logMessage("Nr of players exceeded!");
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
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        server.startServer();
    }

    //game
    public int getNrOfPlayers(){return nrOfPlayers;}
    private static void updateNrOfPlayers(){
        nrOfPlayers = ClientHandler.clientHandlers.size();
    }
    private void waitForMorePlayers(ClientHandler clientHandler) {
        if(nrOfPlayers<requiredPlayers)
            clientHandler.broadcastMessageToAll("We are still waiting for " + (requiredPlayers-nrOfPlayers)+ " players...");
        else if(nrOfPlayers==requiredPlayers)
            clientHandler.broadcastMessageToAll("All players joined, lets start the game!");
    }

    private void logMessage(String message){
        System.out.println(message);
    }
}
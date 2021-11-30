package pl.edu.agh.kis.pz1;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * It is responsible for communicating with assigned Client,
 * implements Runnable - each instance is executed by separate Thread
 */
public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;
    public Player player;

    public Socket getSocket(){return socket;}
    public BufferedReader getBufferedReader(){return bufferedReader;}

    public ClientHandler(Socket socket, Game game, String username){
        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = username;
            this.player = new Player(this.clientUsername);
            clientHandlers.add(this);
            game.tie.addPlayer(this.player);
            broadcastMessageToOthers("SERVER: " + clientUsername + " has joined the game!");
            broadcastMessageToItself("Welcome to the game!");
        }
        catch (IOException e) {
            closeEverything();
        }
    }

    @Override
    public void run(){
        String messageFromClient;
        while(socket.isConnected()){
            try{
                if(player.getIsTheirTurn()){
                    messageFromClient = bufferedReader.readLine();
                    broadcastMessageToOthers(messageFromClient);
                }
            }
            catch(IOException e){
                closeEverything();
                break;
            }
        }
    }

    public void broadcastMessage(String messageToSend, ClientHandler clientHandler) throws IOException {
        clientHandler.bufferedWriter.write(messageToSend);
        clientHandler.bufferedWriter.newLine();
        clientHandler.bufferedWriter.flush();
    }

    public void broadcastMessageToOthers(String messageToSend){
        for(ClientHandler clientHandler: clientHandlers){
            try{
                if(!clientHandler.clientUsername.equals(clientUsername)){
                    broadcastMessage(messageToSend, clientHandler);
                }
            }
            catch(IOException e){
                closeEverything();
            }
        }
    }

    public void broadcastMessageToItself(String messageToSend){
        for(ClientHandler clientHandler: clientHandlers){
            try{
                if( clientHandler.clientUsername.equals(clientUsername)){
                    broadcastMessage(messageToSend, clientHandler);
                }
            }
            catch(IOException e){
                closeEverything();
            }
        }
    }

    public void broadcastMessageToAll(String messageToSend){
        for(ClientHandler clientHandler: clientHandlers){
            try{
                broadcastMessage(messageToSend, clientHandler);
            }
            catch(IOException e){
                closeEverything();
            }
        }
    }

    /**
     * Removes client from array of clients
     */
    public void removeClientHandler(){
        broadcastMessageToOthers("SERVER: " + clientUsername + " has left the game :c");
        Game.clientHandlers.remove(this);  //
        Game.playersInGame.remove(this);   //
        clientHandlers.remove(this);
    }

    public void closeEverything() {
        removeClientHandler();
        try{
            if(bufferedReader != null) {
                bufferedReader.close();
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
            if (socket != null){
                socket.close();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void displayCards(){
        broadcastMessageToItself(player.displayCards());
    }

    public String getClientUsername(){
        return clientUsername;
    }

    // giving answers

    private String extractNumbersFromString(String answer){
        return answer.replaceAll("[^0-9]", "");
    }

    public boolean isChoiceSyntaxOk(String answer, int lowerBound, int upperBound){ // bounds are inclusive
        String numberOnly = extractNumbersFromString(answer);
        if(numberOnly.equals(""))
            return false;
        int number = Integer.parseInt(numberOnly);
        return number >= lowerBound && number <= upperBound;
    }

    private int returnDecision(int lowerBound, int upperBound){
        try{
            String s = bufferedReader.readLine();
            while(!isChoiceSyntaxOk(s,lowerBound,upperBound)){
                broadcastMessageToItself("Improper syntax of your answer, input number between " +
                        lowerBound + " and " + upperBound);
                s = bufferedReader.readLine();
            }
            String numberOnly = extractNumbersFromString(s);
            return Integer.parseInt(numberOnly);
        } catch (IOException e){
            e.printStackTrace();
        }
        return 0;
    }

    public int decideWhichMoveToDo(){
        return returnDecision(1,3);
    }

    public int decideHowMuchRaiseStakes(){
        return returnDecision(1,1000000);
    }

    public int decideWhatToSwap() {
        return returnDecision(0,5);
    }

    private int chooseCardToSwap(){
        return returnDecision(1,5);
    }


    public void swapCads(int swappedCards, Tie tie) {
        if(swappedCards==5){
            // player wants to swap all cards - no need to ask which of them
            for(int i = 0; i < 5; ++i){
                player.swapCard(i, tie.getDeck());
            }
        }
        else if(swappedCards>0){
            int[] ifSwapped = {0,0,0,0,0};
            String[] numbers = {"1st","2nd","3rd","4th","5th"};
            for(int i = 0; i < swappedCards; ++i){
                broadcastMessageToItself("Choose index of the "+ numbers[i] + " card to swap, " +
                        "input number between 1 and 5");
                int option = chooseCardToSwap();
                while(ifSwapped[option-1]==1){
                    broadcastMessageToItself("Choose card which you haven't chosen");
                    option = chooseCardToSwap();
                }
                ifSwapped[option-1]=1;
            }
            for(int i = 0; i < 5; ++i){
                if(ifSwapped[i]==1){
                    player.swapCard(i, tie.getDeck());
                }
            }
        }
    }
}

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
    public BufferedWriter bufferedWriter;
    private String clientUsername;
    public Player player;
    public ClientHandler(Socket socket, Game game){
        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();
            this.player = new Player(this.clientUsername);
            clientHandlers.add(this);
            game.tie.addPlayer(this.player);
            broadcastMessageToOthers("SERVER: " + clientUsername + " has joined the game!");
            broadcastMessageToItself("Welcome to the game!");
        }
        catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
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
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    public void broadcastMessageToOthers(String messageToSend){
        for(ClientHandler clientHandler: clientHandlers){
            try{
                if(!clientHandler.clientUsername.equals(clientUsername)){
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            }
            catch(IOException e){
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void broadcastMessageToItself(String messageToSend){
        for(ClientHandler clientHandler: clientHandlers){
            try{
                if( clientHandler.clientUsername.equals(clientUsername)){
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            }
            catch(IOException e){
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void broadcastMessageToAll(String messageToSend){
        for(ClientHandler clientHandler: clientHandlers){
            try{
                clientHandler.bufferedWriter.write(messageToSend);
                clientHandler.bufferedWriter.newLine();
                clientHandler.bufferedWriter.flush();
            }
            catch(IOException e){
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    // TO DO - it throws exception when someone leaves game
    /**
     * Removes client from array of clients
     */
    public void removeClientHandler(){
        Game.clientHandlers.remove(this);  //
        Game.playersInGame.remove(this);   //
        clientHandlers.remove(this);
        broadcastMessageToOthers("SERVER: " + clientUsername + " has left the game :c");
    }

    public void closeEverything( Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
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

    public void payAnte(int ante){
        if(!player.pay(ante)){
            broadcastMessageToItself("You have too little money to join this round");
        }
    }

    public String getClientUsername(){
        return clientUsername;
    }

    public int decideWhatToDo(){
        int option = 0;
        String numberOnly = "";
        try{
            String s = bufferedReader.readLine();
            while(!isChoiceSyntaxOk(s)){
                broadcastMessageToItself("Improper syntax of your answer, input number between 1 and 3");
                s = bufferedReader.readLine();
            }
            numberOnly = extractNumbersFromString(s);
            option = Integer.parseInt(numberOnly);
            System.out.println(getClientUsername()+ " chose option "+option);
        } catch (IOException e){
            e.printStackTrace();
        }
        return option;
    }

    private boolean isChoiceSyntaxOk(String answer){
        String numberOnly = extractNumbersFromString(answer);
        if(numberOnly.equals(""))
            return false;
        int number = Integer.parseInt(numberOnly);
        return number >= 1 && number <= 3;
    }

    public int raiseStakes(){
        int option = 0;
        String numberOnly = "";
        try{
            String s = bufferedReader.readLine();
            while(!isStakeSyntaxOk(s)){
                broadcastMessageToItself("Improper syntax of your answer, input positive number");
                s = bufferedReader.readLine();
            }
            numberOnly = extractNumbersFromString(s);
            option = Integer.parseInt(numberOnly);
            System.out.println("Option "+option);
        } catch (IOException e){
            e.printStackTrace();
        }
        return option;
    }


    private boolean isStakeSyntaxOk(String answer){
        String numberOnly = extractNumbersFromString(answer);
        if(numberOnly.equals(""))
            return false;
        int number = Integer.parseInt(numberOnly);
        return number >= 0;
    }

    private String extractNumbersFromString(String answer){
        return answer.replaceAll("[^0-9]", "");
    }
}

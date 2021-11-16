package pl.edu.agh.kis.pz1;
import pl.edu.agh.kis.pz1.util.Deck;

import java.io.IOException;
import java.util.ArrayList;
public class Game {
    private ClientHandler currentPlayer;
    private static int nrOfPlayers = 0;
    public static ArrayList<ClientHandler> clientHandlers;
    public static ArrayList<ClientHandler> playersInGame = new ArrayList<>();
    public Deck deck = new Deck(false);
    private int ante = 100;
    private int commonPool = 0;
    private int poolInCurrentBetting = 0;
    int WAIT = 1, PASS = 2, RAISE = 3;
    public boolean isGameOver = false;

    public void play() throws IOException {
        updateNrOfPlayersInGameAndGameOver();
        updateClientHandlerArray();
        initPlayersArray();
        currentPlayer = clientHandlers.get(0);
        collectAnte();
        dealOutInitialCards();
        deck.displayDeck();
        displayCards();

        // 1st betting
        for(int i = 0; i < 2; ++i) { //debug
            if(!isGameOver){
                handleBetting();
                while (!checkIfRoundIsComplete()) {
                    handleBetting();
                }
            }
        }
        System.out.println("Finish");
    }

    private void handleBetting() throws IOException {
        for(int i = 0; i < nrOfPlayers; ++i){
            checkIfGameOver();
            showWhoseTurn();
            currentPlayer.bufferedWriter.flush();
            currentPlayer.broadcastMessageToItself("\nEntrance pool: "+poolInCurrentBetting+ "\n"+
                    "What do you want to do?\n(1) Wait\n(2) Pass\n(3) Raise the stakes");
            int move = currentPlayer.decideWhatToDo();
            handleMove(move);
            while(!isMovePossible(move)){
                currentPlayer.broadcastMessageToItself("Your move is invalid, do sth else");
                move = currentPlayer.decideWhatToDo();
                handleMove(move);
            }
            updatePlayersArray();
            updateNrOfPlayersInGameAndGameOver();
            currentPlayer.broadcastMessageToAll(displayPlayersInGame());
            checkIfGameOver();
            setNextPlayersTurn();
        }
        currentPlayer.broadcastMessageToAll("------- betting round finished --------");
        // ends in bad moment
    }

    private void handleMove(int move) {
        if(move==PASS){
            currentPlayer.player.setHasPassed();
            currentPlayer.broadcastMessageToOthers(currentPlayer.getClientUsername()+" passed");
        }else if(move==RAISE){
            currentPlayer.broadcastMessageToItself("Entrance stake you want to raise: ");
            int raise = currentPlayer.raiseStakes();
            currentPlayer.player.setPoolInCurrentBetting(
                    currentPlayer.player.getPoolInCurrentBetting()+raise
            );
            currentPlayer.player.pay(raise);
            poolInCurrentBetting = currentPlayer.player.getPoolInCurrentBetting();
        }else if(move==WAIT){
            currentPlayer.broadcastMessageToOthers("\n" + currentPlayer.getClientUsername()+" waits");
        }
    }

    private boolean isMovePossible(int move){
        if(move==WAIT){
            return poolInCurrentBetting == currentPlayer.player.getPoolInCurrentBetting();
        }else if(move==RAISE) {
            return currentPlayer.getPool() > 0;
        }
        return true; // PASS is always possible
    }

    /**
     * betting round is complete when:
     * (A) there is only 1 player
     * (B) all players pay pool in current betting
     * @return whether betting round is complete
     */
    private boolean checkIfRoundIsComplete(){
        for(int i = 0; i < playersInGame.size(); ++i){
            if(playersInGame.get(i).player.getHasPassed()){
                playersInGame.remove(playersInGame.get(i));
            }
        }
        if (playersInGame.size()==0 || playersInGame.size()==1){
            isGameOver = true;
            return true;
        }
        for(ClientHandler player: playersInGame){
            if(player.player.getPoolInCurrentBetting() != poolInCurrentBetting){
                return false;
            }
        }
        return true;
    }

    private void collectAnte() {
        currentPlayer.broadcastMessageToAll("\nAnte ("+ante+") was taken from your pool in order to join the game");
        for(ClientHandler player: clientHandlers){
            player.payAnte(ante);
            player.broadcastMessageToItself("Your current pool: " + player.getPool());
        }
        updateNrOfPlayersInGameAndGameOver();
        commonPool += ante * nrOfPlayers;
        currentPlayer.broadcastMessageToAll("Common pool: "+ commonPool);
    }

    private void displayCards() {
        currentPlayer.broadcastMessageToAll("\nYour cards:");
        for(ClientHandler player: clientHandlers){
            player.displayCards();
        }
    }

    private void dealOutInitialCards() {
        for(int i = 0; i < 5; ++i){
            for(ClientHandler player: clientHandlers){
                player.addCard(deck.dealOutCard());
            }
        }
    }

    private void updateClientHandlerArray() {
        clientHandlers = ClientHandler.clientHandlers;
    }

    private void initPlayersArray() {
        playersInGame.addAll(ClientHandler.clientHandlers);
    }

    private void updatePlayersArray() {
        int indexToRemove = -1;
        for(int i = 0; i < playersInGame.size();++i){
            if(playersInGame.get(i).player.getHasPassed()){
                indexToRemove = i;
                break;
            }
        }
        if(indexToRemove!=-1){
            int previousPlayerIndex = indexToRemove - 1;
            if(previousPlayerIndex==-1){ // player who passed was first
                previousPlayerIndex = playersInGame.size()-2;
            }
            playersInGame.remove(currentPlayer);
            currentPlayer = playersInGame.get(previousPlayerIndex);
        }
    }

    private void updateNrOfPlayersInGameAndGameOver() {
        nrOfPlayers = playersInGame.size();
        if(nrOfPlayers==1){
            isGameOver = true;
        }
    }

    public int getAnte(){
        return ante;
    }

    // TODO
    // Here should people be deleted from list not in more higher methods
    // Problem with loop in next rounds - it finished in bad moment
    public void setNextPlayersTurn(){
        int currentIndex = playersInGame.indexOf(currentPlayer);
        if(currentIndex == nrOfPlayers-1){
            currentIndex = 0;
        }
        else{
            currentIndex++;
        }
        for(int i = 0; i < nrOfPlayers; ++i){
            if(i != currentIndex) {
                playersInGame.get(i).setIsTheirTurn(false);
            }
            else{
                playersInGame.get(i).setIsTheirTurn(true); // why true is never set??? - now its set
            }
        }
        currentPlayer = playersInGame.get(currentIndex);
        updateNrOfPlayersInGameAndGameOver();
        updatePlayersArray();
    }

    public void showWhoseTurn(){
        currentPlayer.broadcastMessageToItself("It is your turn");
        currentPlayer.broadcastMessageToOthers("It is " + currentPlayer.getClientUsername() + "'s turn");
    }

    // debug
    private String displayPlayersInGame(){
        String message = "Players in game: ";
        for(ClientHandler player: playersInGame){
            message += player.getClientUsername() + ", ";
        }
        return message;
    }

    public void checkIfGameOver(){
        if(isGameOver){
            currentPlayer.broadcastMessageToAll("GAME OVER");
            currentPlayer.broadcastMessageToAll(currentPlayer.getClientUsername()+" WON "+commonPool);
        }
    }
}

package pl.edu.agh.kis.pz1;
import pl.edu.agh.kis.pz1.util.Deck;

import java.io.IOException;
import java.util.ArrayList;
public class Game {
    private ClientHandler currentPlayer;
    private static int nrOfPlayers = 0;
    public static ArrayList<ClientHandler> clientHandlers; // clients connected to socket
    public static ArrayList<ClientHandler> playersInGame = new ArrayList<>(); // clients in game
    public Tie tie = new Tie();
    public static ArrayList<Player> players = new ArrayList<>();
    int WAIT = 1, PASS = 2, RAISE = 3;

    public void play() throws IOException {
        updateNrOfPlayersInGameAndGameOver();
        updateClientHandlerArray();
        initPlayersArray();
        currentPlayer = clientHandlers.get(0);
        collectAnte();
        dealOutInitialCards();
        tie.getDeck().displayDeck();
        displayCards();

        // 1st betting
        for(int i = 0; i < 2; ++i) { //debug
            if(!tie.isGameOver()){
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
            currentPlayer.broadcastMessageToItself("\nEntrance pool: "+tie.getPoolInCurrentBetting()+ "\n"+
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
                    currentPlayer.player.getPoolInCurrentBetting()+raise);
            currentPlayer.player.pay(raise);
            tie.setPoolInCurrentBetting(currentPlayer.player.getPoolInCurrentBetting());
        }else if(move==WAIT){
            currentPlayer.broadcastMessageToOthers("\n" + currentPlayer.getClientUsername()+" waits");
        }
    }

    private boolean isMovePossible(int move){
        if(move==WAIT){
            return tie.getPoolInCurrentBetting() == currentPlayer.player.getPoolInCurrentBetting();
        }else if(move==RAISE) {
            return currentPlayer.player.getPool() > 0;
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
            tie.setGameOver();
            return true;
        }
        for(ClientHandler player: playersInGame){
            if(player.player.getPoolInCurrentBetting() != tie.getPoolInCurrentBetting()){
                return false;
            }
        }
        return true;
    }

    private void collectAnte() {
        currentPlayer.broadcastMessageToAll("\nAnte ("+tie.getAnte()+") was taken from your pool in order to join the game");
        for(ClientHandler player: clientHandlers){
            player.payAnte(tie.getAnte());
            player.broadcastMessageToItself("Your current pool: " + player.player.getPool());
        }
        updateNrOfPlayersInGameAndGameOver();
        tie.addToCommonPool(tie.getAnte() * nrOfPlayers);
        currentPlayer.broadcastMessageToAll("Common pool: "+ tie.getCommonPool());
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
                player.player.addCard(tie.getDeck().dealOutCard());
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
            tie.setGameOver();
        }
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
                playersInGame.get(i).player.setNotTheirTurn();
            }
            else{
                playersInGame.get(i).player.setTheirTurn(); // why true is never set??? - now its set
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
        if(tie.isGameOver()){
            currentPlayer.broadcastMessageToAll("GAME OVER");
            currentPlayer.broadcastMessageToAll(currentPlayer.getClientUsername()+" WON "+tie.getCommonPool());
        }
    }

    /**
     * Add Player object to players array
     * @param player player attribute of new ClientHandler
     */
    public void addPlayer(Player player) {
        tie.players.add(player);
    }
}

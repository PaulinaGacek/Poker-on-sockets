package pl.edu.agh.kis.pz1;

import pl.edu.agh.kis.pz1.util.Deck;
import pl.edu.agh.kis.pz1.util.Move;
import java.util.ArrayList;

public class Tie {
    private int nrOfPlayers = 0;
    private Deck deck = new Deck(false);
    private final int ante = 100;
    private int commonPool = 0;
    private int poolInCurrentBetting = 0;
    Move[] moves = new Move[]{Move.WAIT, Move.PASS, Move.RAISE};
    private boolean isGameOver = false;
    public ArrayList<Player> players = new ArrayList<>();

    // getters
    public Deck getDeck(){
        return deck;
    }
    public boolean isGameOver(){
        return isGameOver;
    }
    public int getAnte(){
        return ante;
    }
    public int getCommonPool(){
        return commonPool;
    }
    public int getPoolInCurrentBetting(){
        return poolInCurrentBetting;
    }

    // setters
    public void setGameOver(){
        isGameOver = true;
    }
    public void setPoolInCurrentBetting(int newPool){
        poolInCurrentBetting = newPool;
    }
    public void addToCommonPool(int input){
        if(input > 0){
            commonPool += input;
        }
    }

    // game

    /**
     * Add Player object to players array
     * @param newPlayer player attribute of new ClientHandler
     */
    public void addPlayer(Player newPlayer){
        nrOfPlayers++;
        players.add(newPlayer);
    }

    public String displayPlayersInGame(){
        String message = "";
        for(int i = 0; i < players.size(); ++i){
            message += players.get(i).getName() + ", ";
        }
        return message;
    }
}

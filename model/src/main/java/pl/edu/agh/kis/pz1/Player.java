package pl.edu.agh.kis.pz1;

import pl.edu.agh.kis.pz1.util.Card;
import java.util.ArrayList;

public class Player {
    private ArrayList<Card> cards = new ArrayList<>();
    private int pool = 1000;
    private boolean isTheirTurn = false;
    private int poolInCurrentBetting = 0;
    private boolean hasPassed = false;

    // getters
    public int getPool(){return pool;}
    public boolean getIsTheirTurn(){return isTheirTurn;}
    public boolean getHasPassed(){return hasPassed;}
    public ArrayList<Card> getCards(){return cards;}
    public int getPoolInCurrentBetting() {
        return poolInCurrentBetting;
    }

    // setters
    public void setHasPassed(){hasPassed = true;}
    public void setTheirTurn(){isTheirTurn = true;}
    public void setNotTheirTurn(){isTheirTurn = false;}
    public void setPoolInCurrentBetting(int newPool){
        poolInCurrentBetting = newPool;
    }

    // game
    public boolean pay(int amount){
        if(amount < 0 || amount > pool){
            return false;
        }
        pool-= amount;
        return true;
    }
    public void addCard(Card card){
        if(cards.size()<5){
            cards.add(card);
        }
    }
}

package pl.edu.agh.kis.pz1;

import pl.edu.agh.kis.pz1.util.Card;
import pl.edu.agh.kis.pz1.util.Combination;
import pl.edu.agh.kis.pz1.util.Deck;
import pl.edu.agh.kis.pz1.util.Rank;

import java.util.ArrayList;

public class Player {
    private ArrayList<Card> cards = new ArrayList<>();
    private int pool = 1000;
    private boolean isTheirTurn = false;
    private int poolInCurrentBetting = 0;
    private boolean hasPassed = false;
    private String name;

    public Player(String name){
        this.name = name;
    }

    // getters
    public int getPool(){return pool;}
    public boolean getIsTheirTurn(){return isTheirTurn;}
    public boolean getHasPassed(){return hasPassed;}
    public ArrayList<Card> getCards(){return cards;}
    public int getPoolInCurrentBetting() {
        return poolInCurrentBetting;
    }
    public String getName(){return name;}

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

    /**
     * Add new card to player's pack
     * @param card new card of tha player
     */
    public void addCard(Card card){
        if(cards.size()<5){
            cards.add(card);
        }
    }

    /**
     * @return String with enumerated cards in format (Rank, Suit)
     */
    public String displayCards(){
        if(cards.isEmpty()){
            return "You do not have any cards yet!";
        }
        StringBuilder message = new StringBuilder("");
        for(Card c: getCards()){
            message.append(c.getString());
        }
        return message.toString();
    }

    /**
     * Player can swap the card of given index for the first one on deck
     * @param index index of card to being swapped
     * @param deck current deck
     * @return true is operation was successful
     */
    public boolean swapCard(int index, Deck deck){
        if(index < 0 || index > cards.size()){
            return false;
        }
        cards.set(index,deck.dealOutCard());
        return true;
    }

    public Combination getCombination(){
        Hand hand = new Hand();
        return hand.findCombinationInCards(getCards());
    }

    public Rank getHighestRankInCombination(){
        Hand hand = new Hand();
        return hand.getHighestRankInCombination(getCards());
    }
}

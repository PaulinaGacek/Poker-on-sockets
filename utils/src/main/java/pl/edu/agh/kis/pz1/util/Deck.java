package pl.edu.agh.kis.pz1.util;
import java.util.ArrayList;
import java.util.Random;

public class Deck {
    private int size = 52;
    private final Random rand = new Random();
    private boolean isSorted = false;
    private boolean isInitialized = false;
    private static final Suit[] suitArray = {Suit.club, Suit.diamond, Suit.heart,Suit.spade};
    private static final Rank[] rankArray = {Rank._2, Rank._3, Rank._4, Rank._5, Rank._6,Rank._7,
            Rank._8, Rank._9, Rank._10, Rank.J, Rank.Q, Rank.K, Rank.A};
    private static ArrayList<Card> cards = new ArrayList<>();;

    private void initializeSortedDeck() {
        for(int i = 0; i < 4; ++i) {
            for(int j = 0; j < 13; ++j) {
                Card newCard = new Card(suitArray[i], rankArray[j]);
                cards.add(newCard);
            }
        }
        isInitialized = true;
        isSorted = true;
    }

    private void shuffle() {
        int MAX_NR_OF_SHUFFLES = 10;
        int nrOfShuffles = rand.nextInt(MAX_NR_OF_SHUFFLES);
        for(int i = 0; i < nrOfShuffles; ++i) {
            for(int j = 0; j < size; ++j) {
                Card temp = cards.get(i);
                int randomIndex = rand.nextInt(size);
                cards.set(i, cards.get(randomIndex));
                cards.set(randomIndex, temp);
            }
        }
    }

    public void displayDeck() {
        for(int i = 0; i < size; ++i){
            System.out.print("(" + (i+1) + "):");
            cards.get(i).displayCard();
            System.out.println();
        }
    }

    public Card dealOutCard(){
        Card topCard = cards.get(0);
        cards.remove(0);
        size = cards.size();
        return topCard;
    }

    public int getSize(){return size;}
    public Card getCard(int index){
        if(index>0 && index <getSize()){
            return cards.get(index);
        }
        return new Card();
    }

    public Deck(){
        initializeSortedDeck();
    }
    public Deck(boolean isSorted){
        initializeSortedDeck();
        if(!isSorted) {
            shuffle();
        }
    }
}

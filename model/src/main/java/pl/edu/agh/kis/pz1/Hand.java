package pl.edu.agh.kis.pz1;

import pl.edu.agh.kis.pz1.util.Card;
import pl.edu.agh.kis.pz1.util.Combination;
import pl.edu.agh.kis.pz1.util.Rank;
import pl.edu.agh.kis.pz1.util.Suit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Utility class for evaluating strength of the player's hand
 */
public class Hand {
    private static final Map<Combination, Integer> combinationRanking = new TreeMap<>();
    private static final Map<Rank, Integer> rankRanking = new TreeMap<>();
    private static final ArrayList<Rank> rankArray = new ArrayList<>(
            Arrays.asList(Rank._2,Rank._3,Rank._4,Rank._5,Rank._6,Rank._7,
                    Rank._8,Rank._9,Rank._10, Rank.J, Rank.Q, Rank.K, Rank.A)
    );

    private static final ArrayList<Suit> suitArray = new ArrayList<>(
            Arrays.asList(Suit.club, Suit.diamond, Suit.heart, Suit.spade)
    );

    public Hand(){
        initialiseCombinationRanking();
        initialiseRankRanking();
    }

    private static void initialiseRankRanking() {
        rankRanking.put(Rank._2,2);
        rankRanking.put(Rank._3,3);
        rankRanking.put(Rank._4,4);
        rankRanking.put(Rank._5,5);
        rankRanking.put(Rank._6,6);
        rankRanking.put(Rank._7,7);
        rankRanking.put(Rank._8,8);
        rankRanking.put(Rank._9,9);
        rankRanking.put(Rank._10,10);
        rankRanking.put(Rank.J,11);
        rankRanking.put(Rank.Q,12);
        rankRanking.put(Rank.K,13);
        rankRanking.put(Rank.A,14);
    }

    private static void initialiseCombinationRanking(){
        combinationRanking.put(Combination.ROYAL_FLUSH,10);
        combinationRanking.put(Combination.STRAIGHT_FLUSH,9);
        combinationRanking.put(Combination.FOUR_OF_THE_KIND,8);
        combinationRanking.put(Combination.FULL_HOUSE,7);
        combinationRanking.put(Combination.FLUSH,6);
        combinationRanking.put(Combination.STRAIGHT,5);
        combinationRanking.put(Combination.THREE_OF_THE_KIND,4);
        combinationRanking.put(Combination.TWO_PAIRS,3);
        combinationRanking.put(Combination.ONE_PAIR,2);
        combinationRanking.put(Combination.NO_PAIR,1);
    }

    /**
     * @param playersCards array numberOfPlayers x 5 with their cards
     * @return whether there is only one winner
     */
    public boolean isOnlyOneWinner(ArrayList<ArrayList<Card>> playersCards){
        int indexWhoWins = indexWhoWins(playersCards);
        Combination combination = findCombinationInCards(playersCards.get(indexWhoWins));
        int highestScore = mapHandToPoints(combination, getHighestRankInCombination(playersCards.get(indexWhoWins)));
        for(int i = 0; i < playersCards.size(); ++i){
            if(indexWhoWins!=i &&
                    highestScore == mapHandToPoints(findCombinationInCards(playersCards.get(i)), getHighestRankInCombination(playersCards.get(i)))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Points out single winner index in players array
     * @param playersCards array numberOfPlayers x 5 with their cards
     * @return index of the first winner
     */
    public int indexWhoWins(ArrayList<ArrayList<Card>> playersCards){
        int index = 0;
        Combination combination = findCombinationInCards(playersCards.get(0));
        int highestScore = mapHandToPoints(combination, getHighestRankInCombination(playersCards.get(0)));
        for(int i = 1; i < playersCards.size(); ++i){
            combination = findCombinationInCards(playersCards.get(i));
            if(highestScore < mapHandToPoints(combination, getHighestRankInCombination(playersCards.get(i)))){
                highestScore = mapHandToPoints(combination, getHighestRankInCombination(playersCards.get(i)));
                index = i;
            }
        }
        return index;
    }

    /**
     * In the case of a tie points out second winner
     * @param playersCards array numberOfPlayers x 5 with their cards
     * @return index of the second winner
     */
    public int indexSecondWinner(ArrayList<ArrayList<Card>> playersCards){
        int indexWhoWins = indexWhoWins(playersCards);
        Combination combination = findCombinationInCards(playersCards.get(indexWhoWins));
        int highestScore = mapHandToPoints(combination, getHighestRankInCombination(playersCards.get(indexWhoWins)));
        for(int i = 0; i < playersCards.size(); ++i){
            if(indexWhoWins!=i &&
                    highestScore == mapHandToPoints(findCombinationInCards(playersCards.get(i)), getHighestRankInCombination(playersCards.get(i)))) {
                return i;
            }
        }
        return indexWhoWins;
    }

    /**
     * The highest card is important only when there is a tie, it means that
     * higher card cannot be more important than the value of combination,
     * that's why combinations points are multiplied by 15
     * @param combination scored combination of cards on hand
     * @param highestRank cards with the highest rank
     * @return point for cards on hand
     */
    public int mapHandToPoints(Combination combination, Rank highestRank){
        int combinationPoints = combinationRanking.get(combination) * 15;
        int rankPoints = rankRanking.get(highestRank);
        return rankPoints + combinationPoints;
    }

    private Rank getHighestRank(ArrayList<Card> cards){
        int[] playersRanks = {0,0,0,0,0,0,0,0,0,0,0,0,0};
        for(Card card: cards){
            playersRanks[rankArray.indexOf(card.getRank())]++;
        }
        for(int i = 12; i >= 0; --i){
            if(playersRanks[i]>0){
                return rankArray.get(i);
            }
        }
        return rankArray.get(0);
    }

    private Rank getHighestFrom4OfTheKind(ArrayList<Card> cards, int[] playersRanks){
        for(Card card: cards){
            if(playersRanks[rankArray.indexOf(card.getRank())]==4){
                return card.getRank();
            }
        }
        return getHighestRank(cards);
    }

    private Rank getHighestFrom3OfTheKind(ArrayList<Card> cards, int[] playersRanks){
        for(Card card: cards){
            if(playersRanks[rankArray.indexOf(card.getRank())]==3){
                return card.getRank();
            }
        }
        return getHighestRank(cards);
    }

    private Rank getHighestFromOnePair(ArrayList<Card> cards, int[] playersRanks){
        for(Card card: cards){
            if(playersRanks[rankArray.indexOf(card.getRank())]==2){
                return card.getRank();
            }
        }
        return getHighestRank(cards);
    }

    private Rank getHighestFromTwoPairs(ArrayList<Card> cards, int[] playersRanks){
        Rank firstPair = Rank._2;
        Rank secondPair = Rank._2;
        boolean firstAssigned = false;
        for(Card card: cards){
            if(playersRanks[rankArray.indexOf(card.getRank())]==2 && !firstAssigned){
                firstAssigned = true;
                firstPair = card.getRank();
            }
            else if(playersRanks[rankArray.indexOf(card.getRank())]==2){
                secondPair = card.getRank();
            }
        }
        if(rankArray.indexOf(firstPair)>rankArray.indexOf(secondPair)){
            return firstPair;
        }
        return getHighestRank(cards);
    }

    /**
     * Returns the highest rank within player's combination
     * @param cards player's 5 cards
     * @return the highest rank within their combination
     */
    public Rank getHighestRankInCombination(ArrayList<Card> cards){
        int[] playersRanks = {0,0,0,0,0,0,0,0,0,0,0,0,0};
        for(Card card: cards){
            playersRanks[rankArray.indexOf(card.getRank())]++;
        }
        Combination combination = findCombinationInCards(cards);
        if(combination==Combination.FOUR_OF_THE_KIND){
            return getHighestFrom4OfTheKind(cards, playersRanks);
        }
        if(combination==Combination.THREE_OF_THE_KIND){
            return getHighestFrom3OfTheKind(cards, playersRanks);
        }
        if(combination==Combination.ONE_PAIR){
            return getHighestFromOnePair(cards, playersRanks);
        }
        if(combination==Combination.TWO_PAIRS){
            return getHighestFromTwoPairs(cards, playersRanks);
        }
        return getHighestRank(cards);
    }

    /**
     * Looks for some hand combination in player's cards
     * @param cards player's 5 cards
     * @return player's combination
     */
    public Combination findCombinationInCards(List<Card> cards){
        int[] playersRanks = {0,0,0,0,0,0,0,0,0,0,0,0,0};
        int[] playersSuits = {0,0,0,0};
        for(Card card: cards){
            playersSuits[suitArray.indexOf(card.getSuit())]++;
            playersRanks[rankArray.indexOf(card.getRank())]++;
        }
        if(checkRoyalFlush(playersRanks,playersSuits))
            return Combination.ROYAL_FLUSH;
        if(checkStraightFlush(playersRanks,playersSuits))
            return Combination.STRAIGHT_FLUSH;
        if(getMaxOfTheKind(playersRanks)==4)
            return Combination.FOUR_OF_THE_KIND;
        if(checkSFull(playersRanks))
            return Combination.FULL_HOUSE;
        if(checkFlush(playersSuits))
            return Combination.FLUSH;
        if(getMaxOfTheKind(playersRanks)==3)
            return Combination.THREE_OF_THE_KIND;
        // Two Pairs
        if(checkTwoPairs(playersRanks))
            return Combination.TWO_PAIRS;
        if(getMaxOfTheKind(playersRanks)==2)
            return Combination.ONE_PAIR;
        return Combination.NO_PAIR;
    }

    private boolean checkTwoPairs(int[] playersRanks) {
        boolean isFirstPair = false;
        for(int i = 0; i < 13; ++i) {
            if (playersRanks[i] == 2 && !isFirstPair) {
                isFirstPair = true;
            }
            else if(playersRanks[i] == 2){
                return true;
            }
        }
        return false;
    }

    private boolean checkSFull(int[] playersRanks) {
        for(int i = 0; i < 13; ++i) {
            if (playersRanks[i] > 0 && playersRanks[i] != 2 && playersRanks[i] !=3) {
                return false;
            }
        }
        return true;
    }

    private int getMaxOfTheKind(int[] playersRanks){
        int maxOfTheKind = 0;
        for(int i = 0; i < 13; ++i) {
            if (playersRanks[i] > maxOfTheKind) {
                maxOfTheKind = playersRanks[i];
            }
        }
        return maxOfTheKind;
    }

    private boolean checkFlush(int[] playersSuits){
        for(int i = 0; i < 4; ++i){
            if(playersSuits[i]==5){
                return true;
            }
        }
        return false;
    }

    private boolean checkStraight(int[] playersRanks){
        int startingIndex = -1;
        for(int i = 0; i < 13; ++i){
            if(playersRanks[i]>1){
                return false;
            }
            if(startingIndex==-1 && playersRanks[i]==1){
                startingIndex = i;
            }
        }
        for(int i = 1; i < 5; ++i){
            if(playersRanks[startingIndex+i]!=1){
                return false;
            }
        }
        return true;
    }

    private boolean checkStraightFlush(int[] playersRanks, int[] playersSuits) {
        if(!checkFlush(playersSuits)){
            return false;
        }
        return checkStraight(playersRanks);
    }

    private boolean checkRoyalFlush(int[] playersRanks, int[] playersSuits) {
        if(!checkFlush(playersSuits)){
            return false;
        }
        for(int i = 8; i < 13; ++i){
            if(playersRanks[i]!=1)
                return false;
        }
        return true;
    }
}
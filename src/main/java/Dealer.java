import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Random;

public class Dealer {

	Deck theDeck;
	ArrayList<Card> dealersHand = new ArrayList<Card>(3);
	
	Dealer(){
		theDeck = new Deck();
	}
	
	public ArrayList<Card> dealHand(){
		ArrayList<Card> hand = new ArrayList<>(3);
		Random rand = new Random();
		int randomCard;
		Card checkCard;
		for (int i = 0; i < 3; i++) {
			do {
				randomCard = rand.nextInt(52);
				checkCard = theDeck.deckArray.get(randomCard);
			} while(theDeck.chosenCards.contains(checkCard));
			theDeck.deckCount--;
			hand.add(checkCard);
			theDeck.chosenCards.add(checkCard);
		}
		return hand;
	}
	
	public boolean queenHigh() {
		for (int i = 0; i < 3; i++) {
			if (dealersHand.get(i).getValue() >= 12) {
				return true;
			}
		}
		return false;
	}
	
}

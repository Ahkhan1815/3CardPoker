import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Deck extends ArrayList<Card> {
	
	char cardSuit;
	ArrayList<Card> deckArray;
	Set<Card> chosenCards;
	int deckCount;
	
	Deck(){
		deckArray = new ArrayList<>(52);
		deckCount = 52;
		chosenCards = new HashSet<>();
		for(int i = 0; i < 4; i++) {
			if (i == 0) {
				cardSuit = 'C';
			}
			else if (i == 1) {
				cardSuit = 'D';
			}
			else if (i == 2) {
				cardSuit = 'S';
			}
			else if (i == 3) {
				cardSuit = 'H';
			}
			for (int j = 2; j <= 14; j++) {
				Card newCard = new Card(cardSuit, j);
				deckArray.add(newCard);
			}
		}
		Collections.shuffle(deckArray);
	}
	
	public Deck newDeck(){
		deckArray.clear();
		Deck deck = new Deck();
		Collections.shuffle(deck);
		return deck;
	}
	
}

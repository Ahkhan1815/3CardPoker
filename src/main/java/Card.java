
public class Card {
	char suit;
	int value;
	
	Card(char setSuit, int setValue){
		suit = setSuit;
		value = setValue;
	}
	
	public int getValue() {
		return value;
	}
	
	public char getSuit() {
		return suit;
	}
	
	public void setSuit(char setSuit) {
		suit = setSuit;
	}
	
	public void setValue(int setValue) {
		value = setValue;
	}
	
	public Card getCard() {
		return this;
	}
	
	public void setCard(char setSuit, int setValue) {
		suit = setSuit;
		value = setValue;
	}
}

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ThreeCardLogic {
	
	ThreeCardLogic(){
		
	}
	
	
	public static int evalHand(ArrayList<Card> hand) {
		Set<Character> suitSet = new HashSet<>();
		Set<Integer> valueSet = new HashSet<>();
		ArrayList<Integer> compare = new ArrayList<>(3);
		for (int i = 0; i < 3; i++) {
			compare.add(hand.get(i).getValue());
			suitSet.add(hand.get(i).getSuit());
			valueSet.add(hand.get(i).getValue());
		}
		Collections.sort(compare);
		Integer firstVal = compare.get(0);
		ArrayList<Integer> sequence = new ArrayList<>(3);
		sequence.add(firstVal);
		sequence.add(firstVal+1);
		sequence.add(firstVal+2);
		if (compare.equals(sequence) && (suitSet.size() == 1)) {
			return 1;
		}
		else if (valueSet.size() == 1) {
			return 2;
		}
		else if (compare.equals(sequence) && (suitSet.size() != 1)) {
			return 3;
		}
		else if (suitSet.size() == 1) {
			return 4;
		}
		else if (valueSet.size() == 2) {
			return 5;
		}
		return 0;
	}
	
	private static int compareHighCard(ArrayList<Card> dealer,ArrayList<Card> player) {
		ArrayList<Integer> compareDealer = new ArrayList<>(3);
		ArrayList<Integer> comparePlayer = new ArrayList<>(3);
		for (int i = 0; i < 3; i++) {
			compareDealer.add(dealer.get(i).getValue());
			comparePlayer.add(player.get(i).getValue());
		}
		Collections.sort(compareDealer);
		Collections.sort(comparePlayer);
		Integer playerHighVal = 0;
		Integer dealerHighVal = 0;
		int j = 2;
		while ((playerHighVal == dealerHighVal) && (j >= 0)) {
			playerHighVal = comparePlayer.get(j);
			dealerHighVal = compareDealer.get(j);
			j--;
		}
		if (playerHighVal == dealerHighVal) {
			return 0;
		}
		else if (playerHighVal < dealerHighVal){
			return 1;
		}
		else {
			return 2;
		}
	}
	
	public static int compareHands(ArrayList<Card> dealer,ArrayList<Card> player) {
		int dealerResults = evalHand(dealer);
		int playerResults = evalHand(player);
		if ((dealerResults == 0) && (playerResults != 0)){
			return 2;
		}
		else if((playerResults == 0) && (dealerResults != 0)) {
			return 1;
		}
		else if (playerResults == dealerResults) {
			return compareHighCard(dealer,player);
		}
		else {
			if (playerResults < dealerResults) {
				return 2;
			}
			else {
				return 1;
			}
		}
	}
	
	public static int evalPPWinnings(ArrayList<Card> hand, int bet) {
		int pairPlus = evalHand(hand);
		if (pairPlus == 1) {
			return 40 * bet;
		}
		else if (pairPlus == 2) {
			return 30 * bet;
		}
		else if (pairPlus == 3) {
			return 6 * bet;
		}
		else if (pairPlus == 4) {
			return 3 * bet;
		}
		else if (pairPlus == 5) {
			return bet;
		}
		else {
			return 0;
		}
	}
	
	
}

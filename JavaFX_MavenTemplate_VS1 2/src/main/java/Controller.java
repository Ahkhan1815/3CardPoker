import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Controller {
	
	public Text anteTitle1;
	public TextField anteField1;
	public Text ppTitle1;
	public TextField ppField1;
	public Text playTitle1;
	public TextField playField1;
	public Text winningsTitle1;
	public TextField winningsField1;
	
	public Text anteTitle2;
	public TextField anteField2;
	public Text ppTitle2;
	public TextField ppField2;
	public Text playTitle2;
	public TextField playField2;
	public Text winningsTitle2;
	public TextField winningsField2;
	public VBox topScreen;
	public TextField GameMessage1;
	public TextField GameMessage2;
	
	public boolean anteLock1 = false;
	public boolean anteLock2 = false;
	
	Controller(Player playerOne, Player playerTwo){
		anteTitle1 = new Text("Ante Wager");
		anteTitle1.setStyle("-fx-fill: white");
		anteField1 = new TextField("25");

		ppTitle1 = new Text("Pair Plus Wager");
		ppTitle1.setStyle("-fx-fill: white");
		ppField1 = new TextField("0");

		playTitle1 = new Text("Play Wager");
		playTitle1.setStyle("-fx-fill: white");
		playField1 = new TextField("$" +Integer.toString(playerOne.playBet));
		winningsTitle1 = new Text("Total Winnings");
		winningsTitle1.setStyle("-fx-fill: white");
		winningsField1 = new TextField("$" +Integer.toString(playerOne.totalWinnings));
		
		anteTitle2 = new Text("Ante Wager");
		anteTitle2.setStyle("-fx-fill: white");
		anteField2 = new TextField("25");

		ppTitle2 = new Text("Pair Plus Wager");
		ppTitle2.setStyle("-fx-fill: white");
		ppField2 = new TextField("0");
		
		playTitle2 = new Text("Play Wager");
		playTitle2.setStyle("-fx-fill: white");
		playField2 = new TextField("$" +Integer.toString(playerTwo.playBet));
		winningsTitle2 = new Text("Total Winnings");
		winningsTitle2.setStyle("-fx-fill: white");
		winningsField2 = new TextField("$" + Integer.toString(playerTwo.totalWinnings));
		topScreen = new VBox();
		GameMessage1 = new TextField();
		GameMessage2 = new TextField();
		
		
	}
	  
	public String run(Player player, int play, Dealer dealer) {
		String returnMessage = "";
		String winType = "";
		int ppWin = 0;
		if (play == 0) {
			player.totalWinnings -= (player.anteBet + player.pairPlusBet);
			winningsField1.setText("$" +Integer.toString(player.totalWinnings));
			returnMessage = "Player 1 folds: Player 1 loses ante wager and pair plus wager";
		}
		else {
			ppWin = ThreeCardLogic.evalPPWinnings(player.hand,player.pairPlusBet);
			
		}
		
		if ((play == 1) && (ppWin == 0)) {
			player.totalWinnings -= player.pairPlusBet;
			winningsField1.setText("$" +Integer.toString(player.totalWinnings));
			returnMessage += "Player 1 has lost their pair plus wager,";
		}
		else if ((play == 1) && (ppWin != 0)) {
			player.totalWinnings += ppWin;
			winningsField1.setText("$" +Integer.toString(player.totalWinnings));
			int handType = ThreeCardLogic.evalHand(player.hand);
			if (handType == 1) {
				winType = "Straight Flush";
			}
			else if (handType == 2) {
				winType = "Three of a Kind";
			}
			else if (handType == 3) {
				winType = "Straight";
			}
			else if (handType == 4) {
				winType = "Flush";
			}
			else if (handType == 5) {
				winType = "Pair";
			}
			returnMessage += "Player 1 has a " + winType + ", Player 1 has won their pair plus bet. ";
			
		}
		
		
		topScreen.getChildren().set(2, showCards(dealer.dealersHand));
		
		if(!dealer.queenHigh()) {
			if(play == 1) {
				returnMessage += " Dealer does not have at least Queen-High: Ante wager is pushed";
				player.playBet = 0;
				playField1.setText("$" +Integer.toString(player.playBet));
				anteLock1 = true;
			}
			
		}
		else {
			anteLock1 = false;
			if (play == 1) {
				int results = ThreeCardLogic.compareHands(dealer.dealersHand, player.hand);
				
				if (results == 2) {
					player.totalWinnings += (player.anteBet + player.playBet);
					winningsField1.setText("$" +Integer.toString(player.totalWinnings));
					returnMessage += " Player 1 has beat the dealer.";
				}
				else if (results == 1) {
					player.totalWinnings -= (player.anteBet + player.playBet);
					winningsField1.setText("$" +Integer.toString(player.totalWinnings));
					returnMessage += " Player 1 lost against the dealer.";
				}
				else {
					returnMessage += " Player 1 tied with the dealer.";
				}
			}
		}
		
		if(!anteLock1) {
			anteField1.setEditable(true);
		}
		ppField1.setEditable(true);
		
		return returnMessage;
	}
	
	
	
	public String run2(Player player, int play, Dealer dealer) {
		String returnMessage = "";
		String winType = "";
		int ppWin = 0;
		if (play == 0) {
			player.totalWinnings -= (player.anteBet + player.pairPlusBet);
			winningsField2.setText("$" +Integer.toString(player.totalWinnings));
			returnMessage = "Player 2 folds: Player 2 loses ante wager and pair plus wager";
		}
		else {
			ppWin = ThreeCardLogic.evalPPWinnings(player.hand,player.pairPlusBet);
			
		}
		
		if ((play == 1) && (ppWin == 0)) {
			player.totalWinnings -= player.pairPlusBet;
			winningsField2.setText("$" +Integer.toString(player.totalWinnings));
			returnMessage += "Player 2 has lost their pair plus wager,";
		}
		else if ((play == 1) && (ppWin != 0)) {
			player.totalWinnings += ppWin;
			winningsField2.setText("$" +Integer.toString(player.totalWinnings));
			int handType = ThreeCardLogic.evalHand(player.hand);
			if (handType == 1) {
				winType = "Straight Flush";
			}
			else if (handType == 2) {
				winType = "Three of a Kind";
			}
			else if (handType == 3) {
				winType = "Straight";
			}
			else if (handType == 4) {
				winType = "Flush";
			}
			else if (handType == 5) {
				winType = "Pair";
			}
			returnMessage += "Player 2 has a " + winType + ", Player 2 has won their pair plus bet. ";
			
		}
		
		
		
		topScreen.getChildren().set(2, showCards(dealer.dealersHand));
		
		
		if(!dealer.queenHigh()) {
			if(play == 1) {
				returnMessage += " Dealer does not have at least Queen-High: Ante wager is pushed";
				player.playBet = 0;
				playField2.setText("$" +Integer.toString(player.playBet));
				anteLock2 = true;
			}
			
		}
		else {
			anteLock2 = false;
			if (play == 1) {
				int results = ThreeCardLogic.compareHands(dealer.dealersHand, player.hand);
				
				if (results == 2) {
					player.totalWinnings += (player.anteBet + player.playBet);
					winningsField2.setText("$" +Integer.toString(player.totalWinnings));
					returnMessage += " Player 2 has beat the dealer.";
				}
				else if (results == 1) {
					player.totalWinnings -= (player.anteBet + player.playBet);
					winningsField2.setText("$" +Integer.toString(player.totalWinnings));
					returnMessage += " Player 2 lost against the dealer.";
				}
				else {
					returnMessage += " Player 2 tied with the dealer.";
				}
			}
		}
		
		if(!anteLock2) {
			anteField2.setEditable(true);
		}
		ppField2.setEditable(true);
		
		return returnMessage;
	}
	
	
	public String suitToString(char suit) {
		if (suit == 'C') {
			return "clubs";
		}
		else if(suit == 'D') {
			return "diamonds";
		}
		else if(suit == 'S') {
			return "spades";
		}
		else{
			return "hearts";
		}
	}
	
	public String valtoString(int num) {
		if (num == 11) {
			return "jack";
		}
		else if(num == 12) {
			return "queen";
		}
		else if(num == 13) {
			return "king";
		}
		else if (num == 14) {
			return "ace";
		}
		else {
			return Integer.toString(num);
		}
	}
	
	
	public HBox showCards(ArrayList<Card> hand) {
		HBox cardLayout = new HBox(10);
		String valueString;
		String suitString;

		for (int i = 0; i < 3; i++) {
			valueString = valtoString(hand.get(i).getValue());
			suitString = suitToString(hand.get(i).getSuit());
			Image card = new Image("PNG-cards-1.3/" + valueString + "_of_" + suitString + ".png");
			ImageView cardView = new ImageView(card);
			cardView.setFitWidth(50);
			cardView.setFitHeight(75);
			cardView.setPreserveRatio(false);
			cardLayout.getChildren().add(cardView);
		}

		cardLayout.setStyle("-fx-alignment: center;");
		return cardLayout;
	}
	
	
}

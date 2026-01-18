import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;
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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.animation.PauseTransition;
import javafx.util.Duration;


public class JavaFXTemplate extends Application {

    @FXML private TextField GameMessage1;
    @FXML private TextField GameMessage2;
    @FXML private Button dealButton;
    @FXML private Button playButton1;
    @FXML private Button foldButton1;
    @FXML private Button playButton2;
    @FXML private Button foldButton2;
    @FXML private TextField anteField1;
    @FXML private TextField ppField1;
    @FXML private TextField playField1;
    @FXML private TextField winningsField1;
    @FXML private TextField anteField2;
    @FXML private TextField ppField2;
    @FXML private TextField playField2;
    @FXML private TextField winningsField2;
    @FXML private MenuItem exit;
    @FXML private MenuItem freshStart;
    @FXML private MenuItem newLook;
    @FXML private HBox dealerCardLayout;
    @FXML private HBox playerCardLayout1;
    @FXML private HBox playerCardLayout2;
    @FXML private Button startButton;
    @FXML private Button exitButton;
    @FXML private StackPane welcomeScreen; 
    @FXML private BorderPane gameScreen; 
    @FXML private Button confirmButton;
    @FXML private Button returnButton;
    
   
	Player playerOne = new Player();
	Player playerTwo = new Player();
	Dealer theDealer = new Dealer();
	int playerResponse1 = -1;
	int playerResponse2 = -1;
	boolean screenMode = true;
	public boolean anteLock1 = false;
	public boolean anteLock2 = false;
	StackPane base = new StackPane();
	PauseTransition pausePlayers = new PauseTransition(Duration.seconds(2));
	PauseTransition pauseDealer = new PauseTransition(Duration.seconds(2));
	
    public static void main(String[] args) {
        launch(args);
    }
	
    @Override
    public void start(Stage primaryStage) throws Exception {
    	BorderPane gameBase = null;
    	StackPane welcome = null;
    	try {
    	   	FXMLLoader welcomeLoader = new FXMLLoader(getClass().getResource("WelcomeScreen.fxml"));
        	welcomeLoader.setController(this);
        	welcome = welcomeLoader.load();
    	}
    	catch (Exception error) {
    		System.out.println("Error loading file");
    	}
    	
		try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("GameLayout.fxml"));
	        loader.setController(this);
	        gameBase = loader.load();	
		}
    	catch (Exception error) {
    		System.out.println("Error loading file");
    	}
		
        base.getChildren().addAll(gameBase, welcome);
        
		Scene root = new Scene(base, 700, 700);
		root.getStylesheets().add(getClass().getResource("/styles/GameTable.css").toExternalForm());
		gameBase.setStyle("-fx-background-image: url('3CardPokerFront.jpg'); -fx-background-size: cover;");
		primaryStage.setTitle("3 Card Poker");
		primaryStage.setScene(root);
		primaryStage.show();
    	
    }
    

    @FXML
    public void initialize() {
    	startButton.setOnAction(e ->{
    		welcomeScreen.setVisible(false);
    		loadGameScreen();
    		
    	});
		exitButton.setOnAction(e -> {
			Platform.exit();
		});

    }
    
    public void loadGameScreen(){   
        dealButton.setOnAction(e -> dealCards());
        
        playButton1.setOnAction(e -> playerPlay1());
        foldButton1.setOnAction(e -> playerFold1());
        playButton2.setOnAction(e -> playerPlay2());
        foldButton2.setOnAction(e -> playerFold2());
        
        exit.setOnAction(e -> exitGame());
        freshStart.setOnAction(e -> freshStart());
        newLook.setOnAction(e -> newLook());
        
        playButton1.setDisable(true);
        foldButton1.setDisable(true);
        playButton2.setDisable(true);
        foldButton2.setDisable(true);
		playField1.setText("$" +Integer.toString(playerOne.playBet));
		playField2.setText("$" +Integer.toString(playerTwo.playBet));
		winningsField1.setText("$" +Integer.toString(playerOne.totalWinnings));
		winningsField2.setText("$" +Integer.toString(playerTwo.totalWinnings));
        dealerCardLayout.getChildren().add(cardBack());
        playerCardLayout1.getChildren().add(cardBack());
        playerCardLayout2.getChildren().add(cardBack());
        
        pausePlayers.setOnFinished(e -> {
            playerCardLayout1.getChildren().set(0,cardBack());
            playerCardLayout2.getChildren().set(0,cardBack());
            dealButton.setDisable(false);
        });
        
        pauseDealer.setOnFinished(e -> {
        	dealerCardLayout.getChildren().set(0, showCards(theDealer.dealersHand));
        	runAlgorithm();
        });
    }
    
    
    public void dealCards() {
    	dealerCardLayout.getChildren().set(0, cardBack());
        GameMessage1.setText("Player 1's Cards have been dealt.");
        GameMessage2.setText("Player 2's Cards have been dealt.");
        
		try {
			playerOne.anteBet = Integer.parseInt(anteField1.getText());
			playerOne.pairPlusBet = Integer.parseInt(ppField1.getText());
			
		}
		catch (NumberFormatException error) {
			GameMessage1.setText("Please enter valid ante/pair plus bet: (5 - 25) for ante bets and (0, 5 - 25) for pair plus bets (optional)");
		}
		
		try {
			playerTwo.anteBet = Integer.parseInt(anteField2.getText());
			playerTwo.pairPlusBet = Integer.parseInt(ppField2.getText());
			
		}
		catch (NumberFormatException error) {
			GameMessage2.setText("Please enter valid ante/pair plus bet: (5 - 25) for ante bets and (0, 5 - 25) for pair plus bets (optional)");
		}
		
	
		
		if (!validateAnteBet(playerOne.anteBet) || !validatePPBet(playerOne.pairPlusBet)) {
			GameMessage1.setText("Please enter valid ante/pair plus bet: (5 - 25) for ante bets and (0, 5 - 25) for pair plus bets (optional)");
		}
		else if (!validateAnteBet(playerTwo.anteBet) || !validatePPBet(playerTwo.pairPlusBet)){
			GameMessage1.setText("Waiting for other player to input valid values");
		}
		if (!validateAnteBet(playerTwo.anteBet) || !validatePPBet(playerTwo.pairPlusBet)) {
			GameMessage2.setText("Please enter valid ante/pair plus bet: (5 - 25) for ante bets and (0, 5 - 25) for pair plus bets (optional)");
		}
		else if (!validateAnteBet(playerOne.anteBet) || !validatePPBet(playerOne.pairPlusBet)) {
			GameMessage2.setText("Waiting for other player to input valid values");
		}
		if(validateAnteBet(playerOne.anteBet) && validatePPBet(playerOne.pairPlusBet) && validateAnteBet(playerTwo.anteBet) && validatePPBet(playerTwo.pairPlusBet)) {
			dealButton.setDisable(true);
	        playButton1.setDisable(false);
	        foldButton1.setDisable(false);
	        playButton2.setDisable(false);
	        foldButton2.setDisable(false);
	        
			theDealer.dealersHand = theDealer.dealHand();
			if (theDealer.theDeck.deckCount <= 34) {
				theDealer.theDeck = theDealer.theDeck.newDeck();
			}

			anteField1.setEditable(false);
			anteField2.setEditable(false);
			ppField1.setEditable(false);
			ppField2.setEditable(false);

			playerResponse1 = -1;
			playerResponse2 = -1;
			
			playerOne.playBet = 0;
			playerTwo.playBet = 0;
			playField1.setText("$" +Integer.toString(playerOne.playBet));
			playField2.setText("$" +Integer.toString(playerTwo.playBet));

			playerOne.hand = theDealer.dealHand();
			playerCardLayout1.getChildren().set(0, showCards(playerOne.hand));

			playerTwo.hand = theDealer.dealHand();
			playerCardLayout2.getChildren().set(0, showCards(playerTwo.hand));


		}
    }
    
	
	private String suitToString(char suit) {
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
	
	private String valtoString(int num) {
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
	
	
	private HBox showCards(ArrayList<Card> hand) {
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
    
    
    
	private boolean validatePPBet(int bet) {
		if (bet == 0) {
			return true;
		}
		else if ((bet >= 5) && (bet <= 25)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private boolean validateAnteBet(int bet) {
		if ((bet >= 5) && (bet <= 25)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private void runAlgorithm() {
		if((playerResponse1 != -1) && (playerResponse2 != -1)) {
			GameMessage1.setText(run(playerOne, playerResponse1, theDealer));
			GameMessage2.setText(run2(playerTwo, playerResponse2, theDealer));
		}
		pausePlayers.play();
	}

    
    private void playerPlay1() {
			GameMessage1.setText("Player 1 plays their hand");
			playerResponse1 = 1;
			playButton1.setDisable(true);
			foldButton1.setDisable(true);
			playerOne.playBet = playerOne.anteBet;
			playField1.setText("$" +Integer.toString(playerOne.playBet));
			pauseDealer.play();
    }
    
    private void playerFold1() {
		GameMessage1.setText("Player 1 folds: Player 1 loses ante wager and pair plus wager");
		playerResponse1 = 0;
		playButton1.setDisable(true);
		foldButton1.setDisable(true);
		pauseDealer.play();
    }
    
    private void playerPlay2() {
			GameMessage2.setText("Player 2 plays their hand");
			playerResponse2 = 1;
			playButton2.setDisable(true);
			foldButton2.setDisable(true);
			playerTwo.playBet = playerTwo.anteBet;
			playField2.setText("$" +Integer.toString(playerTwo.playBet));
			pauseDealer.play();
    }
    
    private void playerFold2() {
		GameMessage2.setText("Player 2 folds: Player 2 loses ante wager and pair plus wager");
		playerResponse2 = 0;
		playButton2.setDisable(true);
		foldButton2.setDisable(true);
		pauseDealer.play();
    }

    private void freshStart() {
    	BorderPane gameBase = null;
        playerOne = new Player();
        playerTwo = new Player();
        theDealer = new Dealer();
    	playerResponse1 = -1;
    	playerResponse2 = -1;
		try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("GameLayout.fxml"));
	        loader.setController(this);
	        gameBase = loader.load();	
		}
    	catch (Exception error) {
    		System.out.println("Error loading file");
    	}
		base.getChildren().set(0,gameBase);
		loadGameScreen();    	
    }

    private void newLook() {
    	screenMode = !screenMode;
    	if (screenMode) {
    		gameScreen.getStylesheets().add(getClass().getResource("/styles/GameTable.css").toExternalForm());
			gameScreen.setStyle("-fx-background-image: url('3CardPokerFront.jpg'); -fx-background-size: cover;");
    	}
    	else {
    		gameScreen.getStylesheets().clear();
    		gameScreen.getStylesheets().add(getClass().getResource("/styles/GameTable2.css").toExternalForm());
			gameScreen.setStyle("-fx-background-image: url('redBackground.jpg'); -fx-background-size: cover;");
    	}
 
    }
    
    private void exitGame() {
    	StackPane screenBase = null;
		try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("exitConfirm.fxml"));
	        loader.setController(this);
	        screenBase = loader.load();	
		}
    	catch (Exception error) {
    		System.out.println("Error loading file");
    	}
		base.getChildren().add(screenBase);
		confirmButton.setOnAction(e ->{
			Platform.exit();
		});
		returnButton.setOnAction(e ->{
			base.getChildren().remove(2);
		});
		
    }
    

    
	public HBox cardBack() {
		HBox cardLayout = new HBox(10);

		for (int i = 0; i < 3; i++) {
			Image card = new Image("CardBack.png");
			ImageView cardView = new ImageView(card);
			cardView.setFitWidth(50);
			cardView.setFitHeight(75);
			cardView.setPreserveRatio(false);
			cardLayout.getChildren().add(cardView);
		}

		cardLayout.setStyle("-fx-alignment: center;");
		return cardLayout;
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
	
	
}



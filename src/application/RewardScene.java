package application;

import java.io.IOException;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class RewardScene {

	static double scoreReward = 0;
	static boolean isScoreRewardInt = false;
	
	public static int numOfLists = 14;
	public static String[] dropDownArray = { "All (Practice Only)","Colours", "Days", "Days (Loan Words)", "Months", "Months (Loan Words)",
			"Babies", "Weather", "Compass Points", "Feelings", "Work", "Engineering", "Software", "University Life" };

	public static void getScore(double score) {
		scoreReward = score;
		isScoreRewardInt = ((scoreReward % 1) == 0) ? true : false;
	};
	
	private static void setButtonProperties(VBox vBox, Text text) {
		vBox.getChildren().add(text);
		text.setId("rewardSceneWordList");
	}

	public static Scene getRewardScene(Stage primaryStage){
		VBox rewardVBox = new VBox();
		BorderPane rewardBorderPane = new BorderPane();
		Scene rewardScene = new Scene(rewardBorderPane, 1200, 900);

		Label rewardLabel = new Label();
		rewardLabel.setId("mainRewardLabel");
		rewardLabel.setTextAlignment(TextAlignment.CENTER);
		rewardLabel.setPadding(new Insets(30, 10, 20, 10));
		
		String rewardMessage = "";
		
		//Using java ternary operators to define the message.
		if (scoreReward < 1.5) {
			rewardMessage = (isScoreRewardInt) ? ("Hang in there, you scored " + (int)scoreReward + ". Better luck next time!"):
				("Hang in there, your score was " + scoreReward + ". Better luck next time!");
		} else if (scoreReward < 2.5) {
			rewardMessage = (isScoreRewardInt) ? ("Not bad, you got " + (int)scoreReward + "/5. There's always next time!"):
				("Not bad, you got " + scoreReward + "/5. There's always next time!");
		} else if (scoreReward < 3.5) {
			rewardMessage = (isScoreRewardInt) ? ("Pretty good, you got " + (int)scoreReward + "/5 words correct! Keep pushing!"):
				("Pretty good, you got " + scoreReward + "/5 words correct! Keep pushing!");
		} else if (scoreReward < 4.5) {
			rewardMessage = (isScoreRewardInt) ? ("Excellent! You scored " + (int)scoreReward + "/5! So close to the top!"):
				("Excellent! You scored " + scoreReward + "/5! So close to the top!");
		} else if (scoreReward == 4.5) {
			rewardMessage = ("Tremendous, nearly there! " + scoreReward + "/5! You'll ace it next time!");
		} else {
			rewardMessage = ("Magnificent score " + (int)scoreReward + "/5! Can't get much better than this!");
		}
		
		rewardLabel.setText(rewardMessage);
		rewardLabel.setMaxWidth(750);
		rewardLabel.setWrapText(true);
		
		//The list of words spelt correctly and incorrectly.
		VBox wordsSpelt = new VBox();
		wordsSpelt.setAlignment(Pos.CENTER);
		Label correct = new Label("Words spelt correctly");
		correct.setPadding(new Insets(10, 10, 10, 10));
		correct.setId("rewardLabel");
		//Conditional to check if there was any words in here
		wordsSpelt.getChildren().add(correct);

		Boolean isAnyCorrect = false;
		Boolean isAnyIncorrect = false;
		List<String> wordList = NewQuiz.getWordList();
		Boolean[] wordListCorrect = NewQuiz.getWordListCorrect();
		
		for(int i = 0; i<5; i++) {
			if(wordListCorrect[i]) {
				Text word = new Text(wordList.get(i));
				setButtonProperties(wordsSpelt,word);
				isAnyCorrect = true;
			}
		}
		if(!isAnyCorrect) {
			Text word = new Text("There were no words spelt correctly :(");
			setButtonProperties(wordsSpelt,word);
		}
		
		Label incorrect = new Label("Words spelt incorrectly");
		incorrect.setPadding(new Insets(10, 10, 10, 10));
		incorrect.setId("rewardLabel");
		wordsSpelt.getChildren().add(incorrect);
		for(int i = 0; i<5; i++) {
			if(!wordListCorrect[i]) {
				Text word = new Text(wordList.get(i));
				setButtonProperties(wordsSpelt,word);
				isAnyIncorrect = true;
			}
		}
		if(!isAnyIncorrect) {
			Text word = new Text("There were no words spelt incorrectly :)");
			setButtonProperties(wordsSpelt,word);
		}
		
		//TIME TAKEN LABEL
		
		Label timeTaken = new Label();
		if(NewQuiz.getTime()/1000 > 60) {
			timeTaken.setText("Time taken was: " + (int)(NewQuiz.getTime()/60000)+ " minutes " + (int)((NewQuiz.getTime()/1000)-(NewQuiz.getTime()/60000)*60) + " seconds");
		} else {
			timeTaken.setText("Time taken was: " + (int)(NewQuiz.getTime()/1000)+" seconds");
		}
		timeTaken.setFont(Font.font("ubuntu", FontWeight.BOLD, FontPosture.REGULAR, 32));
		timeTaken.setPadding(new Insets(0, 0, 20, 0));
		
		// Creating the drop down box for selecting a word list
		final ComboBox<String> wordListDropDown = new ComboBox<String>();
		wordListDropDown.getItems().addAll(
            "Colours",
            "Days",
            "Days (Loan Words)",
            "Months",
            "Months (Loan Words)",
            "Babies",
            "Weather",
            "Compass Points",
            "Feelings", 
            "Work",
            "Engineering",
            "Software",
            "University Life"
        );   
		wordListDropDown.setPromptText("Please Choose A Spelling Topic");
		wordListDropDown.setValue(MainPage.spellingTopic);
		wordListDropDown.setMinSize(250, 60);
		
		Button playAgainButton = new Button("Play Again");
		Button mainMenuButton = new Button("Main Menu");
		Button quitButton = new Button("Quit Game");
		
		playAgainButton.setMinSize(250, 60);
		playAgainButton.setFont(new Font("ubuntu", 28));
		
		mainMenuButton.setMinSize(250, 60);
		mainMenuButton.setFont(new Font("ubuntu", 28));
		
		quitButton.setMinSize(250, 60);
		quitButton.setFont(new Font("ubuntu", 28));

		playAgainButton.setOnAction(actionEvent -> {
			try {
				if (wordListDropDown.getValue() == null) {
					
				} else {
					MainPage.spellingTopic = wordListDropDown.getValue();
					
					String spellingTopic = wordListDropDown.getValue();
					for (int i = 0; i < numOfLists; i++) {
						if (dropDownArray[i].equals(spellingTopic)) {
							String[] replayList = MainPage.topicArray.get(i).toArray(new String[0]);
							NewQuiz.changeWordList(MainPage.getRandomList(replayList));
						}
					}
					primaryStage.setScene(NewQuiz.getPracticeScene(primaryStage));
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		mainMenuButton.setOnAction(actionEvent -> {
			try {
				primaryStage.setScene(MainPage.getMainPageScene(primaryStage));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		
		quitButton.setOnAction(actionEvent -> {primaryStage.close();});
		
		rewardVBox.getChildren().addAll(rewardLabel, wordsSpelt, timeTaken, wordListDropDown, playAgainButton, mainMenuButton, quitButton);
		rewardVBox.setAlignment(Pos.CENTER);
		rewardVBox.setSpacing(15);
		
		rewardBorderPane.setLeft(rewardVBox);
		
		rewardScene.getStylesheets().add(RewardScene.class.getResource("application.css").toExternalForm());
		
		return rewardScene;
	}
}

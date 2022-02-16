package application;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class QuizScene {
	static boolean isNewQuiz = true;

	static double score = 0;
	static double speechSpeed = 1;
	static int quizNum = 0;

	static long endTime;
	static long startTime;

	static boolean isFirstTryWrong = false;
	static boolean isSecond = false;
	static boolean isSecondTryWrong = false;

	static Label scoreField;
	static Label secondLetterHint;
	static Label letterDisplay;

	/*
	 * static TextField textfield; static Button submit; static Button again; static
	 * Button skip;
	 */

	static List<String> wordList = new ArrayList<String>();
	static Boolean[] wordListCorrect = new Boolean[5];
	static String[] macronChar = new String[] { "ā", "ē", "ī", "ō", "ū" };

	/**
	 * This method is used to detect with mode should we operate
	 * 
	 * @param mode
	 */
	public static void setIsNewQuiz(boolean mode) {
		isNewQuiz = mode;
	};

	/**
	 * This method is used in the main menu and reward scene to tell the quiz scene
	 * the new spelling topic
	 * 
	 * @param array
	 */
	public static void changeWordList(String[] array) {
		// Clear the wordList before new List
		wordList = new ArrayList<String>();
		for (String word : array) {
			wordList.add(word);
		}
		Collections.shuffle(wordList);
	};

	/**
	 * Updates the score and letter hint depending on if the user gets their first
	 * or second try correct
	 * 
	 * @param isFirstTryWrong
	 */

	public static void ifWordIsCorrect(boolean isFirstTryWrong) {
		// Update the score
		if (isFirstTryWrong) {
			score = score + 0.5;
		} else {
			score++;
		}

		// Adding the status of correctness of the word spelt
		wordListCorrect[quizNum] = true;

		// Update the score label
		scoreField.setText("Score: " + String.valueOf(score));

		secondLetterHint.setText("Correct!");

		// Say the next word
		quizNum++;

		// Display the letter hints
		if (quizNum < wordList.size()) {
			Speaker.sayWord(wordList.get(quizNum), speechSpeed);
			String letterHint = LetterDisplay.getBlankSpaces(wordList.get(quizNum));
			letterHint = letterHint.replaceAll(".(?!$)", "$0 ");
			letterDisplay.setText(letterHint);
		}
	}

	public static Scene getPracticeScene(Stage primaryStage) throws IOException {
		
		// Update the SpeechSpeed

		// check if file exist
		if (Bash.runCommand("[ -s SpeechSpeed.txt ] && echo not empty || echo empty").equals("empty")) {
			Bash.runCommand("echo " + String.valueOf(speechSpeed) + " >SpeechSpeed.txt");
		} else {
			speechSpeed = Double.valueOf(Bash.runCommand("cat SpeechSpeed.txt"));
		}

		BorderPane practicePage = new BorderPane();
		Scene practiceScene = new Scene(practicePage, 1200, 900);

		// Top part is the spelling topic
		Label spellingTopicLabel = new Label(MainPage.spellingTopic);
		spellingTopicLabel.setFont(Font.font("ubuntu", FontWeight.BOLD, FontPosture.REGULAR, 60));
		spellingTopicLabel.setPadding(new Insets(30, 10, 10, 10));
		spellingTopicLabel.setMinWidth(500);
		spellingTopicLabel.setAlignment(Pos.CENTER);

		// Center part is the part that quiz game
		VBox quiz = new VBox();
		quiz.maxWidth(500);

		scoreField = new Label("Score: 0");
		scoreField.setFont(Font.font("ubuntu", FontWeight.BOLD, FontPosture.REGULAR, 40));

		letterDisplay = new Label();
		letterDisplay.setFont(Font.font("ubuntu", FontWeight.BOLD, FontPosture.REGULAR, 40));
		letterDisplay.setTextAlignment(TextAlignment.CENTER);

		secondLetterHint = new Label();
		secondLetterHint.setFont(Font.font("ubuntu", FontWeight.BOLD, FontPosture.REGULAR, 30));
		secondLetterHint.setAlignment(Pos.CENTER);
		secondLetterHint.setTextAlignment(TextAlignment.CENTER);
		secondLetterHint.setWrapText(true);

		secondLetterHint.setMaxWidth(500);
		secondLetterHint.setMinHeight(75);

		Button mainMenuButton = new Button("Return To Main Menu");
		mainMenuButton.setMinSize(230, 40);
		mainMenuButton.setId("mainMenuButton");
		BorderPane.setAlignment(mainMenuButton, Pos.TOP_LEFT);

		mainMenuButton.setOnAction(actionEvent -> {
			try {
				quizNum = 0;
				score = 0;
				isFirstTryWrong = false;
				primaryStage.setScene(MainPage.getMainPageScene(primaryStage));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		Button startQuizButton = new Button("Click Here To Start");
		startQuizButton.setMinSize(230, 40);
		startQuizButton.setId("startQuizButton");

		Label macronTips = new Label("Tip: You can also type - before a vowel to add a macron, e.g. -e = ē");
		macronTips.setWrapText(true);
		macronTips.setMaxWidth(500);
		macronTips.setMinHeight(60);
		macronTips.setFont(Font.font("ubuntu", FontWeight.BOLD, FontPosture.REGULAR, 25));
		macronTips.setTextAlignment(TextAlignment.CENTER);

		TextField textfield = new TextField();
		textfield.setFont(Font.font("ubuntu", FontWeight.BOLD, FontPosture.REGULAR, 30));
		textfield.setMaxWidth(500);

		
		//TESTING HAVING A SUB BUTTON
		Label mainSubmit = new Label("        Submit Word     ");
		Label tipSubmit = new Label("(?)");
		mainSubmit.setId("buttonText");
		tipSubmit.setId("buttonText");
		HBox submitBox = new HBox();
		submitBox.getChildren().addAll(mainSubmit, tipSubmit);
		Button submit = new Button();
		submit.setGraphic(submitBox);
		Tooltip submitToolTip = new Tooltip("Tip: you can press \"Enter\" to submit the word");
//		submit.setTooltip(submitToolTip);
		submit.setMinSize(230, 40);
		tipSubmit.setTooltip(submitToolTip);
		
		Label mainAgain = new Label("         Replay Word     ");
		Label tipAgain = new Label("(?)");
		mainAgain.setId("buttonText");
		tipAgain.setId("buttonText");
		HBox againBox = new HBox();
		againBox.getChildren().addAll(mainAgain, tipAgain);
		Button again = new Button();
		again.setGraphic(againBox);
		Tooltip againToolTip = new Tooltip("Tip: you can press \"Control\" to replay the word");
//		again.setTooltip(againToolTip);
		again.setMinSize(230, 40);
		tipAgain.setTooltip(againToolTip);
		
		Button skip = new Button("Skip Word");
		skip.setMinSize(230, 40);

		// Replacing all vowels with dash before them to vowels with macron
		textfield.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.contains("-a") || newValue.contains("-e") || newValue.contains("-i") || newValue.contains("-o")
					|| newValue.contains("-u")) {
				textfield.setText(textfield.getText().replace("-a", "ā"));
				textfield.setText(textfield.getText().replace("-e", "ē"));
				textfield.setText(textfield.getText().replace("-i", "ī"));
				textfield.setText(textfield.getText().replace("-o", "ō"));
				textfield.setText(textfield.getText().replace("-u", "ū"));
			}
		});

		if (isNewQuiz) {
			NewQuiz.setTextField(textfield, submit, again);
			NewQuiz.setSubmit(textfield, submit, primaryStage, startQuizButton);
			NewQuiz.setSkip(textfield, skip, primaryStage, startQuizButton);
		} else {
			Practice.setTextField(textfield, submit, again, skip);
			Practice.setSubmit(textfield, submit, again, skip);
			Practice.setSkip(textfield, submit, again, skip);
		}

		// The say word again button just calls the function
		// that uses festival and says the word in the
		// current index
		again.setOnAction(actionEvent -> {
			
			again.setDisable(true);
			Speaker.sayWord(wordList.get(quizNum), speechSpeed);
			Platform.runLater(() -> again.setDisable(false));
			textfield.requestFocus();
			textfield.selectEnd();
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
			}
		});

		// Creating the macron buttons that append the selected
		// macron to the end of the text field
		HBox macron = new HBox();
		Button[] macronButtons = new Button[5];
		for (int i = 0; i < 5; i++) {
			String macronCurrent = macronChar[i];
			macronButtons[i] = new Button(macronCurrent);
			macronButtons[i].setMinSize(40, 40);
			macronButtons[i].setOnAction(actionEvent -> {
				textfield.setText(textfield.getText() + macronCurrent);
				textfield.requestFocus();
				textfield.selectEnd();
			});
			macron.getChildren().add(macronButtons[i]);
		}

		macron.setAlignment(Pos.CENTER);
		macron.setSpacing(12);

		startQuizButton.setOnAction(actionEvent -> {
			// Say the first word
			startTime = System.currentTimeMillis();
			Speaker.sayWord(wordList.get(quizNum), speechSpeed);
			
			// Remove the start quiz button
			startQuizButton.setVisible(false);

			// Make the other buttons visible
			// and display the number of letters
			// of the first word
			for (Button button : macronButtons) {
				button.setVisible(true);
			}

			macronTips.setVisible(true);
			textfield.setVisible(true);
			submit.setVisible(true);
			again.setVisible(true);
			skip.setVisible(true);
			textfield.requestFocus();
			String letterHint = LetterDisplay.getBlankSpaces(wordList.get(0));
			letterHint = letterHint.replaceAll(".(?!$)", "$0 ");
			letterDisplay.setText(letterHint);
		});

		// Set buttons other than the start button
		// to be not visible before the quiz start
		for (Button button : macronButtons) {
			button.setVisible(false);
		}
		macronTips.setVisible(false);
		textfield.setVisible(false);
		submit.setVisible(false);
		again.setVisible(false);
		skip.setVisible(false);

		quiz.getChildren().addAll(spellingTopicLabel, mainMenuButton, scoreField, letterDisplay, secondLetterHint,
				startQuizButton, macron, macronTips, textfield, submit, again, skip,
				SpeechSpeed.speedButtons(speechSpeed, textfield));
		quiz.setPadding(new Insets(10, 10, 10, 120));
		quiz.setAlignment(Pos.CENTER);
		quiz.setSpacing(12);
		practicePage.setLeft(quiz);

		practicePage.getStylesheets().add(QuizScene.class.getResource("application.css").toExternalForm());
		return practiceScene;
	}
}

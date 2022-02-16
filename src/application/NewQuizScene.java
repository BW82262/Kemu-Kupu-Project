package application;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

public class NewQuizScene {
	static double score = 0;
	static double speechSpeed = 1;
	static int quizNum = 0;
	
	static boolean isFirstTryWrong = false;
	static boolean isSecond = false;
	
	static Label scoreField;
	static Label secondLetterHint;
	static Label letterDisplay;
	
	static long endTime;
	static long startTime;
	
	static String[] wordList = null;
	static Boolean[] wordListCorrect = new Boolean[5];
	
	/**
	 * This method is used by all scenes containing
	 * the speech speed buttons so that the speed
	 * is carried through between scenes when it
	 * is changed by the user
	 * @param speed
	 */
	public static void getSpeed(double speed) {
		speechSpeed = speed;
	};

	/**
	 * This method is used in the main menu
	 * and reward scene to tell the quiz scene
	 * the new spelling topic
	 * @param array
	 */
	public static void changeWordList(String[] array) {
		wordList = array;
	};

	/**
	 * Takes a string representing a bash
	 * command as input and runs that command
	 * using the java process 
	 * @param cmd
	 * @return
	 * @throws IOException
	 */
	public static String runBash(String cmd) throws IOException {

		ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
		try {
			Process process = builder.start();
			InputStream stdout = process.getInputStream();
			BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
			String line = null;
			while ((line = stdoutBuffered.readLine()) != null) {
				return line;
			}
			process.waitFor(); 
			process.destroy();
			stdoutBuffered.close();
		} catch (Exception e) { 
			e.printStackTrace();
		}
		return null;
	}

	public static void sayWord(String word) {
		
		// Use Duration_Stretch which changes the duration time of the wav.file
		double duration = 1 / speechSpeed;
		word = word.replaceAll("-", " ");
		try {
			// Create a new temp file and rewrite the old one
			runBash("echo '(voice_akl_mi_pk06_cg)' >tempSound.scm");
			// Should be echo '(Parameter.set '\''Duration_Stretch 1.0)'>>temp.scm in
			// terminal
			// Should create a line (Parameter.set 'Duration_Stretch 1.0) in temp file
			runBash("echo '(Parameter.set '\\''Duration_Stretch " + String.format("%.1f", duration)
			+ ")'>>tempSound.scm");
			runBash("echo '(SayText \"" + word + "\")' >>tempSound.scm");

			runBash("festival -b tempSound.scm");

			runBash("rm tempSound.scm");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void updateSpeed() {
		// Update the speed in the speech speed file
		try {
			runBash("echo " + String.valueOf(speechSpeed) + " >SpeechSpeed.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Updates the score and letter hint depending
	 * on if the user gets their first or second
	 * try correct
	 * @param isFirstTryWrong
	 */
	public static void ifWordIsCorrect(boolean isFirstTryWrong) {
		// Update the score
		if (isFirstTryWrong) {
			score = score + 0.5;
		} else {
			score++;
		}
		
		//Adding the status of correctness of the word spelt
		wordListCorrect[quizNum] = true;

		// Update the score label
		scoreField.setText("Score: " + String.valueOf(score));

		secondLetterHint.setText("Correct!");

		// Say the next word
		quizNum++;

		// Display the letter hints
		if (quizNum < 5) {
			sayWord(wordList[quizNum]);
			String letterHint = LetterDisplay.getBlankSpaces(wordList[quizNum]);
			letterHint = letterHint.replaceAll(".(?!$)", "$0 ");
			letterDisplay.setText(letterHint);
		}
	}

	public static void toReward(Stage primaryStage, Node startQuizButton) {
		if (quizNum > 4) {
			RewardScene.getScore(score);
			primaryStage.setScene(RewardScene.getRewardScene(primaryStage));
			quizNum = 0;
			score = 0;
			isFirstTryWrong = false;
			startQuizButton.setVisible(true);
		}
		endTime = System.currentTimeMillis();
	};
	
	public static Boolean[] getWordListCorrect() {
		return wordListCorrect;
	}
	
	public static String[] getWordList() {
		return wordList;
	}
	
	public static long getTime() {
		return (endTime - startTime);
	}
	
	private static void setButtonProperties(Button button) {
		button.setFont(Font.font("ubuntu", FontWeight.BOLD, FontPosture.REGULAR, 20));
		button.setMinSize(230, 40);
	}
	
	public static Scene getQuizScene(Stage primaryStage) throws IOException {
		BorderPane quizPage = new BorderPane();
		Scene quizScene = new Scene(quizPage, 1200, 900);

		// Top part is the label
//		Label title = new Label("Kēmu Kupu");
//		title.setFont(Font.font("ubuntu", FontWeight.BOLD, FontPosture.REGULAR, 60));
//		title.setPadding(new Insets(30, 10, 10, 10));
//		quizPage.setTop(title);
//		BorderPane.setAlignment(title, Pos.CENTER);

		// Center part is the part that quiz game
		VBox quiz = new VBox();

		// Displaying the spelling topic
		Label spellingTopicLabel = new Label(MainPage.spellingTopic);
		spellingTopicLabel.setFont(Font.font("ubuntu", FontWeight.BOLD, FontPosture.REGULAR, 60));

		scoreField = new Label("Score: 0");
		scoreField.setFont(Font.font("ubuntu", FontWeight.BOLD, FontPosture.REGULAR, 40));

		letterDisplay = new Label();
		letterDisplay.setFont(Font.font("ubuntu", FontWeight.BOLD, FontPosture.REGULAR, 40));
		letterDisplay.setTextAlignment(TextAlignment.CENTER);
		
		secondLetterHint = new Label();
		secondLetterHint.setFont(Font.font("ubuntu", FontWeight.BOLD, FontPosture.REGULAR, 30));
		secondLetterHint.setTextAlignment(TextAlignment.CENTER);
		
		Button mainMenuButton = new Button("Return To Main Menu");
		mainMenuButton.setFont(Font.font("ubuntu", FontWeight.BOLD, FontPosture.REGULAR, 20));
		mainMenuButton.setMinSize(230, 40);
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
		startQuizButton.setPadding(new Insets(12,12,12,12));
		
		Label macronTips = new Label("Tip: You can also type * before a vowel to add a macron, e.g. *e = ē");
		macronTips.setFont(Font.font("ubuntu", FontWeight.BOLD, FontPosture.REGULAR, 25));
		macronTips.setPadding(new Insets(10, 10, 10, 10));

		TextField textfield = new TextField();
		textfield.setFont(Font.font("ubuntu", FontWeight.BOLD, FontPosture.REGULAR, 30));
		textfield.setMaxWidth(500);
		
		Button submit = new Button("Submit Word");
		setButtonProperties(submit);

		Button again = new Button("Replay Word");
		setButtonProperties(again);

		Button skip = new Button("Skip Word");
		setButtonProperties(skip);

		// Change vowel with a star to a macron
		textfield.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.contains("*a") || newValue.contains("*e") || newValue.contains("*i") || newValue.contains("*o")
					|| newValue.contains("*u")) {
				textfield.setText(textfield.getText().replace("*a", "ā"));
				textfield.setText(textfield.getText().replace("*e", "ē"));
				textfield.setText(textfield.getText().replace("*i", "ī"));
				textfield.setText(textfield.getText().replace("*o", "ō"));
				textfield.setText(textfield.getText().replace("*u", "ū"));
			}
		});

		textfield.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.ENTER)  {
					submit.fire();
				}
			}
		});

		// Checking if the word in the text field when the
		// submit button is hit is the same as the word
		// in the current index of the word array
		submit.setOnAction(actionEvent -> {
			// Only check for correct spelling if the user
			// enters at least 1 character
			if (!textfield.getText().replace(" ", "").isEmpty()) {
				// First try correct
				if (textfield.getText().toUpperCase().equals(wordList[quizNum].toUpperCase()) && !isSecond) {
					ifWordIsCorrect(isFirstTryWrong);
					isSecond = false;

				// First try wrong
				} else if (!isFirstTryWrong) {
					sayWord(wordList[quizNum]);

					//letterDisplay.setText(LetterDisplay.replaceBlankSpaces(letterDisplay.getText(), wordList[quizNum].charAt(1), 1));
					String letterHint = LetterDisplay.practiseModuleHint(wordList[quizNum], LetterDisplay.getBlankSpaces(wordList[quizNum]));
					letterHint = letterHint.replaceAll(".(?!$)", "$0 ");
					letterDisplay.setText(letterHint);
					secondLetterHint.setText("Incorrect, please try again!");
					
					isFirstTryWrong = true;
					isSecond = true;

				// Second try
				} else if (isFirstTryWrong) {

					// Second try correct
					if (textfield.getText().toUpperCase().equals(wordList[quizNum].toUpperCase())) {
						ifWordIsCorrect(isFirstTryWrong);

					// Second try wrong
					} else {
						secondLetterHint.setText("Unlucky, incorrect, you will now move on to the next word!");
						wordListCorrect[quizNum] = false;
						quizNum++;

						if (quizNum < 5) {
							sayWord(wordList[quizNum]);
							String letterHint = LetterDisplay.getBlankSpaces(wordList[quizNum]);
							letterHint = letterHint.replaceAll(".(?!$)", "$0 ");
							letterDisplay.setText(letterHint);
						}

					}
					isFirstTryWrong = false;
					isSecond = false;

				}
				
				toReward(primaryStage, startQuizButton);
			} else {
				secondLetterHint.setText(
						"Your text field was empty. Type something and submit again!");
			}

			textfield.clear();
			textfield.requestFocus();
			textfield.selectEnd();
		});

		// The say word again button just calls the function
		// that uses festival and says the word in the
		// current index
		again.setOnAction(actionEvent -> {
			again.setDisable(true);
			sayWord(wordList[quizNum]);
			try {
				Thread.sleep(1000);
			} catch (Exception e) {

			}
			Platform.runLater(()->again.setDisable(false));
			textfield.requestFocus();
			textfield.selectEnd();
		});
		
		// The skip word button increases the index of the
		// word array and then says the word
		skip.setOnAction(actionEvent -> {
			skip.setDisable(true);
			wordListCorrect[quizNum] = false;
			quizNum++;
			secondLetterHint.setText(null);
			isSecond=false;
			isFirstTryWrong=false;
			textfield.clear();
			if (quizNum < 5) {
				sayWord(wordList[quizNum]);
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
				}
				Platform.runLater(()->skip.setDisable(false));
				String letterHint = LetterDisplay.getBlankSpaces(wordList[quizNum]);
				letterHint = letterHint.replaceAll(".(?!$)", "$0 ");
				letterDisplay.setText(letterHint);
			}

			toReward(primaryStage, startQuizButton);
			textfield.requestFocus();
			textfield.selectEnd();
		});

		// Creating the macron buttons that append the selected
		// macron to the end of the text field
		HBox macron = new HBox();

		Button macronA = new Button("ā");
		macronA.setMinSize(40, 40);

		Button macronE = new Button("ē");
		macronE.setMinSize(40, 40);

		Button macronI = new Button("ī");
		macronI.setMinSize(40, 40);

		Button macronO = new Button("ō");
		macronO.setMinSize(40, 40);

		Button macronU = new Button("ū");
		macronU.setMinSize(40, 40);

		macron.getChildren().addAll(macronA, macronE, macronI, macronO, macronU);
		macron.setAlignment(Pos.CENTER);
		macron.setSpacing(12);
		
		macronA.setOnAction(actionEvent -> {
			textfield.setText(textfield.getText() + "ā");
			textfield.requestFocus();
			textfield.selectEnd();
		});
		
		macronE.setOnAction(actionEvent -> {
			textfield.setText(textfield.getText() + "ē");
			textfield.requestFocus();
			textfield.selectEnd();
		});
		
		macronI.setOnAction(actionEvent -> {
			textfield.setText(textfield.getText() + "ī");
			textfield.requestFocus();
			textfield.selectEnd();
		});
		
		macronO.setOnAction(actionEvent -> {
			textfield.setText(textfield.getText() + "ō");
			textfield.requestFocus();
			textfield.selectEnd();
		});
		
		macronU.setOnAction(actionEvent -> {
			textfield.setText(textfield.getText() + "ū");
			textfield.requestFocus();
			textfield.selectEnd();
		});
		
		
		startQuizButton.setOnAction(actionEvent -> {
			// Say the first word
			sayWord(wordList[quizNum]);
			
			startTime = System.currentTimeMillis();
			
			// Remove the start quiz button
			startQuizButton.setVisible(false);
			
			// Make the other buttons visible
			// and display the number of letters
			// of the first word
			macronA.setVisible(true);
			macronE.setVisible(true);
			macronI.setVisible(true);
			macronO.setVisible(true);
			macronU.setVisible(true);
			macronTips.setVisible(true);
			textfield.setVisible(true);
			submit.setVisible(true);
			again.setVisible(true);
			skip.setVisible(true);
			textfield.requestFocus();
			String letterHint = LetterDisplay.getBlankSpaces(wordList[0]);
			letterHint = letterHint.replaceAll(".(?!$)", "$0 ");
			letterDisplay.setText(letterHint);
		});

		// The bottom left part is the speed section
//		Label speedLabel = new Label("Speech Speed");
		Label showSpeed = new Label("Speech Speed: " + String.valueOf(speechSpeed));
		Button speed05 = new Button("0.50");
		Button speed075 = new Button("0.75");
		Button speed1 = new Button("1.00");
		Button speed125 = new Button("1.25");
		Button speed15 = new Button("1.50");
		Button speed175 = new Button("1.75");
		Button speed2 = new Button("2.00");
		HBox hbox = new HBox(5);
		VBox speedVbox = new VBox(10);

		showSpeed.setId("showSpeed");
		speed05.setId("speed");
		speed075.setId("speed");
		speed1.setId("speed");
		speed125.setId("speed");
		speed15.setId("speed");
		speed175.setId("speed");
		speed2.setId("speed");

		speed05.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				speechSpeed = 0.5;
				// Rounding the number to 1 decimal
				showSpeed.setText("Speed: " + String.valueOf(speechSpeed));;
				updateSpeed();
				textfield.requestFocus();
				textfield.selectEnd();

			}
		});

		speed075.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				speechSpeed = 0.75;
				// Rounding the number to 1 decimal
				showSpeed.setText("Speed: " + String.valueOf(speechSpeed));;
				updateSpeed();
				textfield.requestFocus();
				textfield.selectEnd();

			}
		});

		speed1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				speechSpeed = 1;
				// Rounding the number to 1 decimal
				showSpeed.setText("Speed: " + String.valueOf(speechSpeed));
				updateSpeed();
				textfield.requestFocus();
				textfield.selectEnd();

			}
		});

		speed125.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				speechSpeed = 1.25;
				// Rounding the number to 1 decimal
				showSpeed.setText("Speed: " + String.valueOf(speechSpeed));
				updateSpeed();
				textfield.requestFocus();
				textfield.selectEnd();

			}
		});

		speed15.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				speechSpeed = 1.5;
				// Rounding the number to 1 decimal
				showSpeed.setText("Speed: " + String.valueOf(speechSpeed));
				updateSpeed();
				textfield.requestFocus();
				textfield.selectEnd();

			}
		});

		speed175.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				speechSpeed = 1.75;
				// Rounding the number to 1 decimal
				showSpeed.setText("Speed: " + String.valueOf(speechSpeed));
				updateSpeed();
				textfield.requestFocus();
				textfield.selectEnd();

			}
		});

		speed2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				speechSpeed = 2;
				// Rounding the number to 1 decimal
				showSpeed.setText("Speed: " + String.valueOf(speechSpeed));
				updateSpeed();
				textfield.requestFocus();
				textfield.selectEnd();

			}
		});

		// Set buttons other than the start button
		// to be not visible before the quiz start
		macronA.setVisible(false);
		macronE.setVisible(false);
		macronI.setVisible(false);
		macronO.setVisible(false);
		macronU.setVisible(false);
		macronTips.setVisible(false);
		textfield.setVisible(false);
		submit.setVisible(false);
		again.setVisible(false);
		skip.setVisible(false);
				
		hbox.getChildren().addAll(speed05,speed075,speed1,speed125,speed15,speed175,speed2);
		hbox.setAlignment(Pos.CENTER);

		speedVbox.getChildren().addAll(showSpeed,hbox);

		speedVbox.setAlignment(Pos.CENTER);
		speedVbox.setPadding(new Insets(10, 10, 50, 10));

		quiz.getChildren().addAll(spellingTopicLabel, mainMenuButton, scoreField, letterDisplay, secondLetterHint, startQuizButton, 
				macron,macronTips, textfield, submit, again, skip);
		quiz.setPadding(new Insets(10, 10, 10, 10));
		quiz.setAlignment(Pos.CENTER);
		quiz.setSpacing(12);
		quizPage.setCenter(quiz);
		
		quizPage.setBottom(speedVbox);
		BorderPane.setAlignment(speedVbox, Pos.CENTER);
		
		Arrays.fill(wordListCorrect, null);

		quizPage.getStylesheets().add(NewQuizScene.class.getResource("application.css").toExternalForm());

		return quizScene;
	}
}

package application;

import java.util.List;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class NewQuiz extends QuizScene {


	public static Boolean[] getWordListCorrect() {
		return wordListCorrect;
	}

	public static List<String> getWordList() {
		return wordList;
	}

	public static long getTime() {
		return (endTime - startTime);
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

	public static void setTextField(TextField textfield, Button submit, Button again) {
		textfield.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.ENTER) {
					submit.fire();
				}
				if (keyEvent.getCode() == KeyCode.CONTROL) {
					again.fire();
				}

			}
		});
	}

	public static void setSubmit(TextField textfield, Button submit, Stage primaryStage, Button startQuizButton) {
		submit.setOnAction(actionEvent -> {
			// Only check for correct spelling if the user
			// enters at least 1 character
			if (!textfield.getText().replace(" ", "").isEmpty()) {
				// First try correct
				if (textfield.getText().toUpperCase().equals(wordList.get(quizNum).toUpperCase()) && !isSecond) {
					ifWordIsCorrect(isFirstTryWrong);
					isSecond = false;

					// First try wrong
				} else if (!isFirstTryWrong) {
					Speaker.sayWord(wordList.get(quizNum), speechSpeed);

					// letterDisplay.setText(LetterDisplay.replaceBlankSpaces(letterDisplay.getText(),
					// wordList[quizNum].charAt(1), 1));
					String letterHint = LetterDisplay.newQuizModuleHint(wordList.get(quizNum),
							LetterDisplay.getBlankSpaces(wordList.get(quizNum)));
					letterHint = letterHint.replaceAll(".(?!$)", "$0 ");
					letterDisplay.setText(letterHint);
					secondLetterHint.setText("Incorrect, please try again!");

					isFirstTryWrong = true;
					isSecond = true;

					// Second try
				} else if (isFirstTryWrong) {

					// Second try correct
					if (textfield.getText().toUpperCase().equals(wordList.get(quizNum).toUpperCase())) {
						ifWordIsCorrect(isFirstTryWrong);

						// Second try wrong
					} else {
						secondLetterHint.setText("Unlucky, incorrect, you will now move on to the next word!");
						wordListCorrect[quizNum] = false;
						
						quizNum++;

						if (quizNum < 5) {
							Speaker.sayWord(wordList.get(quizNum), speechSpeed);
							String letterHint = LetterDisplay.getBlankSpaces(wordList.get(quizNum));
							letterHint = letterHint.replaceAll(".(?!$)", "$0 ");
							letterDisplay.setText(letterHint);
						}

					}
					isFirstTryWrong = false;
					isSecond = false;

				}

				toReward(primaryStage, startQuizButton);
			} else {
				secondLetterHint.setText("Your text field was empty. Type something and submit again!");
			}

			textfield.clear();
			textfield.requestFocus();
			textfield.selectEnd();
		});
	}

	public static void setSkip(TextField textfield, Button skip, Stage primaryStage, Button startQuizButton) {
		skip.setOnAction(actionEvent -> {
			skip.setDisable(true);
			wordListCorrect[quizNum] = false;
			quizNum++;
			secondLetterHint.setText(null);
			isSecond = false;
			isFirstTryWrong = false;
			textfield.clear();
			if (quizNum < 5) {
				Speaker.sayWord(wordList.get(quizNum),speechSpeed);
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
				}
				Platform.runLater(() -> skip.setDisable(false));
				String letterHint = LetterDisplay.getBlankSpaces(wordList.get(quizNum));
				letterHint = letterHint.replaceAll(".(?!$)", "$0 ");
				letterDisplay.setText(letterHint);
			}

			toReward(primaryStage, startQuizButton);
			textfield.requestFocus();
			textfield.selectEnd();
		});
	}
}

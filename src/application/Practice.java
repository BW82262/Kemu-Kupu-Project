package application;


import java.util.Collections;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


public class Practice extends QuizScene {
	
	public static void shuffle() {
		if (quizNum > wordList.size() - 1) {
			Collections.shuffle(wordList);
			quizNum = 0;
			isFirstTryWrong = false;
			Speaker.sayWord(wordList.get(0), speechSpeed);
		}
	};

	public static void setTextField(TextField textfield, Button submit, Button again, Button skip) {
		

		textfield.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {

				if (keyEvent.getCode() == KeyCode.ENTER && !submit.isDisable()) {
					submit.fire();
				} else if (keyEvent.getCode() == KeyCode.ENTER && isSecondTryWrong) {
					isSecondTryWrong = false;
					Speaker.sayWord(wordList.get(quizNum), speechSpeed);
					String letterHint = LetterDisplay.getBlankSpaces(wordList.get(quizNum));
					letterHint = letterHint.replaceAll(".(?!$)", "$0 ");
					letterDisplay.setText(letterHint);
					secondLetterHint.setText("");
					submit.setDisable(false);
					again.setDisable(false);
					skip.setDisable(false);
					textfield.clear();
				} else if (keyEvent.getCode() == KeyCode.CONTROL) {
					again.fire();
				}
			}
		});
		
	};

	public static void setSubmit(TextField textfield, Button submit, Button again, Button skip) {

		// Checking if the word in the text field when the
		// submit button is hit is the same as the word
		// in the current index of the word array
		submit.setOnAction(actionEvent -> {
			// Only check for correct spelling if the user
			// enters at least 1 character
			System.out.print(isSecond);
			if (!textfield.getText().replace(" ", "").isEmpty()) {
				// First try correct
				if (textfield.getText().toUpperCase().equals(wordList.get(quizNum).toUpperCase()) && !isSecond) {
					ifWordIsCorrect(isFirstTryWrong);

					// First try wrong
				} else if (!isFirstTryWrong) {
					Speaker.sayWord(wordList.get(quizNum), speechSpeed);

					// letterDisplay.setText(LetterDisplay.replaceBlankSpaces(letterDisplay.getText(),
					// wordList[quizNum].charAt(1), 1));
					String letterHint = LetterDisplay.practiseModuleHint(wordList.get(quizNum),
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
						secondLetterHint.setText("Unlucky, incorrect, press enter to move on to the next word!");

						isSecondTryWrong = true;
						// give full hints and let user go to next word
						letterDisplay.setText(wordList.get(quizNum).replaceAll(".(?!$)", "$0 "));
						submit.setDisable(true);
						again.setDisable(true);
						skip.setDisable(true);
						quizNum++;

					}
					isFirstTryWrong = false;
					isSecond = false;

				}

				shuffle();
			} else {
				secondLetterHint.setText("Your text field was empty. Type something and submit again!");
			}

			textfield.clear();
			textfield.requestFocus();
			textfield.selectEnd();
		});
		

	}


	public static void setSkip(TextField textfield, Button submit, Button again, Button skip) {

		// The skip word button increases the index of the
		// word array and then says the word
		skip.setOnAction(actionEvent -> {
			submit.setDisable(true);
			again.setDisable(true);
			skip.setDisable(true);
			isSecondTryWrong = true;
			// give full hints and let user go to next word
			secondLetterHint.setText("Press enter to move on to the next word!");
			letterDisplay.setText(wordList.get(quizNum));
			quizNum++;
			// reset the states
			isSecond = false;
			isFirstTryWrong = false;
			textfield.clear();
			shuffle();
			textfield.requestFocus();
			textfield.selectEnd();
		});
		

	}
}

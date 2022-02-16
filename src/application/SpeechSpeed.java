package application;

import java.io.IOException;
import java.text.DecimalFormat;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SpeechSpeed extends QuizScene{
	/**
	 * Updating the speed in the speech speed file whenever the user changes the
	 * speed in the program so that the speed is remembered when the program is
	 * closed
	 */
	public static void updateSpeed() {
		try {
			Bash.runCommand("echo " + String.valueOf(speechSpeed) + " >SpeechSpeed.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static VBox speedButtons(double speed, TextField textfield) {
		// The bottom left part is the speed section
		Label showSpeed = new Label("Speech Speed: " + String.valueOf(speed));
		showSpeed.setId("showSpeed");
		HBox hbox = new HBox(5);
		VBox speedVbox = new VBox(10);

		// Set up speed buttons
		for (int i = 0; i < 7; i++) {
			double speedOnButton = 0.5 + 0.25 * i;
			DecimalFormat df = new DecimalFormat("#0.00");
			Button speedButton = new Button(df.format(speedOnButton));
			speedButton.setId("speed");
			speedButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					
					speechSpeed = speedOnButton;
					showSpeed.setText("Speech Speed: " + String.valueOf(speedOnButton));
					;
					updateSpeed();
					textfield.requestFocus();
					textfield.selectEnd();
					
				}
			});
			hbox.getChildren().add(speedButton);


		}
		
//		hbox.getChildren().addAll(speed05,speed075,speed1,speed125,speed15,speed175,speed2);
		hbox.setAlignment(Pos.CENTER);

		speedVbox.getChildren().addAll(showSpeed, hbox);

		speedVbox.setAlignment(Pos.CENTER);
		speedVbox.setPadding(new Insets(10, 10, 50, 10));
		return speedVbox;
	}
}

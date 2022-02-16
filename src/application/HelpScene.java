package application;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class HelpScene {
	
	//A method to give the text labels the same properties
	private static void setLabelProperties(Label label) {
		label.setWrapText(true);
		label.setMaxWidth(700);
		label.setId("helpLabel");
	}

	public static Scene getHelpPage(Stage primaryStage) {
		
		//The main scene setup
		VBox helpVBox = new VBox();
		BorderPane helpBorderPane= new BorderPane();
		Scene helpScene = new Scene(helpBorderPane, 1200, 900);
		
		//Setting up key components of title and button
		Label title = new Label("Useful Information");
		title.setPadding(new Insets(50,0,25,0));
		title.setFont(Font.font("ubuntu", FontWeight.BOLD, FontPosture.REGULAR, 60));
		Button mainMenuButton = new Button("Return to Main Menu");
		mainMenuButton.setId("mainMenuButton");
		mainMenuButton.setMinSize(230,40);
		
		//The labels with the information about this software application
		Label helpLabel1 = new Label("1. The Games Module is a quiz styled spelling bee game that times how long it takes you to spell the 5 words.");
		Label helpLabel2 = new Label("2. The Practice Module is place where you can pratice spelling the words without limit and time, giving more hints when words are spelt incorrectly.");
		Label helpLabel3 = new Label("3. Macron can be added by clicking on the Macron buttons or by adding a hypen before the vowel to write the respective macron by live Macron substitution.");
		Label helpLabel4 = new Label("4. Speech Speeds can be adjusted at the bottom of the quiz page. Different variations of speed for the words spoken.");
		Label helpLabel5 = new Label("5. Submit and replay word buttons have hotkeys that allow them to be used easily. Enter to submit the word and Control to replay the word.");
		setLabelProperties(helpLabel1);
		setLabelProperties(helpLabel2);
		setLabelProperties(helpLabel3);
		setLabelProperties(helpLabel4);
		setLabelProperties(helpLabel5);
		
		mainMenuButton.setOnAction(actionEvent -> {try {primaryStage.setScene(MainPage.getMainPageScene(primaryStage));}catch(Exception e){}});
		
		//helpVBox is the container with all the elements of the page
		helpVBox.getChildren().addAll(title, helpLabel1,helpLabel2,helpLabel3,helpLabel4,helpLabel5,mainMenuButton);
		helpVBox.setPadding(new Insets(10,10,10,75));
		helpVBox.setSpacing(20);
		helpBorderPane.setLeft(helpVBox);
		
		helpScene.getStylesheets().add(HelpScene.class.getResource("application.css").toExternalForm());
		
		return helpScene;
	}
		
}

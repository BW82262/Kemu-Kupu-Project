package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MainPage {

	public static double speechSpeed = 1;
	public static String[] dropDownArray = { "All (Practise Only)", "Colours", "Days", "Days (Loan Words)", "Months",
			"Months (Loan Words)", "Babies", "Weather", "Compass Points", "Feelings", "Work", "Engineering", "Software",
			"University Life" };
	public static int numOfLists = 14;
	public static String spellingTopic;

	public static List<ArrayList<String>> topicArray = new ArrayList<ArrayList<String>>();

	public static String[] getRandomList(String[] wordList) {
		Random rand = new Random();
		int upperbound = wordList.length;
		ArrayList<String> outputList = new ArrayList<String>();
		int i = 0;
		while (i < 5) {
			String checkWord = wordList[rand.nextInt(upperbound)];
			if (!outputList.contains(checkWord)) {
				outputList.add(checkWord);
				i++;
			}
		}

		String[] output = { "0", "1", "2", "3", "4" };
		for (int j = 0; j < 5; j++) {
			output[j] = outputList.get(j);
		}

		return output;
	}


	public static List<ArrayList<String>> readFile(List<ArrayList<String>> topicArray) {
		String files[] = { "colours", "daysOfTheWeek1", "daysOfTheWeek2", "monthsOfTheYear1", "monthsOfTheYear2",
				"babies", "weather", "compassPoints", "feelings", "work", "engineering", "software", "uniLife" };

		String wordList;
		for (int i = 0; i < topicArray.size(); i++) {
			wordList = files[i];
			try {
				BufferedReader br = new BufferedReader(new FileReader("./wordlists/" + wordList));
				String word;
				while ((word = br.readLine()) != null) {

					topicArray.get(i).add(word);
				}
				br.close();
			} catch (Exception e) {

			}
		}

		return topicArray;
	}

	// Method to check if the lists are already added.
	public static void addToTopicArray(ArrayList<String> list) {
		if (topicArray.size() < 13) {
			topicArray.add(list);
		}
	}

	public static Scene getMainPageScene(Stage primaryStage) throws IOException {
		

		// Declaring and adding the lists to one arraylist.
		ArrayList<String> coloursList = new ArrayList<String>();
		addToTopicArray(coloursList);
		ArrayList<String> daysOfTheWeek1List = new ArrayList<String>();
		addToTopicArray(daysOfTheWeek1List);
		ArrayList<String> daysOfTheWeek2List = new ArrayList<String>();
		addToTopicArray(daysOfTheWeek2List);
		ArrayList<String> monthsOfTheYear1List = new ArrayList<String>();
		addToTopicArray(monthsOfTheYear1List);
		ArrayList<String> monthsOfTheYear2List = new ArrayList<String>();
		addToTopicArray(monthsOfTheYear2List);
		ArrayList<String> babiesList = new ArrayList<String>();
		addToTopicArray(babiesList);
		ArrayList<String> weatherList = new ArrayList<String>();
		addToTopicArray(weatherList);
		ArrayList<String> compassPointsList = new ArrayList<String>();
		addToTopicArray(compassPointsList);
		ArrayList<String> feelingsList = new ArrayList<String>();
		addToTopicArray(feelingsList);
		ArrayList<String> workList = new ArrayList<String>();
		addToTopicArray(workList);
		ArrayList<String> engineeringList = new ArrayList<String>();
		addToTopicArray(engineeringList);
		ArrayList<String> softwareList = new ArrayList<String>();
		addToTopicArray(softwareList);
		ArrayList<String> uniLifeList = new ArrayList<String>();
		addToTopicArray(uniLifeList);

		ArrayList<String> allArray = new ArrayList<String>();

		// Checks if all word lists are already added
		if (topicArray.size() < numOfLists) {
			topicArray = readFile(topicArray);
			// Create an list that add all the arraylist together
			for (ArrayList<String> wordList : topicArray) {
					allArray.addAll(wordList);
			}
			topicArray.add(0, allArray);
		}



		BorderPane mainPage = new BorderPane();
		Scene mainPageScene = new Scene(mainPage, 1200, 900);

		// Top of the borderpane will be the title

		Label title = new Label("Kēmu Kupu");
		title.setId("title");
		title.setFont(Font.font("ubuntu", FontWeight.BOLD, FontPosture.REGULAR, 100));
		title.setPadding(new Insets(50, 10, 50, 10));
		BorderPane.setAlignment(title, Pos.CENTER);

		// Centre of the borderpane will be the new game and quit options

		VBox vbox = new VBox(20);
		Button newQuizButton = new Button("New Quiz");
		newQuizButton.setId("mainPageLabel");
		newQuizButton.setMinSize(300, 75);
		Button practiceButton = new Button("Practise");
		practiceButton.setId("mainPageLabel");
		practiceButton.setMinSize(300, 75);
		Button helpButton = new Button("Help");
		helpButton.setId("mainPageLabel");
		helpButton.setMinSize(300, 75);
		Button quitButton = new Button("Quit Game");
		quitButton.setId("mainPageLabel");
		quitButton.setMinSize(300, 75);

		// Creating the drop down box for selecting a word list
		final ComboBox<String> wordListDropDown = new ComboBox<String>();
		wordListDropDown.getItems().addAll(dropDownArray);
		wordListDropDown.setPromptText("Please Choose A Spelling Topic");
		wordListDropDown.setMinSize(300, 75);

		newQuizButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					if (wordListDropDown.getValue() == null
							|| wordListDropDown.getValue().equals("All (Practise Only)")) {

					} else {
						spellingTopic = wordListDropDown.getValue();
						for (int i = 0; i < numOfLists; i++) {
							if (dropDownArray[i].equals(spellingTopic)) {
								NewQuiz.setIsNewQuiz(true);
								NewQuiz.changeWordList(getRandomList(topicArray.get(i).toArray(new String[0])));
							}
						}
						primaryStage.setScene(NewQuiz.getPracticeScene(primaryStage));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		practiceButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {

					if (wordListDropDown.getValue() == null) {

					} else {
						spellingTopic = wordListDropDown.getValue();
						for (int i = 0; i < numOfLists; i++) {
							if (dropDownArray[i].equals(spellingTopic)) {
								Practice.setIsNewQuiz(false);
								Practice.changeWordList(topicArray.get(i).toArray(new String[0]));
							}
						}
						primaryStage.setScene(Practice.getPracticeScene(primaryStage));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		
		helpButton.setOnAction(actionEvent -> {primaryStage.setScene(HelpScene.getHelpPage(primaryStage));});

		quitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				primaryStage.close();
			}
		});

		BorderPane.setAlignment(vbox, Pos.CENTER);


		vbox.getChildren().addAll(title, wordListDropDown, newQuizButton, practiceButton,helpButton, quitButton);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(0, 0, 100, 100));
		mainPage.setLeft(vbox);


		mainPageScene.getStylesheets().add(MainPage.class.getResource("application.css").toExternalForm());

		return mainPageScene;
	}
}
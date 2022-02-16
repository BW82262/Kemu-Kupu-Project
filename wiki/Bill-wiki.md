# Bill's Journal

## 20/9/2021

Issue with how to read the word list.

Design decision to determine if to read list or hardcode. Deciding to hard code as there are multiple different sections of word list in the excel file.


Problem with transitioning between scenes, can't see the scene while inside one of the add functions.

Solved by inputting the "primaryStage" through all the add functions so that we can use primaryStage.setScene() command during all different scenes.

## 23/9/2021 - JavaFX scene styling.

How to link the scenes and the application.css file together when they're in separate classes?
Use the same application.css file but have different IDs for each component.

## 25/9/2021 - Resizing WordList and Reward Scenes

How to store multiple different buttons in a 2 by 7 spacing?
Can implement a GridPane that stores a button in each cell

## 15/10/2021 - Spam protection on buttons

There is an issue when spamming the skip button, it will automatically queue the follow words, so we need to prevent this from occurring.
Can disable the button, then after it's done saying the word, the renable it.
Doesn't work, multiple threads or such are running, preventing this from happening.
SOLUTION: Use Platform.runLater 

## 17/10/2021 - Reading word lists from files

Need to change the method word lists are implemented in our code. Currently hard coded.
Lots of dependencies on the prexisting word list arrays, need to change this.
Resolved by adding a static term TopicArrays, containing all the word lists.



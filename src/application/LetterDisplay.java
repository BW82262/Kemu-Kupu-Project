package application;

import java.util.ArrayList;
import java.util.Collections;

public class LetterDisplay {
	/**
	 * Takes the input string and returns a string with all the letters replaced
	 * with under scores
	 * 
	 * @param wordToSpell
	 * @return blankSpaces
	 */
	public static String getBlankSpaces(String wordToSpell) {
		String[] inputWord = wordToSpell.split("");
		String blankSpaces = "";

		// If the character in the input is a hyphen
		// then keep it as a hyphen in the hint, if
		// it is a space then increase the number of spaces,
		// otherwise if it is a letter then replace it with
		// an underscore
		for (int i = 0; i < wordToSpell.length(); i++) {
			if (inputWord[i].equals(" ")) {
				blankSpaces = blankSpaces + " ";
			} else {
				blankSpaces = blankSpaces + "_";
			}
		}

		return blankSpaces;
	}

	/**
	 * This is the same method as above but if given certain parameters will replace
	 * an under score at the given index with the input character, this is used when
	 * the user gets their first attempt wrong in the quiz and practise module
	 * 
	 * @param wordToSpell
	 * @param character
	 * @param index
	 * @return blankSpaces
	 */
	public static String replaceBlankSpaces(String blankSpaces, char character, int index) {
		String[] unreplacedWord = blankSpaces.split("");
		String replacedWord = "";

		boolean isReplaced = false;

		int letterCount = -1;
		for (int i = 0; i < unreplacedWord.length; i++) {
			// Track which letter index the loop is at
			// by counting the number of under scores or
			// letters seen
				letterCount++;

			// If we are at the correct letter index
			// and we haven't already done the
			// replacing then replace that index
			// with the input character
			if (letterCount == index && !isReplaced) {
				replacedWord = replacedWord + character;
				isReplaced = true;
			} else {
				replacedWord = replacedWord + unreplacedWord[i];
			}
		}

		return replacedWord;
	}

	/**
	 * Checks if the current index in the input word is a letter, then records it in
	 * the output
	 * 
	 * @param wordToSpell
	 * @return letterIndices
	 */
	public static ArrayList<Integer> getLetterIndices(String wordToSpell) {
		String[] inputWord = wordToSpell.split("");

		ArrayList<Integer> letterIndices = new ArrayList<>();
		int j = 0;
		for (int i = 0; i < wordToSpell.length(); i++) {
			// If the current index is not a space or a hyphen
			// then it must be a letter or underscore, we then
			// keep track of that current index
			if (!inputWord[i].equals(" ")) {
				letterIndices.add(j, i);
				j++;
			}
		}

		return letterIndices;
	}
	
	/**
	 * Used in the practise module, this method calls the getLetterIndices method
	 * and getRandom method to generate the indices of the letters to be shown for
	 * the hint, it then calls replaceBlankSpaces multiple times to replace those
	 * letters
	 * 
	 * @param wordToSpell
	 * @param blankSpaces
	 * @return blankSpaces
	 */
	public static String newQuizModuleHint(String wordToSpell, String blankSpaces) {
		return replaceBlankSpaces(blankSpaces, wordToSpell.charAt(1), 1);
	}

	/**
	 * Generates a random number between the mininum and maximum input
	 * 
	 * @param min
	 * @param max
	 * @return rand
	 */
	public static int getRandom(int min, int max) {
		int range = max - min + 1;
		int rand = (int) (Math.random() * range) + min;

		return rand;
	}

	/**
	 * Used in the practise module, this method calls the getLetterIndices method
	 * and getRandom method to generate the indices of the letters to be shown for
	 * the hint, it then calls replaceBlankSpaces multiple times to replace those
	 * letters
	 * 
	 * @param wordToSpell
	 * @param blankSpaces
	 * @return blankSpaces
	 */
	public static String practiseModuleHint(String wordToSpell, String blankSpaces) {
		// Get the array list of indices
		// that contain a letter
		ArrayList<Integer> letterIndices = getLetterIndices(wordToSpell);

		// Generate how many letters will be
		// shown in the hint, it should be
		// at least one and less than half
		// the total letters
		int numLetters = getRandom(1, letterIndices.size() / 2);

		// Shuffle all the indices that
		// contain a letter so that the
		// indices chosen aren't always the
		// beginning few
		Collections.shuffle(letterIndices);

		// Insert the hint letters into
		// the generated indices
		for (int i = 0; i < numLetters; i++) {
			int index = letterIndices.get(i);
		
			blankSpaces = replaceBlankSpaces(blankSpaces, wordToSpell.charAt(index), index);
		}

		return blankSpaces;
	}
}

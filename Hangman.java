/*
 * File: Hangman.java
 * ------------------
 * This program will eventually play the Hangman game from
 * Assignment #4.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Hangman extends ConsoleProgram {

	/***********************************************************
	 *              CONSTANTS                                  *
	 ***********************************************************/
	
	/* The number of guesses in one game of Hangman */
	private static final int N_GUESSES = 7;
	/* The width and the height to make the karel image */
	private static final int KAREL_SIZE = 150;
	/* The y-location to display karel */
	private static final int KAREL_Y = 230;
	/* The width and the height to make the parachute image */
	private static final int PARACHUTE_WIDTH = 300;
	private static final int PARACHUTE_HEIGHT = 130;
	/* The y-location to display the parachute */
	private static final int PARACHUTE_Y = 50;
	/* The y-location to display the partially guessed string */
	private static final int PARTIALLY_GUESSED_Y = 430;
	/* The y-location to display the incorrectly guessed letters */
	private static final int INCORRECT_GUESSES_Y = 460;
	/* The fonts of both labels */
	private static final String PARTIALLY_GUESSED_FONT = "Courier-36";
	private static final String INCORRECT_GUESSES_FONT = "Courier-26";
	
	/***********************************************************
	 *              Instance Variables                         *
	 ***********************************************************/
	
	/* An object that can produce pseudo random numbers */
	private RandomGenerator rg = new RandomGenerator();
	
	private GCanvas canvas = new GCanvas();
	
	/***********************************************************
	 *                    Methods                              *
	 ***********************************************************/
	
	private int guesses = N_GUESSES;
	private String word = getRandomWord();
	private String hiddenWord;
	private String getLetter;
	private ArrayList<GLine> ropes = new ArrayList<GLine>();
	private GLabel hiddenLabel = new GLabel ("");
	private String incorrectGuess = "";
	private GLabel incorrectGuessLabel = new GLabel ("");
	private GImage karel = new GImage ("karel.png");
	
	public void run() {
		setUp();
		playGame();
	}	
	
	private void setUp() {
		println("Welcome to Hangman");
		println(word);
		hiddenWord = hideWord(word);
		println("Your word now looks like this: " + hiddenWord);
		println("You have " + guesses + " guesses left.");
	}	
		
	private void playGame() {	
		while (guesses > 0) {	
			displayPartialWord(hiddenWord);
			getLetter = readLine ("Your guess: ");
			if (getLetter.length() > 1) {
				getLetter = readLine ("Please enter only character. Your guess: ");
			}
			checkLetter();	
			if (hiddenWord.equals(word)) {
				println("You win.");			
				println("The word was: " + word);
				break;
			}
			println("Your guess now looks like this: " + hiddenWord);
			println("You have " + guesses + " guesses left.");
		}
		if (guesses == 0) {
			canvas.remove(karel);
			drawKarelFlipped();
			println ("You Lose.");
			println ("The word was: " + word);
		}
	}	
	
	private String hideWord(String word) {
		String result = "";
		for (int i = 0; i < word.length(); i++) {
			result = result + "-";
		}
		return result;
	}
	
	private void checkLetter() {
		char letter = getLetter.charAt(0);
		if (Character.isLowerCase(letter)) {
			letter = Character.toUpperCase(letter);
		}
		if (word.indexOf(letter) != -1) {
			println ("That guess is correct.");
			for (int i = 0; i < word.length(); i++) {
				if (letter == word.charAt(i)) {
					hiddenWord = hiddenWord.substring(0, i) + letter + hiddenWord.substring(i + 1);
				} 		
			}
		} else {
			guesses--;
			println("There are no " + letter + "'s in the word.");
			if (guesses % 2 == 0) {
				GLine line = ropes.get(0);
				canvas.remove(line);
				ropes.remove(0);
			} else {
				GLine line = ropes.get(ropes.size() - 1);
				canvas.remove(line);
				ropes.remove(ropes.size() - 1);
			}
			incorrectGuess += getLetter;
			displayIncorrectLetters(incorrectGuess);
		}
	}	
	
	public void init() {
		add(canvas);
		drawBackground();
		drawKarel();
		drawParachute();
		drawLines();
	}
	
	private void drawBackground() {
		GImage bg = new GImage ("background.jpg");
		bg.setSize(canvas.getWidth(), canvas.getHeight());
		canvas.add(bg, 0, 0);
	}
	
	private void drawKarel() {
		karel = new GImage ("karel.png");
		karel.setSize(KAREL_SIZE, KAREL_SIZE);
		canvas.add(karel, canvas.getWidth()/2 - KAREL_SIZE/2, KAREL_Y);
	}
	
	private void drawKarelFlipped() {
		GImage karelFlipped = new GImage ("karelFlipped.png");
		karelFlipped.setSize(KAREL_SIZE, KAREL_SIZE);
		canvas.add(karelFlipped, canvas.getWidth()/2 - KAREL_SIZE/2, KAREL_Y);
	}
	
	public void drawParachute() {
		GImage parachute = new GImage ("parachute.png");
		parachute.setSize(PARACHUTE_WIDTH, PARACHUTE_HEIGHT);
		canvas.add(parachute, canvas.getWidth()/2 - PARACHUTE_WIDTH/2, PARACHUTE_Y);
	}
	
	private void displayPartialWord(String word) {
		canvas.remove(hiddenLabel);
		hiddenLabel = new GLabel (hiddenWord);
		hiddenLabel.setFont(PARTIALLY_GUESSED_FONT);
		double x = canvas.getWidth()/2 - hiddenLabel.getWidth()/2;
		double y = PARTIALLY_GUESSED_Y;
		canvas.add(hiddenLabel, x, y);
	}
	
	private void displayIncorrectLetters(String incorrectGuess) {
		canvas.remove(incorrectGuessLabel);
		incorrectGuessLabel = new GLabel (incorrectGuess);
		incorrectGuessLabel.setFont(INCORRECT_GUESSES_FONT);
		double x = canvas.getWidth()/2 - incorrectGuessLabel.getWidth()/2;
		double y = INCORRECT_GUESSES_Y;
		canvas.add(incorrectGuessLabel, x, y);
	}
	
	private void drawLines() {
		int startY = PARACHUTE_HEIGHT + PARACHUTE_Y;
		int endX = canvas.getWidth()/2;
		int endY = KAREL_Y;
		for (int i = 0; i < 7; i++) {
			int startX = (canvas.getWidth()/2 - PARACHUTE_WIDTH/2) + (PARACHUTE_WIDTH/6 * i);
			GLine line = new GLine(startX, startY, endX, endY);
			canvas.add(line);
			ropes.add(line);
		}
	}
	

	
	
	

	
	/**
	 * Method: Get Random Word
	 * -------------------------
	 * This method returns a word to use in the hangman game. It randomly 
	 * selects from among 10 choices.
	 */
	private String getRandomWord() {
		int index = rg.nextInt(10);
		if(index == 0) return "BUOY";
		if(index == 1) return "COMPUTER";
		if(index == 2) return "CONNOISSEUR";
		if(index == 3) return "DEHYDRATE";
		if(index == 4) return "FUZZY";
		if(index == 5) return "HUBBUB";
		if(index == 6) return "KEYHOLE";
		if(index == 7) return "QUAGMIRE";
		if(index == 8) return "SLITHER";
		if(index == 9) return "ZIRCON";
		throw new ErrorException("getWord: Illegal index");
	}

}

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeSet;

public class EvilHangman {
	private String dictionaryFile;
	private int wordLength;
	private int numberOfGuesses;
	private ArrayList<String> dictionary;
	private ArrayList<Character> partialSolution;
	private TreeSet<Character> incorrectGuesses;
	private SolutionManipulator manipulator;

	// separate constructors into two for convenient testing w/ other files
	public EvilHangman() {
		this("engDictionary.txt");
	}

	public EvilHangman(String dictionarySource) {
		this.dictionaryFile = dictionarySource;
		this.dictionary = new ArrayList<String>();
		this.incorrectGuesses = new TreeSet<Character>();

		try {
			this.dictionary = dictionaryToArrayList(dictionaryFile);
		} catch (FileNotFoundException e) {
			System.out.println("Dictionary file could not be found.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Dictionary file could not be opened.");
			e.printStackTrace();
		}
	}

	public void start() {
		int numberOfGuesses = 0; // to keep track of number of guesses (for fun)
		generateRandomLength();
		manipulator = new SolutionManipulator(wordLength, dictionary);
		while (!manipulator.solutionComplete(partialSolution)) {
			askForInput();
		}
		printFinalMessage(ListToString(partialSolution));
	}

	private void generateRandomLength() {
		int randomInt = new Random().nextInt(dictionary.size());
		String randomWord = dictionary.get(randomInt);

		this.wordLength = randomWord.length();
		partialSolution = new ArrayList<Character>(this.wordLength);

		for (int i = 0; i < this.wordLength; i++) {
			partialSolution.add(i, '_');
		}
	}

	private void printCurrentState() {
		for (int i = 0; i < partialSolution.size(); i++) {
			System.out.print(partialSolution.get(i) + " ");
		}
		System.out.println();
		System.out.println("Incorrect guesses: " + incorrectGuesses);
	}

	private void askForInput() {
		System.out.println("Please enter a letter.");

		printCurrentState();

		Scanner inputScnr = new Scanner(System.in);
		String userInput = inputScnr.next();

		if (userInput.length() != 1) {
			System.out.println("Please enter a single letter character.");
		} else if (incorrectGuesses.contains(userInput.charAt(0)) || partialSolution.contains(userInput.charAt(0))) {
			System.out.println("You have already tried that letter.");
		} else {
			ArrayList<Character> previousSolution = (ArrayList<Character>) partialSolution.clone();
			partialSolution = manipulator.findBestSolution(previousSolution, userInput.charAt(0));
			if (previousSolution.equals(partialSolution)) {
				System.out.println("No such letter in this word!");
				incorrectGuesses.add(userInput.charAt(0));
			}
			numberOfGuesses++;
		}
	}

	private void printFinalMessage(String solution) {
		System.out.println(
				"Game over! The word was " + solution + ". You guessed the solution in " + numberOfGuesses + " tries.");
	}

	public ArrayList<String> dictionaryToArrayList(String fileName) throws IOException {
		// Note: no handling here, because this method is called
		// from a try block
		FileInputStream fs = new FileInputStream(fileName);
		Scanner scnr = new Scanner(fs);

		ArrayList<String> wordList = new ArrayList<String>();

		while (scnr.hasNext()) {
			wordList.add(scnr.next());
		}
		fs.close();
		return wordList;
	}

	private String ListToString(ArrayList<Character> list) {
		String str = "";
		for (int i = 0; i < list.size(); i++) {
			str += list.get(i);
		}
		return str;
	}

	public ArrayList<String> getWordList() {
		return this.dictionary;
	}
}

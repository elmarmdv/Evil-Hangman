import java.util.ArrayList;
import java.util.HashMap;

public class SolutionManipulator {
	int wordLength;
	ArrayList<String> remainingWords; // to keep track which words can still be used in the dictionary
	// maps arraylist of characters (candidate solution)
	// to arraylist of word families
	HashMap<ArrayList<Character>, ArrayList<String>> wordFamilies;

	public SolutionManipulator(int length, ArrayList<String> allWords) {

		this.wordLength = length;
		wordFamilies = new HashMap<ArrayList<Character>, ArrayList<String>>();
		remainingWords = new ArrayList<String>();

		// add only words that are of defined length
		for (String word : allWords) {
			if (word.length() == this.wordLength) {
				remainingWords.add(word);
			}
		}
	}

	public boolean solutionComplete(ArrayList<Character> partialSolution) {
		// check if there are empty letters left to guess
		int emptyChars = 0;

		for (int i = 0; i < partialSolution.size(); i++) {
			if (partialSolution.get(i) == '_') {
				emptyChars++;
			}
		}
		return emptyChars == 0;
	}

	public ArrayList<Character> findBestSolution(ArrayList<Character> partialSolution, char chosenLetter) {
		// general: take the chosenLetter and see where/if we should place it

		ArrayList<Character> candidateSolution = new ArrayList<Character>(this.wordLength);

		// Main algorithm:
		// 1. Take each word among the remaining words
		for (int i = 0; i < remainingWords.size(); i++) {
			String word = remainingWords.get(i);
			// "reset" candidateSolution to default
			candidateSolution.clear();
			candidateSolution = (ArrayList<Character>) partialSolution.clone();

			// 2. For each word, see what partial solution would look like
			// when taking into account user's guess
			for (int j = 0; j < word.length(); j++) {
				if (word.charAt(j) == chosenLetter) {
					candidateSolution.set(j, chosenLetter);
				}
			}
			// 3. If such partial solution (key) exists in our wordFamilies hashmap
			// add this word to the arraylist (value) of words that fit this solution
			if (wordFamilies.containsKey(candidateSolution)) {
				(wordFamilies.get(candidateSolution)).add(word);

				// 4. If such partial solution doesn't exist as a key yet, create such key
				// and associated arraylist (value) with that word in it
			} else {
				// create a new key and arraylist value
				wordFamilies.put((ArrayList<Character>) candidateSolution.clone(), new ArrayList<String>());
				wordFamilies.get(candidateSolution).add(word);
			}
		}

		// Now, let's see which partial solution is best by finding which partial
		// solution (key) has the largest arraylist of words associated with it
		int maxWords = 0;
		ArrayList<Character> bestSolution = new ArrayList<Character>(this.wordLength);

		for (ArrayList<Character> oneSolution : wordFamilies.keySet()) {
			// if size of this key (partial solution) is higher that max, set it to max
			if (wordFamilies.get(oneSolution).size() > maxWords
					// or, if the sizes for two candidates is the same, then choose
					// the one, if any, that is the original partial solution
					|| (wordFamilies.get(oneSolution).size() == maxWords && oneSolution.equals(partialSolution))) {
				maxWords = wordFamilies.get(oneSolution).size();
				bestSolution = (ArrayList<Character>) oneSolution.clone();
			}
		}
		remainingWords = (ArrayList<String>) wordFamilies.get(bestSolution).clone();
		wordFamilies.clear();

		return bestSolution;
	}
}

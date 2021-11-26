import java.util.ArrayList;
import java.util.HashMap;

public class SolutionManipulator {
	int wordLength;
	ArrayList<String> remainingWords; // to keep track which words can still be used in the dictionary
	// maps arraylist of characters (candidate solution) to arraylist of fitting
	// words
	HashMap<ArrayList<Character>, ArrayList<String>> wordFamilies;

	SolutionManipulator(int length, ArrayList<String> allWords) {
		this.wordLength = length;
		wordFamilies = new HashMap<ArrayList<Character>, ArrayList<String>>();
		remainingWords = new ArrayList<String>();

		// add only words that are of defined length
		for (String word : allWords) {
			if (word.length() == this.wordLength) {
				remainingWords.add(word);
			}
		}
		System.out.println("Words at the beginning: " + remainingWords.size());
	}

	public boolean solutionComplete(ArrayList<Character> partialSolution, int solutionLength) {
		// check if there are empty letters left to guess
		int emptyChars = 0;

		for (int i = 0; i < solutionLength; i++) {
			if (partialSolution.get(i) == '_') {
				emptyChars++;
			}
		}
		return emptyChars == 0;
	}

	public ArrayList<Character> calculateBestPosition(ArrayList<Character> partialSolution, char chosenLetter) {
		// general: take the chosenLetter and see where/if we should place it

		ArrayList<Character> candidateSolution = new ArrayList<Character>(this.wordLength);

		for (int i = 0; i < remainingWords.size(); i++) {
			String word = remainingWords.get(i);
			// "reset" candidateSolution to default
			candidateSolution.clear();
			candidateSolution = (ArrayList<Character>) partialSolution.clone();

			// TEST
			System.out.println("Next word is: " + word);

			for (int j = 0; j < word.length(); j++) {
				if (word.charAt(j) == chosenLetter) {
					candidateSolution.set(j, chosenLetter);
				}
				// TEST
				System.out.println(candidateSolution);
			}
			if (wordFamilies.containsKey(candidateSolution)) {
				(wordFamilies.get(candidateSolution)).add(word);
			} else {
				// create a new key and arraylist value

				System.out.println("wordFamilies keyset before adding key: " + wordFamilies.keySet());
				wordFamilies.put((ArrayList<Character>) candidateSolution.clone(), new ArrayList<String>());
				wordFamilies.get(candidateSolution).add(word);
				// TEST
				System.out.println("New comb word: " + word);
				System.out.println("New candidate key value: " + wordFamilies.get(candidateSolution));
				System.out.println("wordFamilies keyset when adding key: " + wordFamilies.keySet());
			}
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException ex) {
//				Thread.currentThread().interrupt();
//			}
			System.out.println("wordFamilies keyset endloop" + wordFamilies.keySet());
		}

		int maxWords = 0;
		ArrayList<Character> bestSolution = new ArrayList<Character>(this.wordLength);

		// TEST
		System.out.println("WordFamilies keyset: " + wordFamilies.keySet());

		for (ArrayList<Character> oneSolution : wordFamilies.keySet()) {
			// if size of this key (partial solution) is higher that max, set it to max
			if (wordFamilies.get(oneSolution).size() > maxWords
					// avoid returning the full word at the end if there is still another option
					// example: if we are left with "cat" and "bat", and the user guesses 'c'
					// then, maxWords for each is = 1, but we want to return "bat" to continue
					&& !(remainingWords.size() > 1 && !oneSolution.contains('_'))) {
				maxWords = wordFamilies.get(oneSolution).size();
				bestSolution = (ArrayList<Character>) oneSolution.clone();
			}
		}

		System.out.println("Max values: " + maxWords);

		// TEST
		System.out.println("Best solution: " + bestSolution);

		remainingWords = (ArrayList<String>) wordFamilies.get(bestSolution).clone();
		wordFamilies.clear();

		System.out.println("Remaining words: " + remainingWords);
		return bestSolution;

//		ArrayList<ArrayList> allCandidateFamilies = new ArrayList<ArrayList>(this.wordLength);
//		
//		allCandidateFamilies.set(0, calculateFamilies(ListToString(partialSolution))); // first take: what if we don't place the letter at all
//		ArrayList<Character> candidateSolution = new ArrayList<Character>(this.wordLength);
//		
//		for (int i = 0; i < this.wordLength; i++) {
//			candidateSolution = partialSolution; // set to default
//			// set the i'th letter to user input and call calculateFamilies
//			if (candidateSolution.get(i) != '_') {
//				candidateSolution.set(i, chosenLetter); 
//			}
//			allCandidateFamilies.set(0, calculateFamilies(ListToString(candidateSolution)));
//		}
//		
//		int maxFamilies = 0;
//		int maxFamilyIndex = 0;
//		for (int i = 0; i < allCandidateFamilies.size(); i++) {
//			if (allCandidateFamilies.get(i).size() > maxFamilies) {
//				maxFamilies = allCandidateFamilies.get(i).size();
//				maxFamilyIndex = i;
//			}
//		}
//		
//		// make remainingWords consist of the words of the biggest family
//		remainingWords = allCandidateFamilies.get(maxFamilyIndex);
//				
//		bestCandidate = ???;
//				
//		// WHAT TO DO WHEN IN THE END THE BEST WORD MIGHT HAVE TWO LETTERS???
//		
//		// return best solution / updated current state
//		return candidateSolution;
	}

//	// DELETE LATER!
//	public ArrayList<String> calculateFamilies(String candidate) {
//		// "clear" the family array for current candidate
//		ArrayList<String> candidateFamily = new ArrayList<String>();
//
//		for (String word : remainingWords) {
//			if (word.length() == candidate.length() && checkIfWordFits(word, candidate)) {
//				candidateFamily.add(word);
//			}
//		}
//		return candidateFamily;
//	}

//	public boolean checkIfWordFits(String word, String candidate) {
//		boolean wordFits = true;
//		for (int i = 0; i < word.length(); i++) {
//			if (word.charAt(i) != candidate.charAt(i) && word.charAt(i) != '_') {
//				wordFits = false;
//			}
//		}
//		return wordFits;
//	}

//	public ArrayList<Object> copyList(ArrayList<Object> inputList) {
//		ArrayList<Object> outputList = new ArrayList<Object>();
//
//		for (int i = 0; i < inputList.size(); i++) {
//			outputList.add(i, inputList.get(i));
//		}
//		return outputList;
//	}

}

package madlibs;

import java.util.*;

/**
 * Tracks the index of a certain part of speech within a passage and determines
 * which words should be replaced. Should really be an inner/nested class
 * within the Passenger class since will only be used by the Passage class.
 * It is easier to test if it is a separate class though.
 * @author ross
 *
 */
public class PartsOfSpeech {
	private ArrayList<Integer> indexesOfAllWords;
	// Making into 
	private ArrayList<Integer> indexesOfReplacementWords;
	
	/**
	 * Creates a new PartsOfSpeechObject
	 * @param indexes The indexes of the words in the passage that correspond to
	 * this part of speech.
	 * @param percent The percent of words that should be replaced.
	 * @param minN The minimum number of words that should be replaced. If
	 * the number of words is less than this
	 */
	PartsOfSpeech() {
		indexesOfAllWords =  new ArrayList<Integer>();
	}
	
	/**
	 * Adds an index that corresponds to a word in the original passage that
	 * is the correct kind of part of speech.
	 * @param index
	 */
	public void add(Integer index) {
		indexesOfAllWords.add(index);
	}
	public int numberOfWords() {
		return indexesOfAllWords.size();
	}
	public Integer[] getIndexesOfAllWords() {
		return indexesOfAllWords.toArray(new Integer[indexesOfAllWords.size()]);
	}
	
	/**
	 * Populates the replacement from provided indexes based on the supplied criteria.
	 * No random seed is required.
	 * @param percent The percent of words that should be selected for replacement
	 * @param minN The minimum number of words that should be selected for replacement
	 * @param maxN The maximum number of words that should be selected for replacement
	 * @param seed The random seed
	 * @return
	 */
	public void sample(double percent, int minN, int maxN) {
		sample(percent, minN, maxN, new Random().nextLong());
	}
	/**
	 * Samples from provided indexes based on the supplied criteria. A random seed is
	 * required.
	 * @param percent The percent of words that should be selected for replacement
	 * @param minN The minimum number of words that should be selected for replacement
	 * @param maxN The maximum number of words that should be selected for replacement
	 * @param seed The random seed
	 * @return
	 */
	public void sample(double percent, int minN, int maxN,	long seed) {
		// percent: between 0 and 1
		// minN > 0
		// MaxN > 1; greater than minN
		int numToSample = (int) (percent * indexesOfAllWords.size());
		if (numToSample < minN) {
			numToSample = minN;
		}
		if (numToSample > maxN) {
			numToSample = maxN;
		}
		if (numToSample < indexesOfAllWords.size()) {
			indexesOfReplacementWords = new ArrayList<>();
			
			Random random = new Random(seed);
			// Determine spacing
			int stepSize = indexesOfAllWords.size() / numToSample;
			
			// Determine starting index
			int currentIndex = random.nextInt(indexesOfAllWords.size());
			while (indexesOfReplacementWords.size() < numToSample) {
				indexesOfReplacementWords.add(indexesOfAllWords.get(currentIndex));
				currentIndex += stepSize;
				// If past end of available indexes, start from beginning
				if (currentIndex >= indexesOfAllWords.size()) {
					currentIndex -= indexesOfAllWords.size();
				}
			}
		}
		else {
			indexesOfReplacementWords = indexesOfAllWords;
		}
	}
	/**
	 * @return The number of words that the user should supply to replace
	 * the sampled words.
	 */
	public int getNumberOfReplacementWords() {
		//TODO: Throw an exception if calling before sample has been called.
		return indexesOfReplacementWords.size();		
	}

	
	public Integer[] getIndexesOfReplacementWords() {
		return indexesOfReplacementWords.toArray(new Integer[getNumberOfReplacementWords()]);
	}
	
	

}

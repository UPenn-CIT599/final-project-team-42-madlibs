package madlibs;

import java.util.*;
import edu.stanford.nlp.simple.*;

/**
 * This class represents a passage of text in a suitable format for playing a
 * game of MadLibs. It chooses which words will be replaced and accepts replacement words.
 * Will return the newly modified text along with the indexes of the modified words
 * so that they can be highlighted. 
 *
 */
public class Passage {
	
	
	public enum PartOfSpeech {
		SINGULAR_NOUN,
		PLURAL_NOUN,
		ADJECTIVE,
		ADVERB,
		ED_VERB,
		ING_VERB;
	}
	
	private ArrayList<String> words;
	
	private ArrayList<Integer> singularNouns;
	private ArrayList<Integer> pluralNouns;
	private ArrayList<Integer> adverbs;
	private ArrayList<Integer> adjectives;
	private ArrayList<Integer> ingVerbs;
	private ArrayList<Integer> edVerbs;
	
	private String originalText;
	
	/** Constructs a new Passage object from the supplied text.
	 * 
	 * @param originalText The text of the passage
	 */
	Passage (String originalText) {
		
	
		this.originalText = originalText;
		words = new ArrayList<String>();
		
		singularNouns = new ArrayList<Integer>();
		pluralNouns = new ArrayList<Integer>();
		adverbs = new ArrayList<Integer>();
		adjectives = new ArrayList<Integer>();
		edVerbs = new ArrayList<Integer>();
		ingVerbs = new ArrayList<Integer>();
		
		Document document = new Document(originalText);
		
		int index = 0;
		for (Sentence sentence: document.sentences()) {
			for (String word: sentence.words()) {
				words.add(word);
			}
			// Get the different parts of speech, adding to appropriate object
			for (String pos: sentence.posTags()) {
				if (pos.equals("NN") || pos.equals("NNP")) {
					singularNouns.add(index);
				}
				else if (pos.equals("NNS") || pos.equals("NNPS")) {
					pluralNouns.add(index);
				}
				else if (pos.equals("JJ")) {
					adjectives.add(index);
				}
				else if (pos.equals("RB")) {
					adverbs.add(index);
				}
				else if (pos.equals("VBG")) {
					ingVerbs.add(index);
				}
				else if (pos.equals("VBN")) {
					edVerbs.add(index);
				};
				index++;
			};
		}
	}
	
	/**
	 * 
	 * @return The original, unmodified, text.
	 */
	public String getOriginalText() {
		return originalText;
	}
	
	/**
	 * 
	 * @return The modified text that contains user supplied words
	 */
	public String getUpdatedText() {
		// Just an example case
		return "This is a test case that is use to test things. Test!";
	}
	
	/**
	 * 
	 * @return Gets the indexes of within the modified text of the words that
	 * have been replaced. This is a 2-dimensional integer array, n x 2, where
	 * n is the the number of words that have been replaced. The first element
	 * of every row gives the place where the word starts in the passage, and
	 * the second element give the place where the word ends.
	 */
	public int[][] getIndexesOfReplacements() {
		// Just an example case
		int[][] indexes = {
				{10, 14},
				{35, 39},
				{49, 53}
		};
		return indexes;
	}
	
	/**
	 * Helper method for converting an Integer ArrayList to an Array
	 * @param list
	 * @return
	 */
	private Integer[] toArray(ArrayList<Integer> list) {
		return list.toArray(new Integer[list.size()]);
	}
	
	/**
	 * Returns the indexes where the specified part of speech can be found.
	 * @param partOfSpeech A valid part of speech from the PartOfSpeech Enum
	 * @return the indexes where the specified part of speech can be found,
	 * as an Integer array 
	 */
	public Integer[] getIndexes(PartOfSpeech partOfSpeech) {
		switch(partOfSpeech) {
		case SINGULAR_NOUN:
			return toArray(singularNouns);
		case PLURAL_NOUN:
			return toArray(pluralNouns);
		case ADJECTIVE:
			return toArray(adjectives);
		case ADVERB:
			return toArray(adverbs);
		case ED_VERB:
			return toArray(edVerbs);
		case ING_VERB:
			return toArray(ingVerbs);
		default:
			throw new IllegalArgumentException("Invalid part of speech");
		}
	}

	/**
	 * Samples from provided indexes based on the supplied criteria. A random seed is
	 * required.
	 * @param indexesToSample The indexes to sample from
	 * @param percent The percent of words that should be selected for replacement
	 * @param minN The minimum number of words that should be selected for replacement
	 * @param maxN The maximum number of words that should be selected for replacement
	 * @param seed The random seed
	 * @return
	 */
	public static Integer[] sample(
			Integer[] indexesToSample,
			double percent,
			int minN,
			int maxN,
			long seed
			) {
		int numToSample = (int) (percent * indexesToSample.length);
		if (numToSample < minN) {
			numToSample = minN;
		}
		if (numToSample > maxN) {
			numToSample = maxN;
		}
		if (numToSample > indexesToSample.length) {
			return indexesToSample;
		}
	
		Integer[] sampledIndexes = new Integer[numToSample]; 
		
		Random random = new Random(seed);
		// Determine spacing
		int stepSize = indexesToSample.length / numToSample;
		
		// Determine starting index
		int currentIndex = random.nextInt(indexesToSample.length);
		for (int i = 0; i < numToSample; i++) {
			sampledIndexes[i] = indexesToSample[currentIndex];
			currentIndex += stepSize;
			// If past end of available indexes, start from beginning
			if (currentIndex >= indexesToSample.length) {
				currentIndex -= indexesToSample.length;
			}
		}
		return sampledIndexes;
	}
	
	/**
	 * Samples from provided indexes based on the supplied criteria. No random seed is
	 * required.
	 * @param indexesToSample The indexes to sample from
	 * @param percent The percent of words that should be selected for replacement
	 * @param minN The minimum number of words that should be selected for replacement
	 * @param maxN The maximum number of words that should be selected for replacement
	 * @param seed The random seed
	 * @return
	 */
	public static Integer[] sample(
			Integer[] indexesToSample,
			double percent,
			int minN,
			int maxN
			) {
		return sample(indexesToSample, percent, minN, maxN, new Random().nextInt());
	}
}

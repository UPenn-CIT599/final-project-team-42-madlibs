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
	
	
	// For storing the words as individual strings
	private ArrayList<String> originalWords;
	private ArrayList<String> updatedWords;
	
	// For tracking all parts of speech. Helps when reconstructing text and
	// need to flag punctuation
	private ArrayList<String> posTags;
	
	// For storing the indexes of the different parts of speech
	/* TODO: consider combining with the PartOfSpeech class somehow. 
	 * Maybe make a PartOfSpeech class? (Had that before...)
	 */
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
		originalWords = new ArrayList<String>();
		posTags = new ArrayList<String>();
		
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
				originalWords.add(word);
			}
			// Get the different parts of speech, adding to appropriate object
			for (String pos: sentence.posTags()) {
				posTags.add(pos);
				
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
		updatedWords = (ArrayList<String>) originalWords.clone();
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
		/* Initialize with first word, since when adding additional words
		* we will prepend a space so that words are spaced properly
		*/
		StringBuilder updatedText = new StringBuilder(updatedWords.get(0));
		for (int i = 1; i < updatedWords.size(); i++) {
			String pos = posTags.get(i);
			String word = updatedWords.get(i);
			if (pos.equals(".") || pos.equals(",") || pos.equals(":") || 
				pos.equals("(") || pos.equals(")")) {
				updatedText.append(word);
			}
			else {
				updatedText.append(" " + word);
			}
		}
		return updatedText.toString();
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
		/*
		 * Flow:
		 * loop over words? check length? if a replacement then...
		 * Need to track punctuation. Maybe need a new data structure?
		 * Word object? Has length.. Punctuation? New line..  
		 */
		// Just an example case
		int[][] indexes = {
				{10, 14},
				{35, 39},
				{49, 53}
		};
		return indexes;
	}
	
	/**
	 * Updates the words at the specified indexes with the supplied words.
	 * @param replacementWords The new words that will overwrite the original words
	 * @param indexes The indexes of the original words that should be replaed
	 */
	public void replaceWords(String[] replacementWords, Integer[] indexes) {
		// TODO: Verify that they have the same length?
		for (int i = 0; i < replacementWords.length; i++) {
			updatedWords.set(indexes[i], replacementWords[i]);
		}
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

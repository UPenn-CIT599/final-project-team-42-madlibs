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
	private ArrayList<Word> originalWords;
	private ArrayList<Word> updatedWords;
	
	// For tracking all parts of speech. Helps when reconstructing text and
	// need to flag punctuation
	private ArrayList<String> posTags;
	
	// For storing the indexes of the different parts of speech
	/* TODO: consider combining with the PartOfSpeech enum somehow (would involve
	 * making it a "regular" class instead of an enum. 
	 */
	private PartOfSpeechTracker singularNouns;
	private PartOfSpeechTracker pluralNouns;
	private PartOfSpeechTracker adverbs;
	private PartOfSpeechTracker adjectives;
	private PartOfSpeechTracker ingVerbs;
	private PartOfSpeechTracker edVerbs;
	
	private String originalText;
	
	/**
	 * For storing important information about a word, such as the actual word,
	 * trailing white spaces, and the total length (including preceding
	 * and proceding blankspace. 
	 *
	 */
	private class Word {
		// The actual word, as a string
		private String text;
		// The blank space (including new lines?) that comes after this word;
		private String trailingBlanks;
		// The part of speech tag;
		private String posTag;
		// Used to decide if the first character should be capitilized or not
		private int sentenceIndex;
		private boolean replaced = false;
		/**
		 * Constructs a new Word object from the supplied parameters.
		 * @param word The actual text of the word
		 * @param posTag The part of speech as this word, using the tag from Penn Treebank
		 * @param trailingBlanks The white space characters that follow this word
		 * @param sentenceIndex The position of this word in its sentence.
		 * 
		 */
		Word (String text, String posTag, String trailingBlanks, int sentenceIndex) {
			this.text = text;
			this.posTag = posTag;
			this.sentenceIndex = sentenceIndex;
			this.trailingBlanks = trailingBlanks;
		}
		
		private void setReplaced(boolean replaced) {
			this.replaced = replaced;
		}
		
		private boolean isReplaced() {
			return replaced;
		}
		
		public String toString() {
			// If this is the first character of the sentence, capitalize the first character
			if (sentenceIndex == 0) {
				return text.substring(0, 1).toUpperCase() + text.substring(1) + trailingBlanks;
			}
			else {
				return text + trailingBlanks;
			}
		}
		
		/*
		 * Get the length of this word, including the blank spaces.
		 */
		public int length() {
			return toString().length();
		}
		
		/**
		 * Update the actual string part of the word with the new string
		 * @param text
		 */
		public void setText(String text) {
			this.text = text;
		}
		/**
		 * Update the actual string part of the word with the new string
		 * @param text
		 */
		public String getText() {
			return text;
		}
	}
	
	// For tracking the indexes of the different parts of speech
	class PartOfSpeechTracker {
		// Keys are the string of original words, values are indexes where this word can be found.
		// Keys but not correspond to current word, if replacement had been done
		private HashMap<String, ArrayList<Integer>> indexLookup;
		// For knowing the total number of individual words that belong to this
		// part of speech in this passage
		private int numOccurences = 0;
		
		PartOfSpeechTracker() {
			indexLookup = new HashMap<String, ArrayList<Integer>>();
		}
		
		public void add(String word, int index) {
			if(!indexLookup.containsKey(word)) {
				indexLookup.put(word, new ArrayList<Integer>());
			}
			indexLookup.get(word).add(index);
		}
		
		// Convert a flat array of indexes - sorted. Partially so tests don't break
		public Integer[] toArray() {
			ArrayList<Integer> allIndexes = new ArrayList<Integer>();
			for (String word : indexLookup.keySet()) {
				ArrayList<Integer> theseIndexes = indexLookup.get(word);
				for (Integer index : theseIndexes) {
					allIndexes.add(index);
				}
			}
			Collections.sort(allIndexes);
			return allIndexes.toArray(new Integer[allIndexes.size()]);
		}
		
	}
	
	/** Constructs a new Passage object from the supplied text.
	 * 
	 * @param originalText The text of the passage
	 */
	Passage (String originalText) {
		
	
		this.originalText = originalText;
		originalWords = new ArrayList<Word>();
		posTags = new ArrayList<String>();
		
		singularNouns = new PartOfSpeechTracker();		pluralNouns = new PartOfSpeechTracker();
		adverbs = new PartOfSpeechTracker();
		adjectives = new PartOfSpeechTracker();
		edVerbs = new PartOfSpeechTracker();
		ingVerbs = new PartOfSpeechTracker();
		
		Document document = new Document(originalText);
		
		int passageIndex = 0;
		for (Sentence sentence: document.sentences()) {
			for (int i = 0; i < sentence.length(); i++) {
				String word = sentence.originalText(i);
				String posTag = sentence.posTag(i);
				String trailingBlanks = sentence.after(i);
				originalWords.add(new Word(word, posTag, trailingBlanks, i));
				// Get the different parts of speech, adding to appropriate object
				posTags.add(posTag);
				
				if (posTag.equals("NN") || posTag.equals("NNP")) {
					singularNouns.add(word, passageIndex);
				}
				else if (posTag.equals("NNS") || posTag.equals("NNPS")) {
					pluralNouns.add(word, passageIndex);
				}
				else if (posTag.equals("JJ")) {
					adjectives.add(word, passageIndex);
				}
				else if (posTag.equals("RB")) {
					adverbs.add(word, passageIndex);
				}
				else if (posTag.equals("VBG")) {
					ingVerbs.add(word, passageIndex);
				}
				else if (posTag.equals("VBN")) {
					edVerbs.add(word, passageIndex);
				};
				passageIndex++;
			};
		}
		updatedWords = (ArrayList<Word>) originalWords.clone();
	}
	
	/**
	 * 
	 * @return The original, unmodified, text.
	 */
	public String getOriginalText() {
		return originalText;
	}
	
	/**
	 * @return The modified text that contains user supplied words
	 */
	public String getUpdatedText() {
		/* Initialize with first word, since when adding additional words
		* we will prepend a space so that words are spaced properly
		*/
		StringBuilder updatedText = new StringBuilder();
		for (Word word : updatedWords) {
			updatedText.append(word.toString());
		}
		return updatedText.toString().trim();
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
		Integer index = 0;
		ArrayList<Integer[]> indexesList = new ArrayList<Integer[]>();
		for (Word word : updatedWords) {
			if (word.isReplaced()) {
				indexesList.add(new Integer[] {index, index + word.getText().length() });
			}
			// The length method includes the trailing blank spaces.
			index += word.length();
		}
		// We will return an array, since it is a bit more straightforward to work with
		int[][] indexes = new int[indexesList.size()][2];
		for (int i = 0; i < indexesList.size(); i++) {
			indexes[i][0] = indexesList.get(i)[0];
			indexes[i][1] = indexesList.get(i)[1];
		}
		return indexes;
	}
	
	/**
	 * Updates the words at the specified indexes with the supplied words.
	 * @param replacementWords The new words that will overwrite the original words
	 * @param indexes The indexes of the original words that should be replaced.
	 * This is an integer array of integers, because can replace indexes with the
	 * same word at the same time.
	 */
	public void replaceWords(String[] replacementWords, Integer[][] indexes) {
		for (int i = 0; i < indexes.length; i++) {
			for (int j = 0; j < indexes[i].length; j++) {
				Word wordToUpdate = updatedWords.get(indexes[i][j]);
				wordToUpdate.setText(replacementWords[i]);
				wordToUpdate.setReplaced(true);
			}
		}
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
			return singularNouns.toArray();
		case PLURAL_NOUN:
			return pluralNouns.toArray();
		case ADJECTIVE:
			return adjectives.toArray();
		case ADVERB:
			return adverbs.toArray();
		case ED_VERB:
			return edVerbs.toArray();
		case ING_VERB:
			return ingVerbs.toArray();
		// This should never happen...
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
		
		// Some checks on parameters
		if (minN < 0) {
			throw new IllegalArgumentException("Need to request a minimum of at" +
				"least one word to replace ");
		}
		if (minN > maxN) {
			throw new IllegalArgumentException("Minimum number of requested words is greater" +
				"than maximum!");
		}
		if (percent <= 0 || percent > 1.0) {
			throw new IllegalArgumentException("The percentage of words to replace" +
				"needs to be greater than 0 and no more than 1.0 (100%)");
		}
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

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
	private ArrayList<Integer> singularNouns;
	private ArrayList<Integer> pluralNouns;
	private ArrayList<Integer> adverbs;
	private ArrayList<Integer> adjectives;
	private ArrayList<Integer> ingVerbs;
	private ArrayList<Integer> edVerbs;
	
	private String originalText;
	
	/**
	 * For storing important information about a word, such as the actual word,
	 * the blank space before and after, and the total length (including preceding
	 * and proceding blankspace. 
	 *
	 */
	private class Word {
		// The actual word, as a string
		private String text;
		// The blank space (including new lines) that comes before this word;
		private String precedingBlanks;
		// The blank space (including new lines?) that comes after this word;
		private String trailingBlanks;
		// The part of speech tag;
		private String posTag;
		private boolean replaced = false;
		/**
		 * Constructs a new Word object from the supplied parameters.
		 * @param string The actual word
		 * @param beforeBlank The part of speech tag;
		 */
		Word (String text, String posTag) {
			this.text = text;
			this.posTag = posTag;
			// It is unfortunate that the ".after()" and ".before()" methods
			// from the Sentence API don't appear to return the actual whitespace.
			// I would expect newlines to be counted as whitespace, and don't expect
			// a blank to appear before a period.
			// Guess we will need to figure out on own.
			trailingBlanks = "";
			if (!isPunctuation()) {
				precedingBlanks = " ";
			}
			else {
				precedingBlanks = "";
			}
		}
		// Alternative constructor that provides sets preceding blanks to an empty string
		// and instead relies on trailing blanks;
		Word (String text, String posTag, String trailingBlanks) {
			this(text, posTag);
			this.precedingBlanks = "";
			this.trailingBlanks = trailingBlanks;
		}
		
		private boolean isPunctuation() {
			if (posTag.equals(",") || posTag.equals(".") || posTag.equals("'") ||
					posTag.equals("\"") || posTag.equals(":")) {
				return true;
			}
			else {
				return false;
			}
		}
		
		private void setReplaced(boolean replaced) {
			this.replaced = replaced;
		}
		
		private boolean isReplaced() {
			return replaced;
		}
		
		public String toString() {
			return precedingBlanks + text + trailingBlanks;
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
	
	/** Constructs a new Passage object from the supplied text.
	 * 
	 * @param originalText The text of the passage
	 */
	Passage (String originalText) {
		
	
		this.originalText = originalText;
		originalWords = new ArrayList<Word>();
		posTags = new ArrayList<String>();
		
		singularNouns = new ArrayList<Integer>();
		pluralNouns = new ArrayList<Integer>();
		adverbs = new ArrayList<Integer>();
		adjectives = new ArrayList<Integer>();
		edVerbs = new ArrayList<Integer>();
		ingVerbs = new ArrayList<Integer>();
		
		Document document = new Document(originalText);
		
		int passageIndex = 0;
		for (Sentence sentence: document.sentences()) {
			int sentenceIndex = 0;
			for (int i = 0; i < sentence.length(); i++) {
				String word = sentence.originalText(i);
				String posTag = sentence.posTag(i);
				String trailingBlanks = sentence.after(i);
				originalWords.add(new Word(word, posTag, trailingBlanks));
				// Get the different parts of speech, adding to appropriate object
				posTags.add(posTag);
				
				if (posTag.equals("NN") || posTag.equals("NNP")) {
					singularNouns.add(passageIndex);
				}
				else if (posTag.equals("NNS") || posTag.equals("NNPS")) {
					pluralNouns.add(passageIndex);
				}
				else if (posTag.equals("JJ")) {
					adjectives.add(passageIndex);
				}
				else if (posTag.equals("RB")) {
					adverbs.add(passageIndex);
				}
				else if (posTag.equals("VBG")) {
					ingVerbs.add(passageIndex);
				}
				else if (posTag.equals("VBN")) {
					edVerbs.add(passageIndex);
				};
				sentenceIndex++;
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
		/*
		 * Flow:
		 * loop over words? check length? if a replacement then...
		 * Need to track punctuation. Maybe need a new data structure?
		 * Word object? Has length.. Punctuation? New line..  
		 */
		// Just an example case
		// For keeping track of index.
		Integer index = 0;
		ArrayList<Integer[]> indexesList = new ArrayList<Integer[]>();
		for (Word word : updatedWords) {
			if (word.isReplaced()) {
				indexesList.add(new Integer[] {index, index + word.getText().length() - 1});
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
	 * @param indexes The indexes of the original words that should be replaced
	 */
	public void replaceWords(String[] replacementWords, Integer[] indexes) {
		// TODO: Verify that they have the same length?
		for (int i = 0; i < replacementWords.length; i++) {
			Word wordToUpdate = updatedWords.get(indexes[i]);
			wordToUpdate.setText(replacementWords[i]);
			wordToUpdate.setReplaced(true);
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
			throw new IllegalArgumentException("Need to request a minium of at" +
				"least one word to replace ");
		}
		if (minN > maxN) {
			throw new IllegalArgumentException("Mininum number of requested words is greater" +
				"than maximum!");
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

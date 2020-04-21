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
	private ArrayList<Word> modifiedWords;
	
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
	
	/**
	 * For storing important information about a word, such as the actual word,
	 * trailing white spaces, and the total length (including trailing whitespace). 
	 *
	 */
	private class Word {
		// The actual word, as a string
		private String text;
		// The blank space (including new lines?) that comes after this word;
		private String trailingBlanks;
		// Used to decide if the first character should be capitalized or not
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
		private int length() {
			return toString().length();
		}
		
		/**
		 * Update the actual string part of the word with the new string
		 * @param text
		 */
		private void setText(String text) {
			this.text = text;
		}
		/**
		 * Update the actual string part of the word with the new string
		 * @param text
		 */
		private String getText() {
			return text;
		}
	}
	
	// For tracking the indexes of the different parts of speech
	class PartOfSpeechTracker {
		// Keys are the string of original words, values are indexes where this word can be found.
		// Keys but not correspond to current word, if replacement had been done
		private HashMap<String, ArrayList<Integer>> indexLookup;
		PartOfSpeechTracker() {
			indexLookup = new HashMap<String, ArrayList<Integer>>();
		}
		
		private void add(String word, int index) {
			if(!indexLookup.containsKey(word)) {
				indexLookup.put(word, new ArrayList<Integer>());
			}
			indexLookup.get(word).add(index);
		}
		
		/**
		 * Return the indexes where the corresponding parts of speech can be
		 * found in the passage as a flat Integer array.
		 * @return An 1d Integer array of the indexes that belong to this
		 * part of speech, sorted.
		 */
		public Integer[] toFlatArray() {
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
		/**
		 * Return the indexes where the corresponding parts of speech can be
		 * found in the passage as a 2d Integer array.
		 * @return A 2d Integer array of the indexes that belong to this 
		 * part of speech.
		 */
		public Integer[][] toNestedArray() {
			Integer[][] allIndexes = new Integer[indexLookup.values().size()][];
			int i = 0;
			for (ArrayList<Integer> indexes : indexLookup.values()) {
				allIndexes[i] = indexes.toArray(new Integer[indexes.size()]);
				i++;
			}
			return allIndexes;
		}
		
	}
	
	/** Constructs a new Passage object from the supplied text.
	 * 
	 * @param originalText The text of the passage
	 */
	Passage (String originalText) {
		
	
		originalWords = new ArrayList<Word>();
		modifiedWords = new ArrayList<Word>();
		
		
		singularNouns = new PartOfSpeechTracker();
		pluralNouns = new PartOfSpeechTracker();
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
				// Adding a separate word object to both original and updated word
				// lists so can modify a Word object in one list without affecting
				// the other
				originalWords.add(new Word(word, posTag, trailingBlanks, i));
				modifiedWords.add(new Word(word, posTag, trailingBlanks, i));
				// Get the different parts of speech, adding to appropriate object
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
	}
	
	/**
	 * 
	 * @return The original, unmodified, text.
	 */
	public String getOriginalText() {
		return reconstructText(originalWords);
	}
	
	// Strings together the string representations Word objects into a cohesive
	// string
	private String reconstructText(ArrayList<Word> deconstructedText) {
		StringBuilder updatedText = new StringBuilder();
		for (Word word : deconstructedText) {
			updatedText.append(word.toString());
		}
		return updatedText.toString();
	}
	
	/**
	 * @return The modified text that contains user supplied words
	 */
	public String getUpdatedText() {
		return reconstructText(modifiedWords);
	}
	
	/**
	 * @return Gets the indexes of within the supplied ArrayList of Words of the
	 * words that have been replaced. This is a 2-dimensional integer array,
	 * n x 2, where n is the the number of words that have been replaced. The
	 * first element of every row gives the place where the word starts in the
	 * passage, and the second element give the place where the word ends.
	 */
	private int[][] getIndexesOfReplacements(ArrayList<Word> individualWords) {
		Integer index = 0;
		ArrayList<Integer[]> indexesList = new ArrayList<Integer[]>();
		for (Word word : individualWords) {
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
	 * @return Gets the indexes of within the modified text of the words that
	 * have been replaced. This is a 2-dimensional integer array, n x 2, where
	 * n is the the number of words that have been replaced. The first element
	 * of every row gives the place where the word starts in the passage, and
	 * the second element give the place where the word ends.
	 */
	public int[][] getIndexesOfReplacedWords() {
		return getIndexesOfReplacements(modifiedWords);
	}
	
	/**
	 * @return Gets the indexes of within the original text of the words that
	 * have been replaced. This is a 2-dimensional integer array, n x 2, where
	 * n is the the number of words that have been replaced. The first element
	 * of every row gives the place where the word starts in the passage, and
	 * the second element give the place where the word ends.
	 */
	public int[][] getIndexesOfOriginalWords() {
		return getIndexesOfReplacements(originalWords);
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
				Word wordToUpdate = modifiedWords.get(indexes[i][j]);
				wordToUpdate.setText(replacementWords[i]);
				wordToUpdate.setReplaced(true);
				// Also mark in original text if word has been replaced, so can
				// get indexes for highlighting
				originalWords.get(indexes[i][j]).setReplaced(true);
			}
		}
	}
	
	/**
	 * Returns the requested PartOfSpeechTracker object.
	 * @param partOfSpeech A valid part of speech from the PartOfSpeech Enum
	 * @return A PartOfSpeechTracker objects that contains the indexes for the
	 * specified PartOfSpeech
	 */
	public PartOfSpeechTracker getPartOfSpeech(PartOfSpeech partOfSpeech) {
		switch(partOfSpeech) {
		case SINGULAR_NOUN:
			return singularNouns;
		case PLURAL_NOUN:
			return pluralNouns;
		case ADJECTIVE:
			return adjectives;
		case ADVERB:
			return adverbs;
		case ED_VERB:
			return edVerbs;
		case ING_VERB:
			return ingVerbs;
		// This should never happen...
		default:
			throw new IllegalArgumentException("Invalid part of speech");
		}
	}

}

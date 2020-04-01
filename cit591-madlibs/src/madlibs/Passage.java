package madlibs;

import java.util.*;
import edu.stanford.nlp.simple.*;

/**
 * This class represents a passage of text in a suitable format for playing a
 * game of MadLibs. It chooses which words will be replaced and accepts replacement words.
 * Will return the newly modifed text along with the indexes of the modified words
 * so that they can be highlighted. 
 * @author ross
 *
 */
public class Passage {
	
	private ArrayList<String> words;
	
	private PartsOfSpeech singularNouns;
	private PartsOfSpeech pluralNouns;
	private PartsOfSpeech adverbs;
	private PartsOfSpeech adjectives;
	private PartsOfSpeech ingVerbs;
	private PartsOfSpeech edVerbs;
	
	private String originalText;
	
	//constants that will parameterize the sampling of the different parts of speech
	private final int MIN_SINGULAR_NOUNS = 2;
	private final int MAX_SINGULAR_NOUNS = 10;
	private final double PERCENT_SINGULAR_NOUNS = 0.25;
	
	private final int MIN_PLURAL_NOUNS = 2;
	private final int MAX_PLURAL_NOUNS = 10;
	private final double PERCENT_PLURAL_NOUNS = 0.25;
	
	private final int MIN_ADJECTIVES = 2;
	private final int MAX_ADJECTIVES = 10;
	private final double PERCENT_ADJECTIVES = 0.25;
	
	private final int MIN_ADVERBS = 2;
	private final int MAX_ADVERBS = 10;
	private final double PERCENT_ADVERBS = 0.25;
	
	private final int MIN_EDVERBS = 2;
	private final int MAX_EDVERBS = 10;
	private final double PERCENT_EDVERBS = 0.25;
	
	private final int MIN_INGVERBS = 2;
	private final int MAX_INGVERBS = 10;
	private final double PERCENT_INGVERBS = 0.25;
	/** Constructs a new Passage object from the supplied text.
	 * 
	 * @param originalText The text of the passage
	 */
	Passage (String originalText) {
		this.originalText = originalText;
		words = new ArrayList<String>();
		
		singularNouns = new PartsOfSpeech();
		pluralNouns = new PartsOfSpeech();
		adverbs = new PartsOfSpeech();
		adjectives = new PartsOfSpeech();
		edVerbs = new PartsOfSpeech();
		ingVerbs = new PartsOfSpeech();
		
		Document document = new Document(originalText);
		
		int index = 0;
		for (Sentence sentence: document.sentences()) {
			for (String word: sentence.words()) {
				words.add(word);
			}
			// Get the different parts of speech, adding to appropiate object
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
			}
		}
		// Now sample
		singularNouns.sample(
				PERCENT_SINGULAR_NOUNS,
				MIN_SINGULAR_NOUNS,
				MAX_SINGULAR_NOUNS
		);
		pluralNouns.sample(
				PERCENT_PLURAL_NOUNS,
				MIN_PLURAL_NOUNS,
				MAX_PLURAL_NOUNS
		);
		adjectives.sample(
				PERCENT_ADJECTIVES,
				MIN_ADJECTIVES,
				MAX_ADJECTIVES
		);
		adverbs.sample(
				PERCENT_ADVERBS,
				MIN_ADVERBS,
				MAX_ADVERBS
		);
		edVerbs.sample(
				PERCENT_EDVERBS,
				MIN_EDVERBS,
				MAX_EDVERBS
		);
		ingVerbs.sample(
				PERCENT_INGVERBS,
				MIN_INGVERBS,
				MAX_INGVERBS
		);
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
		return "";
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
		int[][] indexes = {{0,0}};
		return indexes;
	}
	
	/**
	 * @return The number of singular nouns that the user should supply.
	 */
	public int getNumberOfSingularNounsToReplace() {
		return singularNouns.getNumberOfReplacementWords();
	}
	/**
	 * @return The indexes of all the singular nouns in this passage.
	 */
	public Integer[] getIndexesOfSingularNouns() {
		return singularNouns.getIndexesOfAllWords();
	}
	/**
	 * Replaces the identified singular nouns with the replacements from the user. 
	 * @param userSuppliedWords
	 */
	public void replaceSingularNouns(String[] userSuppliedWords) {
		
	}
	
	/**
	 * @return The number of plural nouns that the user should supply.
	 */
	public int getNumberOfPluralNounsToReplace() {
		return pluralNouns.getNumberOfReplacementWords();
	}
	
	/**
	 * @return The indexes of all the plural nouns in this passage.
	 */
	public Integer[] getIndexesOfPluralNouns() {
		return pluralNouns.getIndexesOfAllWords();
	}
	
	/**
	 * Replaces the identified plural nouns with the replacements from the user. 
	 * @param userSuppliedWords
	 */
	public void replacePluralNouns(String[] userSuppliedWords) {
		
	}
	
	/**
	 * @return The number of adjectives that the user should supply.
	 */
	public int getNumberOfAdjectivesToReplace() {
		return adjectives.getNumberOfReplacementWords();
	}
	
	/**
	 * Replaces the identified adjectives with the replacements from the user. 
	 * @param userSuppliedWords
	 */
	public void replaceAdjectives(String[] userSuppliedWords) {
		
	}
	
	/**
	 * @return The number of adverbs that the user should supply.
	 */
	public int getNumberOfAdverbsToReplace() {
		return adverbs.getNumberOfReplacementWords();
	}
	/**
	 * Replaces the identified adverbs with the replacements from the user. 
	 * @param userSuppliedWords
	 */
	public void replaceAdverbs(String[] userSuppliedWords) {
		
	}
	/**
	 * @return The number of "ed" verbs that the user should supply.
	 */
	public int getNumberOfEdVerbsToReplace() {
		return edVerbs.getNumberOfReplacementWords();
	}
	/**
	 * Replaces the identified "ed" verbs with the replacements from the user. 
	 * @param userSuppliedWords
	 */
	public void replaceEdVerbs(String[] userSuppliedWords) {
		
	}
	
	/**
	 * @return The number of "ing" verbs that the user should supply.
	 */
	public int getNumberOfIngVerbsToReplace() {
		return ingVerbs.getNumberOfReplacementWords();
	}
	/**
	 * Replaces the identified "ing" verbs with the replacements from the user. 
	 * @param userSuppliedWords
	 */
	public void replaceIngVerbs(String[] userSuppliedWords) {
		
	}
	/**
	 * Get the words of this TaggedText object as an array.
	 * @return An array of the words (tokens?) in this TaggedText object. Making certain
	 * to return a copy, not the underlying, mutable, list.
	 */
	public String[] getWords() {
		return words.toArray(new String[words.size()]);
	}
	
}

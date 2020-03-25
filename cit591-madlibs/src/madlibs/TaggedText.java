package madlibs;

import java.util.*;
import edu.stanford.nlp.simple.*;

/**
 * This class stores text, splits it into words, tags these words into nouns, adjectives, adverbs and verbs.
 * It can also reconstruct the original text. Maybe it can also accept overides? - Flagging the override?
 * I think that the reconstruction should be done in another class. Maybe. Worry about that later.
 * @author ross
 *
 */
public class TaggedText {
	
	private ArrayList<String> words;
	private ArrayList<Integer> singularNounIndexes;
	
	
	/** Constructs a new Text Tagger class
	 * 
	 * @param text The text to parse
	 */
	TaggedText (String text) {
		words = new ArrayList<String>();
		singularNounIndexes = new ArrayList<Integer>();
		
		Document document = new Document(text);
		for (Sentence sentence: document.sentences()) {
			for (String word: sentence.words()) {
				words.add(word);
			}
			// Get the different parts of speech
			int index = 0;
			for (String pos: sentence.posTags()) {
				if (pos.equals("NN")) {
					singularNounIndexes.add(index);
				};
				index++;
			}
		}
	}
	
	/**
	 * Get the words of this TaggedText object as an array.
	 * @return An array of the words (tokens?) in this TaggedText object. Making certain
	 * to return a copy, not the underlying, mutable, list.
	 */
	public String[] getWords() {
		return words.toArray(new String[words.size()]);
	}
	
	/**
	 * Get the indexes of the singular nouns in this TaggedText object as an array.
	 * @return An integer array of the indexes of the singular nouns. Making certain
	 * to return a copy, not the underlying, mutable, list.
	 */
	public Integer[] getSingularNounIndexes() {
		return singularNounIndexes.toArray(new Integer[singularNounIndexes.size()]);
	}
	
	public static void main(String[] args) {
		String testText = "To be yourself in a world that is constantly trying to make you something else is the greatest accomplishment";
		TaggedText taggedText = new TaggedText(testText);
		String[] words = taggedText.getWords();
		for (String word: words) {
			System.out.println(word);
		}
		Integer[] singularNounIndexes = taggedText.getSingularNounIndexes();
		for (Integer index: singularNounIndexes) {
			System.out.println(index);
		}
	}

}

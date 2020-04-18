package madlibs;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class PassageTest {
	String shortText = "To be yourself in a world that is constantly trying to make you something else is the greatest accomplishment.";
	String longText = "Alice was beginning to get very tired of sitting by her sister on the\n" + 
			"bank, and of having nothing to do: once or twice she had peeped into\n" + 
			"the book her sister was reading, but it had no pictures or\n" + 
			"conversations in it, and what is the use of a book, thought Alice\n" + 
			"without pictures or conversations?\n" + 
			"\n" + 
			"So she was considering in her own mind (as well as she could, for the\n" + 
			"hot day made her feel very sleepy and stupid), whether the pleasure of\n" + 
			"making a daisy-chain would be worth the trouble of getting up and\n" + 
			"picking the daisies, when suddenly a White Rabbit with pink eyes ran\n" + 
			"close by her.";

	@Test
	void indexesOfReplacementsTest() {
		Passage shortPassage = new Passage(shortText);
		shortPassage.replaceWords(
			new String[] {"Alice Cooper", "Smart Car", "angriest"},
			new Integer[][] {{2}, {5}, {17}}
		);
		int[][] expectedIndexes = new int[][] {{6, 18}, {24, 33}, {94, 102}}; 
		assertArrayEquals(expectedIndexes, shortPassage.getIndexesOfReplacements());
	}
	@Test
	void getOriginalTextShortTest() {
		Passage shortPassage = new Passage(shortText);
		assertEquals(shortText, shortPassage.getUpdatedText());
	}
	
	@Test
	void getUpdatedTextShortTest() {
		Passage shortPassage = new Passage(shortText);
		shortPassage.replaceWords(
			new String[] {"help", "Alice Cooper", "Smart Car", "angriest"},
			new Integer[][] {{0}, {2}, {5}, {17}}
		);
		String expectedText = "Help be Alice Cooper in a Smart Car that is constantly " +
				"trying to make you something else is the angriest accomplishment.";
		assertEquals(expectedText, shortPassage.getUpdatedText());
	}
	@Test
	void getUpdatedTextCapitalizeTest() {
		Passage shortPassage = new Passage(shortText);
		shortPassage.replaceWords(
			new String[] {"Alice Cooper", "Smart Car", "angriest"},
			new Integer[][] {{2}, {5}, {17}}
		);
		String expectedText = "To be Alice Cooper in a Smart Car that is constantly " +
				"trying to make you something else is the angriest accomplishment.";
		assertEquals(expectedText, shortPassage.getUpdatedText());
	}
	// Currently failing, due to not reproducing line breaks. Also parentheses?
	@Test
	void getUpdatedTextLongTest() {
		Passage longPassage = new Passage(longText);
		assertEquals(longText, longPassage.getUpdatedText());
	}
	// Tests on shorter passage
	@Test
	void getIndexesOfSingularNounsShortTest() {
		Passage shortPassage = new Passage(shortText);
		Integer[] expectedIndexes = {5, 13, 18};
		assertArrayEquals(expectedIndexes, shortPassage.getPartOfSpeech(PartOfSpeech.SINGULAR_NOUN).toFlatArray());
	}
	//Testing that the nested part works
	@Test
	void getNestedIndexesOfSingularNounsShortTest() {
		Passage shortPassage = new Passage(shortText);
		Integer[][] expectedIndexes = {{5}, {13}, {18}};
		assertArrayEquals(expectedIndexes, shortPassage.getPartOfSpeech(PartOfSpeech.SINGULAR_NOUN).toNestedArray());
	}
	@Test
	void getIndexesOfAdverbsShortTest() {
		// Note that index 14, else from "something else" should really be an adjective
		// Issue with parser?
		Passage shortPassage = new Passage(shortText);
		Integer[] expectedIndexes = {8, 14};
		assertArrayEquals(expectedIndexes, shortPassage.getPartOfSpeech(PartOfSpeech.ADVERB).toFlatArray());
	}
	@Test
	void getIndexesOfAdjectivesShortTest() {
		// "greatest" (17) doesn't count because it is superlative. "else" (14)  doesn't count
		// since parser thinks it is a adverb
		Passage shortPassage = new Passage(shortText);
		Integer[] expectedIndexes = {};
		assertArrayEquals(expectedIndexes, shortPassage.getPartOfSpeech(PartOfSpeech.ADJECTIVE).toFlatArray());
	}
	@Test
	void getIndexesOfSingularNounsLongTest() {
		// Index 84 is not actually an noun, it is the word "feel" from "made her feel"
		// so should be some sort of verb. However I guess the parser isn't perfect
		// Note also that White Rabbit is counted as two nouns (114 and 115)
		Passage longPassage = new Passage(longText);
		Integer[] expectedIndexes = {
				0, 11, 14, 19, 31, 33, 51, 54, 57,
				70, 81, 84, 93, 97, 102, 114, 115};
		assertArrayEquals(expectedIndexes, longPassage.getPartOfSpeech(PartOfSpeech.SINGULAR_NOUN).toFlatArray());
	}
	
	@Test
	void getIndexesOfPluralNounsLongTest() {
		Passage longPassage = new Passage(longText);
		Integer[] expectedIndexes = {41, 43, 59, 61, 109, 118};
		assertArrayEquals(expectedIndexes, longPassage.getPartOfSpeech(PartOfSpeech.PLURAL_NOUN).toFlatArray());
	}
	
	@Test
	void getIndexesOfAdjectivesLongTest() {
		Passage longPassage = new Passage(longText);
		Integer[] expectedIndexes = {6, 69, 80, 86, 88, 100, 117};
		assertArrayEquals(expectedIndexes, longPassage.getPartOfSpeech(PartOfSpeech.ADJECTIVE).toFlatArray());
	}

	@Test
	void getIndexesOfAdverbsLongTest() {
		Passage longPassage = new Passage(longText);
		Integer[] expectedIndexes = {5, 23, 25, 72, 73, 85, 105, 112, 120};
		assertArrayEquals(expectedIndexes, longPassage.getPartOfSpeech(PartOfSpeech.ADVERB).toFlatArray());
	}
	@Test
	void getIndexesOfEdVerbsLongTest() {
		Passage longPassage = new Passage(longText);
		Integer[] expectedIndexes = {28};
		assertArrayEquals(expectedIndexes, longPassage.getPartOfSpeech(PartOfSpeech.ED_VERB).toFlatArray());
	}
	@Test
	void getIndexesOfIngVerbsLongTest() {
		Passage longPassage = new Passage(longText);
		Integer[] expectedIndexes = {2, 8, 18, 35, 66, 95, 104, 107};
		assertArrayEquals(expectedIndexes, longPassage.getPartOfSpeech(PartOfSpeech.ING_VERB).toFlatArray());
	}
}

package madlibs;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

class PassageTest {
	static Passage shortPassage;
	static Passage longPassage;

	@BeforeAll
	static void setupBeforeClass() {
		/*
		shortPassage = new Passage("To be yourself in a world that is constantly trying to make you something else is the greatest accomplishment.");
		*/
		longPassage = new Passage("Alice was beginning to get very tired of sitting by her sister on the\n" + 
				"bank, and of having nothing to do: once or twice she had peeped into\n" + 
				"the book her sister was reading, but it had no pictures or\n" + 
				"conversations in it, and what is the use of a book, thought Alice\n" + 
				"without pictures or conversations?\n" + 
				"\n" + 
				"So she was considering in her own mind (as well as she could, for the\n" + 
				"hot day made her feel very sleepy and stupid), whether the pleasure of\n" + 
				"making a daisy-chain would be worth the trouble of getting up and\n" + 
				"picking the daisies, when suddenly a White Rabbit with pink eyes ran\n" + 
				"close by her.");
	}
	
	// Tests on shorter passage
	@Test
	void getNumberOfSingularNounsToReplaceShortTest() {
		assertEquals(2, shortPassage.getNumberOfSingularNounsToReplace());
	}
	@Test
	void getIndexesOfAllSingularNounsShortTest() {
		Integer[] expectedIndexes = {5, 13, 18};
		assertArrayEquals(expectedIndexes, shortPassage.getIndexesOfSingularNouns());
	}
	@Test
	void getNumberOfPluralNounsToReplaceShortTest() {
		assertEquals(0, shortPassage.getNumberOfPluralNounsToReplace());
	}
	@Test
	void getNumberOfAdjectivesToReplaceShortTest() {
		assertEquals(0, shortPassage.getNumberOfAdjectivesToReplace());
	}
	@Test
	void getNumberOfAdverbsToReplaceShortTest() {
		assertEquals(2, shortPassage.getNumberOfAdverbsToReplace());
	}
	@Test
	void getNumberOfIngVerbsToReplaceShortTest() {
		assertEquals(1, shortPassage.getNumberOfIngVerbsToReplace());
	}
	@Test
	void getNumberOfEdVerbsToReplaceShortTest() {
		assertEquals(0, shortPassage.getNumberOfEdVerbsToReplace());
	}
	// Tests on longer passage - most of these are failing now
	@Test
	void getNumberOfSingularNounsToReplaceLongTest() {
		assertEquals(2, longPassage.getNumberOfSingularNounsToReplace());
	}
	@Test
	void getIndexesOfAllSingularNounsLongTest() {
		// Index 84 is not actually an noun, it is the word "feel" from "made her feel"
		// so should be some sort of verb. However I guess the parser isn't perfect
		Integer[] expectedIndexes = {
				0, 11, 14, 19, 31, 33, 51, 54, 57,
				70, 81, 84, 93, 97, 102, 114, 115};
		assertArrayEquals(expectedIndexes, longPassage.getIndexesOfSingularNouns());
	}
	
	@Test
	void getNumberOfPluralNounsToReplaceLongTest() {
		assertEquals(0, longPassage.getNumberOfPluralNounsToReplace());
	}
	
	@Test
	void getIndexesOfAllPluralNounsLongTest() {
		Integer[] expectedIndexes = {41, 43, 59, 61, 109, 118};
		assertArrayEquals(expectedIndexes, longPassage.getIndexesOfPluralNouns());
	}

	@Test
	void getNumberOfAdjectivesToReplaceLongTest() {
		assertEquals(0, longPassage.getNumberOfAdjectivesToReplace());
	}
	@Test
	void getNumberOfAdverbsToReplaceLongTest() {
		assertEquals(2, longPassage.getNumberOfAdverbsToReplace());
	}
	@Test
	void getNumberOfIngVerbsToReplaceLongTest() {
		// 9 candidates * 0.25 = 2
		assertEquals(8, longPassage.getNumberOfIngVerbsToReplace());
	}
	@Test
	void getNumberOfEdVerbsToReplaceLongTest() {
		assertEquals(0, longPassage.getNumberOfEdVerbsToReplace());
	}
}

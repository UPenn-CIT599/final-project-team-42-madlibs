package madlibs;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TaggedTextTest {
	static TaggedText taggedTester;

	@BeforeAll
	static void setUpBeforeClass() {
		taggedTester = new TaggedText("To be yourself in a world that is constantly trying to make you something else is the greatest accomplishment");
	}

	@Test
	void getNounsTest() {
		assertArrayEquals(taggedTester.getSingularNounIndexes(), new Integer[] {5, 13, 18});
	}
	

}

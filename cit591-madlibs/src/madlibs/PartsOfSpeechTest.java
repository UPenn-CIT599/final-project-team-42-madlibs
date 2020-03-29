package madlibs;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PartsOfSpeechTest {

	@Test
	void sampleTenTest() {
		PartsOfSpeech testPOS = new PartsOfSpeech();
		testPOS.add(0);
		testPOS.add(1);
		testPOS.add(2);
		testPOS.add(3);
		testPOS.add(4);
		testPOS.add(5);
		testPOS.add(6);
		testPOS.add(7);
		testPOS.add(8);
		testPOS.add(9);
		testPOS.sample(0.33, 1, 5, 20200329);
		assertArrayEquals(new Integer[] {4, 7, 0}, testPOS.getIndexesOfReplacementWords());
	}
	@Test
	void countTenTest() {
		PartsOfSpeech testPOS = new PartsOfSpeech();
		testPOS.add(0);
		testPOS.add(1);
		testPOS.add(2);
		testPOS.add(3);
		testPOS.add(4);
		testPOS.add(5);
		testPOS.add(6);
		testPOS.add(7);
		testPOS.add(8);
		testPOS.add(9);
		testPOS.sample(0.33, 1, 5, 20200329);
		assertEquals(3, testPOS.getNumberOfReplacementWords());
	}

}

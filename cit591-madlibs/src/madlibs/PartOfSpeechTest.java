package madlibs;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PartOfSpeechTest {

	@Test
	void compareMaxMinTest() {
		assert(PartOfSpeech.SINGULAR_NOUN.getMaxN() >= PartOfSpeech.SINGULAR_NOUN.getMinN());
	}

}

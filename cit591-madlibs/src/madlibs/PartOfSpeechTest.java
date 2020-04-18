package madlibs;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PartOfSpeechTest {

	// Simple test, since screwed up when developing
	@Test
	void compareMaxMinTest() {
		assert(PartOfSpeech.SINGULAR_NOUN.getDesiredPercent() > 0);
	}

}

package madlibs;

/**
 * A Enum for storing the different parts of speech that the user will
 * be prompted to provide replacements for.
 *
 */
public enum PartOfSpeech {
	SINGULAR_NOUN("singular nouns", 0.2),
	PLURAL_NOUN("plural nouns", 0.2),
	ADJECTIVE("adjectives", 0.33),
	ADVERB("adverbs", 0.2),
	ED_VERB("past tense verbs ending in 'ed'", 0.33),
	ING_VERB("present tense verb ending in 'ing'", 0.33);
	
	private String description;
	// "Suggest" percent of this part of speech to sample
	private double desiredPercent;
	
	PartOfSpeech(String description, double desiredPercent) {
		this.description = description;
		this.desiredPercent = desiredPercent;
	}
	/**
	 * @return A description of this part of speech that can be shown to the player
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @return The suggested percent of this part of speech that should be sampled
	 */
	public double getDesiredPercent() {
		return desiredPercent;
	}
}

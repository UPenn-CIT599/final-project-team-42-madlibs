package madlibs;

/**
 * A Enum for storing the different parts of speech that the user will
 * be prompted to provide replacements for.
 *
 */
public enum PartOfSpeech {
	SINGULAR_NOUN("singular nouns", 0.2, 5, 2),
	PLURAL_NOUN("plural nouns", 0.2, 5, 1),
	ADJECTIVE("adjectives", 0.33, 5, 1),
	ADVERB("adverbs", 0.33, 5, 1),
	ED_VERB("past tense verbs ending in 'ed'", 0.33, 5, 2),
	ING_VERB("present tense verb ending in 'ing'", 0.33, 5, 2);
	
	private String description;
	// "Suggest" percent of this part of speech to sample
	private double percent;
	// "Suggested" max number of this part of speech to sample
	private int maxN;
	// "Suggested" min number of this part of speech to sample
	private int minN;
	
	PartOfSpeech(String description, double percent, int maxN, int minN) {
		this.description = description;
		this.maxN = maxN;
		this.minN = minN;
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
	public double getPercent() {
		return percent;
	}
	/**
	 * @return The suggested maximum number of this part of speech that should be sampled
	 */
	public int getMaxN() {
		return maxN;
	}
	/**
	 * @return The suggested minimum number of this part of speech that should be sampled
	 */
	public int getMinN() {
		return minN;
	}
}

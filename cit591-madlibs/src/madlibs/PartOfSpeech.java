package madlibs;

/**
 * A Enum for storing the different parts of speech that the user will
 * be prompted to provide replacements for.
 *
 */
public enum PartOfSpeech {
	SINGULAR_NOUN("Singular noun", 0.2, 5, 2),
	PLURAL_NOUN("Plural noun", 0.2, 5, 1),
	ADJECTIVE("Adjective", 0.33, 5, 1),
	ADVERB("Adverb", 0.33, 5, 1),
	ED_VERB("A past verb ending in 'ed'", 0.33, 5, 2),
	ING_VERB("Present verb ending in 'ing'", 0.33, 5, 2);
	
	private String description;
	// "Suggest" percent of this part of speech to sample
	private double percent;
	// "Suggested" max number of this part of speech to sample
	private int maxN;
	// "Suggested" min number of this part of speech to sample
	private int minN;
	
	PartOfSpeech(String description, double percent, int maxN, int minN) {
		this.description = description;
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

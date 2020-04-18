package madlibs;

import java.util.*;

// Samples based on numer of occurences
public class WeightedSampler {
	
	ArrayList<Integer[]> indexesToSample;
	int totalNumberOfIndexes = 0;
	
	WeightedSampler(Integer[][] indexesToSample) {
		// Good way of ensuring that don't mutate original object
		this.indexesToSample = new ArrayList<Integer[]>();
		for (Integer[] wordIndexes : indexesToSample) {
			this.indexesToSample.add(wordIndexes);
			totalNumberOfIndexes += wordIndexes.length;
		}
	}
	
	
	// Always get up to or above desired percent
	// Need a seed.... (later)
	private Integer[][] sample(double desiredPercent, Random random) {
		// Make copy so can modify without mutating original
		ArrayList<Integer[]> indexesToSample = (ArrayList<Integer[]>) this.indexesToSample.clone();
		ArrayList<Integer[]> sampledIndexes = sample(
			indexesToSample,
			desiredPercent,
			random
		);
		
		return sampledIndexes.toArray(new Integer[sampledIndexes.size()][]);
	}
	
	public Integer[][] sample(double desiredPercent, long seed) {
		Random random = new Random(seed);
		return sample(desiredPercent, random);
	}
	
	private ArrayList<Integer[]> sample(
		ArrayList<Integer[]> indexesToSample,
		double desiredPercent,
		Random random) {
		
		// Will populate
		ArrayList<Integer[]> sampledIndexes = new ArrayList<Integer[]>();
		
		// index for looping - start randomly
		// Subtracting 1 since will
		int i = random.nextInt(indexesToSample.size());
		double weightThreshold = random.nextDouble() * desiredPercent; // Bounding by desired percent
		// Loop untilWeightThreshold is less than 0;
		Integer[] currentIndexes;
		double currentWeight;
		while (true) {
			currentIndexes = indexesToSample.get(i);
			currentWeight = (double) currentIndexes.length / totalNumberOfIndexes;
			weightThreshold -= currentWeight;
			if (weightThreshold <= 0) {
				break;
			}
			// issue: don't want to increment right away
			i++;
			if (i >= indexesToSample.size()) {
				i = 0;
			}
		}
		// Removed the sampled set of indexes so they don't get chosen again
		indexesToSample.remove(i);
		// Update how much percentage needs to be sampled
		desiredPercent -= currentWeight;
		
		sampledIndexes.add(currentIndexes);
		if (desiredPercent > 0) {
			sampledIndexes.addAll(
				sample(
					indexesToSample,
					desiredPercent,
					random
				)
			);
		}
		return sampledIndexes;
	}

}

package madlibs;

import java.util.*;

/**
 * This class can sample a 2d array of Integers so that the percentage of elements
 * contained within the sampled Integer arrays will be equal to or greater than the
 * requested percentage.
 * 
 *
 */
public class WeightedSampler {
	
	ArrayList<Integer[]> indexesToSample;
	int totalNumberOfIndexes = 0;
	
	WeightedSampler(Integer[][] indexesToSample) {
		// Creating an ArrayList helps ensure that we don't mutate original object
		this.indexesToSample = new ArrayList<Integer[]>();
		for (Integer[] wordIndexes : indexesToSample) {
			this.indexesToSample.add(wordIndexes);
			totalNumberOfIndexes += wordIndexes.length;
		}
	}
	
	
	// Always get up to or above desired percent
	// Need a seed.... (later)
	/** 
	 * Samples 1d Integer arrays from the supplied 2d Integer array. The percentage of 
	 * of elements contained within the chosen Integer arrays will be equal to or greater
	 * than the requested percentage. The change of a 1d Integer array being selected is proprotional
	 * to the total number of elements that it contains. A seed is required.
	 * @param desiredPercent The percentage of the total elements that should be contained
	 * within the chosen Integer arrays. The actual percentage will always be equal to or
	 * greater than this amount.
	 * @param seed A random seed for reproducibility purposes
	 * @return A 2d Integer array containing the randomly selected 1d Integer arrays
	 */
	public Integer[][] sample(double desiredPercent, long seed) {
		Random random = new Random(seed);
		// Make copy so can modify without mutating original
		ArrayList<Integer[]> indexesToSample = (ArrayList<Integer[]>) this.indexesToSample.clone();
		ArrayList<Integer[]> sampledIndexes = sample(
			indexesToSample,
			desiredPercent,
			random
		);
		
		return sampledIndexes.toArray(new Integer[sampledIndexes.size()][]);
	}
	/**
	 * Samples 1d Integer arrays from the supplied 2d Integer array. The percentage of 
	 * of elements contained within the chosen Integer arrays will be equal to or greater
	 * than the requested percentage. The change of a 1d Integer array being selected is proprotional
	 * to the total number of elements that it contains
	 * @param desiredPercent The percentage of the total elements that should be contained
	 * within the chosen Integer arrays. The actual percentage will always be equal to or
	 * greater than this amount.
	 * @return A 2d Integer array containing the randomly selected 1d Integer arrays
	 */
	public Integer[][] sample(double desiredPercent) {;
		return sample(desiredPercent, new Random().nextLong());
	}
	private ArrayList<Integer[]> sample(
		ArrayList<Integer[]> indexesToSample,
		double desiredPercent,
		Random random) {
		
		// Will populate
		ArrayList<Integer[]> sampledIndexes = new ArrayList<Integer[]>();
		
		// If empty, then return an empty ArrayList
		if (indexesToSample.size() == 0) {
			return sampledIndexes;
		}
		
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
		// Call recursively until we have met our desired percentage
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

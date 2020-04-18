package madlibs;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class WeightedSamplerTest {

	@Test
	void testSample() {
		Integer[][] indexesToSample = {
			{0},
			{1,2},
			{3,4,5},
			{6,7},
			{8},
		};
		Integer[][] expectedIndexes = {
			{3, 4, 5},
			{8},
			{1,2},
		};
		WeightedSampler sampler = new WeightedSampler(indexesToSample);
		
		assertArrayEquals(expectedIndexes, sampler.sample(0.5, 20200418));
		
	}
	@Test
	void testEmptySample() {
		Integer[][] indexesToSample = new Integer[0][];
		Integer[][] expectedIndexes = new Integer[0][];
		WeightedSampler sampler = new WeightedSampler(indexesToSample);
		
		assertArrayEquals(expectedIndexes, sampler.sample(0.5, 20200418));
		
	}

}

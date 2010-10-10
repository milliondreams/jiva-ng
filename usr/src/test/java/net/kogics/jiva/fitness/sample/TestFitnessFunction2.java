package net.kogics.jiva.fitness.sample;

import java.util.Arrays;

import net.kogics.jiva.sample.FitnessFunction1;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author vipul
 * 
 */
public class TestFitnessFunction2 {

	FitnessFunction1 fitnessFunction = null;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		int activityDuration = 5;
		int standardDelay = 5;
		int[] projectActivities = { 7, 8, 3, 4 };// four projects
		int[] maxProjectDuration = { 50, 55, 25, 30 };

		fitnessFunction = new FitnessFunction1(activityDuration, standardDelay,
				projectActivities, maxProjectDuration);
	}

	/**
	 * pass on an incomplete boolean array and check if it throws exception or
	 * not
	 * 
	 */
	@Test
	public void testException() {
		// boolean array to calculate fitness funciton should be atleast 22 in
		// length
		Boolean[] bArray = { true, false };
		try {
			fitnessFunction.evaluate(Arrays.asList(bArray));
			Assert.fail("it should have thrown an exception");
		} catch (RuntimeException e) {
			// looks good
		}
	}

	/**
	 * Test for no delay in any activity
	 * 
	 */
	@Test
	public void testNoDelay() {
		Boolean[] bArray = { false, false, false, false, false, false, false,
				false, false, false, false, false, false, false, false, false,
				false, false, false, false, false, false };
		double expectedValue = 3;
		double retValue = fitnessFunction.evaluate(Arrays.asList(bArray));
		Assert.assertEquals(expectedValue, retValue);
	}

	/**
	 * Test for delay in - some activities of first project - some activities of
	 * second project - all activities of third project - no activities of
	 * fourth project
	 * 
	 */
	@Test
	public void testSomeDelay123Project() {
		Boolean[] bArray = { false, false, false, true, true, true, true,
				false, false, false, true, true, true, true, true, true, true,
				true, false, false, false, false };
		double expectedValue = 1.5333333333333332;
		double retValue = fitnessFunction.evaluate(Arrays.asList(bArray));
		Assert.assertEquals(expectedValue, retValue);
	}

	/**
	 * Test for delay in all activities of second project
	 * 
	 */
	@Test
	public void testAllDelaySecondProject() {
		Boolean[] bArray = { false, false, false, false, false, false, false,
				true, true, true, true, true, true, true, true, false, false,
				false, false, false, false, false };
		double expectedValue = 1.8666666666666663;
		double retValue = fitnessFunction.evaluate(Arrays.asList(bArray));
		Assert.assertEquals(expectedValue, retValue);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

}

package net.kogics.jiva.sample;

import java.util.ArrayList;
import java.util.List;

import net.kogics.jiva.fitness.JFitnessFunction;
import net.kogics.jiva.population.JChromosome;
import net.kogics.jiva.population.JGene;


/**
 * @author vipul
 *
 */
public class FitnessFunction1 implements JFitnessFunction<Boolean> {
	/**
	 * 
	 */
	private int standardActivityDuration;
	/**
	 * 
	 */
	private int standardDelay;

	/**
	 * 
	 */
	private int numberOfProjects;
	/**
	 * 
	 */
	private int[] activityCount;
	/**
	 * 
	 */
	private int[] maxProjectDuration;
	/**
	 * 
	 */
	private int totalActivities;

	/**
	 * 
	 */
	private int totalMaxEarlyness = 0;
	/**
	 * 
	 */
	private int totalMaxTardiness= 0;
	
	

	/**
	 * 
	 * @param activityDuration Duration for each activity - in hours 
	 * @param standardDelay Delay value - in hours
	 * @param projActivityCounts Count of activities for each project
	 * @param maxProjectDuration Maximum Duration for each project (in hours)
	 */
	public FitnessFunction1(int activityDuration,int standardDelay,int[] projActivityCounts,int[] maxProjectDuration) {
		this.standardActivityDuration = activityDuration;
		this.standardDelay = standardDelay;
		numberOfProjects = projActivityCounts.length;
		activityCount = projActivityCounts;
		this.maxProjectDuration = maxProjectDuration;
		
		for(int count : activityCount) {
			totalActivities = totalActivities + count;
		}

		//calculate max-earlyness and max-tardiness for any solution.
		for(int i = 0 ; i<projActivityCounts.length ; i++) {
			totalMaxEarlyness = totalMaxEarlyness+ (maxProjectDuration[i] - projActivityCounts[i]*standardActivityDuration); 	
			totalMaxTardiness = totalMaxTardiness  + ((projActivityCounts[i]*standardActivityDuration+projActivityCounts[i]*standardDelay) - maxProjectDuration[i]);
		
		}
		
	}

	public double evaluate(JChromosome<Boolean> chr) {
		List<Boolean> bArray = new ArrayList<Boolean>();
		
		for (JGene<Boolean> gene : chr) {
			bArray.add(gene.allele());
		}
		return evaluate(bArray);
	}	
	
	/**
	 * 
	 * Accepts an array of boolean indicating if an activity of a project 
	 * is started with a delay or not.
	 * 
	 * @param bArray
	 * @return
	 */
	public double evaluate(List<Boolean> bArray ) {
		
		if(bArray.size() != totalActivities)
			throw new RuntimeException(" Invalid Array length "+bArray.size()+" : should be "+totalActivities);
		
		// return value;
		double value = 2;
		
		int booleanArrayIndex = 0;
		//loop through bArray for all the projects 
		for(int projNumber = 0; projNumber < numberOfProjects ; projNumber++) {

			//  duration for this project - 
			//minimum duration is the product of the number of activities in this project and the avtivity duration
			int projectDuration = activityCount[projNumber]*standardActivityDuration;
			
			//loop through bArray for each activity
			for(int activityNumber = 0;activityNumber < activityCount[projNumber];activityNumber++) {
				if(bArray.get(booleanArrayIndex++)) {
					projectDuration = projectDuration+standardDelay;
				}
			}

			int maxDuration = maxProjectDuration[projNumber];

			/*
			 * Apply a simple formula for calculating the fitness value
			 *  add the fraction of earlyness (earlyness/max-earlyness)
			 *  and subtract twice the fraction of tardiness (tardiness/max-tardiness) 
			 *  	(peanlizing more for tardiness)
			 *  
			 */
			
			
			//if project duration is greater than the max allowed for this project
			if(projectDuration > maxDuration) {
				int tardiness = projectDuration - maxDuration; 
				//penalize ... twice
				value = value - 2*(double)tardiness/(double)totalMaxTardiness;
			}
			else {
				int earlyness = maxDuration - projectDuration;
				//give some credit
				value = value + (double)earlyness/(double)totalMaxEarlyness;
			}
		}
		
		return value;
	}

	public int $tag() {
		return 0;
	}
}

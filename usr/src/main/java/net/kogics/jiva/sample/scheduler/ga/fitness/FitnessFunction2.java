/**
 * 
 */
package net.kogics.jiva.sample.scheduler.ga.fitness;

import java.util.List;

import net.kogics.jiva.sample.scheduler.Project;
import net.kogics.jiva.sample.scheduler.ResourcePool;

/**
 * @author vipul
 *
 */
public class FitnessFunction2 extends FitnessFunction {

	/**
	 * @param pool
	 * @param projects
	 */
	public FitnessFunction2(ResourcePool pool, List<Project> projects) {
		super(pool, projects);
	}
	
	

	/**
	 * 
	 * @param earlyness
	 * @param tardiness
	 * @param clashes
	 * @return
	 */
	private boolean printed = false;
	protected double calculateFitness(int earlyness, int tardiness, int clashes) {
	
		double fitnessValue = 0;
		if(!printed){
			System.out.println("FitnessFunction2 called");
			printed = true;
		}
		
//		double clashComponent = 100000*clashes*Math.pow(13d, (double)clashes);
//		double tardinessComponent = 100000*Math.pow((double)tardiness,8);
//		double earlynessComponent =  Math.pow((double)earlyness,1);
		double clashComponent = 10000*clashes;
		double tardinessComponent = 100*tardiness;
		double earlynessComponent =  10*earlyness;

		// penelty for lower number of clashes may get overpowered by tardiness thus 
		// assigning a lower fitness value to non-clashing-more-tardy schedules than
		// clashing-but-less-tardy schedules ... 
		// now it totally depends upon what's more acceptable in a given scenario ... 
		// 	- a clash .. (that can be resolved by throwing in a resource - or making one overwork)
		// 	- tardyness .. where resources cannot be added!
		//fitnessValue = 1000000000000000d - clashComponent - tardinessComponent +earlynessComponent;
		fitnessValue = 10000000 - clashComponent - tardinessComponent +earlynessComponent;
		return fitnessValue>0?fitnessValue:0;//return 0 if fitness value is negative 
	}

}

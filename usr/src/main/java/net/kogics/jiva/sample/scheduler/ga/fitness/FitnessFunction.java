/*
 * Copyright (C) 2007 Vipul Pandey<vipandey@gmail.com>
 *
 * The contents of this file are subject to the GNU General Public License 
 * Version 2 or later (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.gnu.org/copyleft/gpl.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 */
package net.kogics.jiva.sample.scheduler.ga.fitness;

import java.util.List;

import net.kogics.jiva.fitness.JFitnessFunction;
import net.kogics.jiva.population.JChromosome;
import net.kogics.jiva.population.JGene;
import net.kogics.jiva.sample.scheduler.Project;
import net.kogics.jiva.sample.scheduler.ResourcePool;
import net.kogics.jiva.sample.scheduler.TimeSeries;

/**
 * @author vipul
 *
 */
public class FitnessFunction implements JFitnessFunction<Integer> {
	
	/**
	 * the resource pool
	 */
	private ResourcePool resourcePool = null;
	
	/**
	 * list of projects
	 */
	private List<Project> projects = null;
	
	/**
	 * total number of activities in all projects
	 */
	private int numActivities = 0;
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
	 * @param pool
	 * @param projects
	 */
	public FitnessFunction(ResourcePool pool, List<Project> projects) {
		this.resourcePool = pool;
		this.projects = projects;
		
		for(Project project : projects) {
			numActivities = numActivities+project.getActivities().size();
		}
		
	}


	/**
	 * 
	 * @param chr
	 * @return
	 */
	public double evaluate(JChromosome<Integer> chr){

		int index = 0;
		Integer[] iArray = new Integer[chr.size()];
		for (JGene<Integer> gene : chr) {
			iArray[index++] =gene.allele();
		}
		return evaluate(iArray);
	}
		
	/**
	 * 
	 * @param iArray
	 * @return
	 */
	protected double evaluate(Integer[] iArray){
		double value = 2;
		
		TimeSeries timeSeries = null;

		int earlyness = 0;
		int tardiness = 0;
		int clashes = 0 ;
		

		
		if(iArray.length != numActivities)
			throw new RuntimeException("Invalid array length "+iArray.length+"  - Should be "+numActivities);
		
		//create a new timeSeries instance
		try {
			timeSeries = new TimeSeries((ResourcePool) resourcePool.clone(),projects,iArray);
			// make a copy of the pool since the pool goes through modification while setting up the time series
			//.... it's not required to make a copy of the projects beacuase only the work-data of the project changes
			// and can be reset at the end. there is no multithreading happening anyways.
			timeSeries.generate();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		clashes = timeSeries.getClashes();
		earlyness = timeSeries.getEarlyness();
		tardiness  = timeSeries.getTardiness();
		
		value = calculateFitness(earlyness,tardiness,clashes);
//		System.out.println(" earlyness "+earlyness);
//		System.out.println(" tardiness "+tardiness);
//		System.out.println(" clashes "+clashes);
//		System.out.println(" fitness-value "+value);
		
		for(Project project : projects){
			project.reset();
		}
		
		
		return value; 
		
	}

	/**
	 * 
	 * @param earlyness
	 * @param tardiness
	 * @param clashes
	 * @return
	 */
	protected double calculateFitness(int earlyness, int tardiness, int clashes) {
	
		double fitnessValue = 0;
		
		double clashComponent = 100*clashes*Math.pow(10d, (double)clashes);
		double tardinessComponent = Math.pow((double)tardiness,2);
		double earlynessComponent =  Math.pow((double)earlyness,1);
		// penelty for lower number of clashes may get overpowered by tardiness thus 
		// assigning a lower fitness value to non-clashing-more-tardy schedules than
		// clashing-but-less-tardy schedules ... 
		// now it totally depends upon what's more acceptable in a given scenario ... 
		// 	- a clash .. (that can be resolved by throwing in a resource - or making one overwork)
		// 	- tardyness .. where resources cannot be added!
		fitnessValue = 1000000000d - clashComponent - tardinessComponent +earlynessComponent;
		
		return fitnessValue>0?fitnessValue:0;//return 0 if fitness value is negative 
	}
	
	private FitnessFunction(){}
	public static void main(String[] args){
		System.out.println(new FitnessFunction().calculateFitness(111, 50, 3));
	}

	
}

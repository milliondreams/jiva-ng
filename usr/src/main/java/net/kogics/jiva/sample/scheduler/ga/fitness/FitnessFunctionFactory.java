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

import net.kogics.jiva.sample.scheduler.Project;
import net.kogics.jiva.sample.scheduler.ResourcePool;
import net.kogics.jiva.sample.scheduler.SchedulingProblem;

/**
 * Factory that gets an instance of FitnessFunction
 * 
 * @author vipul
 *
 */
public class FitnessFunctionFactory {
	
	
	
	/**
	 * 
	 * returns a fitness function
	 * 
	 * @return
	 */
	public static JFitnessFunction<Integer> getFitnessFunction(){
		JFitnessFunction<Integer> fitnessFunction = null;
		
		ResourcePool pool = SchedulingProblem.getResourcePool();
		List<Project> projects = SchedulingProblem.getProjects();
		
		SchedulingProblem.printProblem(pool,projects);
 
		fitnessFunction = new FitnessFunction(pool,projects);
		
		return fitnessFunction;
		
	}

	/**
	 * 
	 * returns a fitness function
	 * 
	 * @return
	 */
	public static JFitnessFunction<Integer> getFitnessFunction2(){
		JFitnessFunction<Integer> fitnessFunction = null;
		
		ResourcePool pool = SchedulingProblem.getResourcePool();
		List<Project> projects = SchedulingProblem.getProjects();
		
		SchedulingProblem.printProblem(pool,projects);
 
		fitnessFunction = new FitnessFunction2(pool,projects);
		
		return fitnessFunction;
		
	}
	
	
}

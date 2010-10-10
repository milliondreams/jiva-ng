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
package net.kogics.jiva.sample.scheduler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.kogics.jiva.fitness.JFitnessFunction;
import net.kogics.jiva.gaprob.GaProblem;
import net.kogics.jiva.population.JChromosome;
import net.kogics.jiva.population.JGene;
import net.kogics.jiva.population.JPopulation;
import net.kogics.jiva.sample.scheduler.ga.fitness.FitnessFunctionFactory;

/**
 * @author vipul
 *
 */
public class SchedulerMain {

	/**
	 * @param args
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws Exception {
		
		// get the problem definition from either an external file .. or use the hardcoded one
		//THE PHENOTYPE
		
		
		ResourcePool resourcePool = null;
		List<Project> projects =  null;
		if(args.length != 0){
			//assuming that the first argument is a path to a valid sch-problem file
			SchedulingProblem.initFromfile(args[0]);
		}else{
			SchedulingProblem.initHardcoded();
		}
		resourcePool = SchedulingProblem.getResourcePool();
		projects =  SchedulingProblem.getProjects();
		
		//get total number of activitiees 
		int totalActivities = 0;
		//get the max execution time within all the projects and set it as the max value for integer
		int maxDelayValue = 0;
		
		for(Project project : projects){
			totalActivities = totalActivities+project.getActivities().size();
			//this would work because even if there is just one activity in the project .. it should not go beyond the endtime of the project
			maxDelayValue = (maxDelayValue  > project.getLatestEndTime()) ? maxDelayValue :  project.getLatestEndTime();
		}

		System.out.println("\nmaxDelayValue : "+maxDelayValue);
		//get the fitness function
		JFitnessFunction<Integer> fitnessFunction = FitnessFunctionFactory.getFitnessFunction();
		JFitnessFunction<Integer> fitnessFunction2 = FitnessFunctionFactory.getFitnessFunction2();
		
		//create the GaProblem instance
		GaProblem gaProb = createGaProblem(totalActivities,maxDelayValue,fitnessFunction,fitnessFunction2);
		
		
		// ********* print the Zero Delay schedule on the screen ... the output can be used in the flex-based Scheduler UI
		Integer[] zeroDelays = null;
		zeroDelays = new Integer[totalActivities];
		for(int i = 0 ; i < totalActivities ; i++)
			zeroDelays[i] = 0;
		System.out.println("Non  Delay Schedule : ");
		Util.printSchedule(resourcePool, projects, zeroDelays);
		// ********* 		
		
		
		//accept the number of solutions desired by the person
		System.out.println("\n\n Please indicate the number of good solutions you desire : ");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String str = br.readLine();
		
		//catch and throw the numberformatexception
		int count = 10;
		try{
			count  = Integer.parseInt(str);
		}catch(NumberFormatException nfe){
			//ignore it
			System.out.println("Was expecting a number ... instead got a "+((str.length()==0)?"BLANK":str));
			System.out.println("Never mind ... will give you "+count+" solution");
		}
		
		long currentTimeMillis = System.currentTimeMillis();
		
		JPopulation<Integer> population = gaProb.run(); //we know the population  in this case is going to hold Integers based choromosomes
//		System.out.println("\nPopulation "+population.size());
		System.out.println("\n Total Time : "+(System.currentTimeMillis() - currentTimeMillis));

		//get 'count' good solutions
		JChromosome<Integer>[] chromosomes = null;
		if(count == 1){
			chromosomes =  new JChromosome[1];
			chromosomes[0] = population.fittestChromosome();
		}
		else{
			chromosomes = getGoodSolutions(population,count);
		}

		for(JChromosome<Integer> chromosome:chromosomes){
			System.out.println(" Solution :("+chromosome.fitnessVal()+") "+chromosome.toString());
			Integer[] delays = getDelayArray(chromosome);
			Util.printSchedule(resourcePool, projects, delays);
			System.out.println();

			System.out.println(" Hit enter/return to see the next proposed schedule");
			System.in.read(); //uncomment if you don't need all printed at once
		}
		
		System.out.println("Done! Exiting");
		
	}


	/**
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	
	private static GaProblem createGaProblem(int activityCount,int maxDelayValue ,JFitnessFunction<Integer> fitnessFunction,JFitnessFunction<Integer> fitnessFunction2) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		
		
//		Class cls = Class.forName("net.kogics.jiva.sample.scheduler.standard.SchedulingProblemBuilder");
//		Class[] args = {int.class,int.class,JFitnessFunction.class};
//		Method buildProb = cls.getMethod("buildProb", args);
//		return (GaProblem)buildProb.invoke(cls, activityCount,maxDelayValue, fitnessFunction);

//		Class cls = Class.forName("net.kogics.jiva.sample.scheduler.apga.SchedulingProblemBuilder");
//		Class[] args = {int.class,int.class,JFitnessFunction.class};
//		Method buildProb = cls.getMethod("buildProb", args);
//		return (GaProblem)buildProb.invoke(cls, activityCount,maxDelayValue, fitnessFunction);

//		Class cls = Class.forName("net.kogics.jiva.sample.scheduler.varparam.SchedulingProblemBuilder");
//		Class[] args = {int.class,int.class,JFitnessFunction.class};
//		Method buildProb = cls.getMethod("buildProb", args);
//		return (GaProblem)buildProb.invoke(cls, activityCount,maxDelayValue, fitnessFunction);
		
		

//		Class cls = Class.forName("net.kogics.jiva.sample.scheduler.chained.SchedulingProblemBuilder");
//		Class[] args = {int.class,int.class,JFitnessFunction.class,JFitnessFunction.class};
//		Method buildProb = cls.getMethod("buildProb", args);
//		return (GaProblem)buildProb.invoke(cls, activityCount,maxDelayValue, fitnessFunction,fitnessFunction2);

		Class cls = Class.forName("net.kogics.jiva.sample.scheduler.endnested.SchedulingProblemBuilder");
		Class[] args = {int.class,int.class,JFitnessFunction.class,JFitnessFunction.class};
		Method buildProb = cls.getMethod("buildProb", args);
		return (GaProblem)buildProb.invoke(cls, activityCount,maxDelayValue, fitnessFunction,fitnessFunction2);

	}
 
	/**
	 * 
	 * @param population
	 * @param count
	 * @return
	 */
	private static JChromosome<Integer>[] getGoodSolutions(JPopulation<Integer> population,
			int count) {
		JChromosome<Integer>[] chromosomes = new JChromosome[count];
		
		Iterator<JChromosome<Integer>> iterator =population.iterator();
		
		Set<JChromosome<Integer>> set = new TreeSet<JChromosome<Integer>>(
				new Comparator<JChromosome<Integer>>(){
					public int compare(JChromosome<Integer> o1,
							JChromosome<Integer> o2) {
						if(o1.fitnessVal() > o2.fitnessVal()){
							return -1;
						}else if(o1.fitnessVal() < o2.fitnessVal()){
							return 1;
						}else{
							return 0;
						}
					}
					
				});
		

		while(iterator.hasNext()){
			JChromosome<Integer> chromosome = iterator.next();
			System.out.print(chromosome.fitnessVal()+",");
			set.add(chromosome);
		}
		
		Iterator<JChromosome<Integer>> it = set.iterator();
		for(int i =0;i<count && it.hasNext();i++){
			chromosomes[i] = it.next();
		}

		return chromosomes;
	}
	
	/**
	 * 
	 * @param chromosome
	 * @return
	 */
	private static Integer[] getDelayArray(JChromosome<Integer> chromosome){

		Iterator<JGene<Integer>> genes = chromosome.iterator();
		
		List<Integer> listOfDelays = new ArrayList<Integer>();
		while(genes.hasNext()){
			JGene<Integer> gene = genes.next();
			Integer delay= gene.allele();
			listOfDelays.add(delay);
		}
		Integer[] delays = new Integer[listOfDelays.size()];
		delays = listOfDelays.toArray(delays);
		
		return delays;
		
		
	}
	
	

}

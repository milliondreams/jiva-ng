/*
 * Copyright (C) 2007 Vipul Pandey <vipandey@gmail.com>
 *
 * The contents of this file are subject to the GNU General Public License 
 * Version 3 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.gnu.org/copyleft/gpl.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 */

package net.kogics.jiva.sample.scheduler.endnested


import net.kogics.jiva.Predef._
import net.kogics.jiva.gaprob.{GaProblem,ProbBuilder,IntegerProbBuilder,JProbBuilder,ChainedGaProblems}
import net.kogics.jiva.evolution.FitnessFunction
import net.kogics.jiva.fitness.JFitnessFunction
import net.kogics.jiva.operators.{MutationOp,CrossoverOp,RandomResetMutationOp,SinglePointCrossoverOp,DetXingMutationOp,IdentityCrossoverOp}
import net.kogics.jiva.evolution.{ParentSelector,TournamentSelector,ApgaReplacer,ApgaFitnessEvaluator,RouletteWheelSelector}
import net.kogics.jiva.util.ShufflerImpl
import net.kogics.jiva.jbridge.JFitnessWrapper
import net.kogics.jiva.sample.scheduler.varparam.GenVarMutationProb



object SchedulingProblemBuilder {

  // build the problem with the given chromosome size .. max value for int alleles and the fitnessfunction
    
  def buildProb(chrSize:int, maxGeneValue:int,jff:JFitnessFunction[jint],njff:JFitnessFunction[jint]) : GaProblem[jint] = {
    

    
    
    // base GaProb 
    
    var builder:IntegerProbBuilder = JProbBuilder.integerProbBuilder
    var popSize = 400
    var evolutions = 250
    
    var ff:FitnessFunction[jint] = new JFitnessWrapper(jff)
    
    builder name "Scheduler"
    builder popSize popSize
    builder chrSize (chrSize,maxGeneValue) 
    builder numEvolutions evolutions
    builder fitnessFunction ff
    
    var popDelta = Math.ceil(popSize / 4.0).toInt
    var selector:ParentSelector[jint] = new TournamentSelector[jint](popDelta, builder.rgen, 4)
    var replacer = new ApgaReplacer[jint]
    var evaluator = new ApgaFitnessEvaluator[jint](ff)
    
    var ulMutOp = new RandomResetMutationOp[jint](0.05, builder.rgen)
    var mutProb = new GenVarMutationProb[jint](0.05,popSize)
    var mutOp:MutationOp[jint] = new DetXingMutationOp(mutProb,ulMutOp)
    
    var crossOp:CrossoverOp[jint] = new SinglePointCrossoverOp[jint](0.15, builder.rgen)
    
    builder evolver (selector, replacer, evaluator, mutOp, crossOp)
    val mainGaProb = builder.build

    
    
    // nested Ga Problem 
    val nff = new JFitnessWrapper(njff)
    // max gene value for nested GA ... 
    val maxGeneValue2 = 30
    //median ... the delta range would vary from  - maxGeneValue2/2 to maxGeneValue2/2
    val median = maxGeneValue2/2
     // wrapper GaProb
    val enGaProblem = new SchedulingEndNestedGaProblem(mainGaProb,buildNestedProbWrapper(maxGeneValue2),ff,nff,5,median) // return back a population of around 20*20 chromosomes to select from
    enGaProblem

  }

  private def buildNestedProbWrapper(maxGeneValue:int) : ((int,FitnessFunction[jint]) => GaProblem[jint]) = {
    //doing all this for two reasons
    // i) i need maxGeneValue here ... and i needed maxGeneValue there too (while creating SchedulingEndNestedGaProblem ...
    // ii) i wanted to return a closure from a function .... just for the heck of it
       
     (chrSize:int,nff:FitnessFunction[jint]) => {
	
 	    print(" buildingNestedProblem for chrSize"+chrSize+" : maxGeneValue - "+maxGeneValue)
	    val probBuilder = JProbBuilder.integerProbBuilder
	    val popSize = 400
	    val evolutions = 500
	    
	    probBuilder name "SchedulerNested"
	    probBuilder popSize popSize
	    probBuilder chrSize (chrSize,maxGeneValue) 
	    probBuilder numEvolutions evolutions
	    probBuilder fitnessFunction nff
	
	    val popDelta = Math.ceil(popSize / 4.0).toInt
	
	    val selector = new TournamentSelector[jint](popDelta, probBuilder.rgen, 4)/*new RouletteWheelSelector[jint](popDelta, probBuilder.rgen)*/
	    val replacer = new ApgaReplacer[jint]
	    val evaluator = new ApgaFitnessEvaluator[jint](nff)
	    
	    val mutOp = new RandomResetMutationOp[jint](0.01, probBuilder.rgen)
	    val crossOp =  new SinglePointCrossoverOp[jint](0.15, probBuilder.rgen)
	    
	    probBuilder evolver (selector, replacer, evaluator, mutOp, crossOp)
	
	    probBuilder build
	  }
  }
  
}

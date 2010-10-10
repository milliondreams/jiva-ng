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

package net.kogics.jiva.sample.scheduler.chained


import net.kogics.jiva.Predef._
import net.kogics.jiva.gaprob.{GaProblem,ProbBuilder,IntegerProbBuilder,JProbBuilder,ChainedGaProblems}
import net.kogics.jiva.evolution.FitnessFunction
import net.kogics.jiva.fitness.JFitnessFunction
import net.kogics.jiva.operators.{CrossoverOp,RandomResetMutationOp,SinglePointCrossoverOp,DetXingMutationOp,IdentityCrossoverOp}
import net.kogics.jiva.evolution.{ParentSelector,TournamentSelector,ApgaReplacer,ApgaFitnessEvaluator,RouletteWheelSelector}
import net.kogics.jiva.util.ShufflerImpl
import net.kogics.jiva.jbridge.JFitnessWrapper
import net.kogics.jiva.sample.scheduler.varparam.GenVarMutationProb



object SchedulingProblemBuilder {

  // build the problem with the given chromosome size .. max value for int alleles and the fitnessfunction
    
  def buildProb(chrSize:int, maxGeneValue:int,jff:JFitnessFunction[jint],jff2:JFitnessFunction[jint]) : GaProblem[jint] = {
    
    val gaProbArr = new Array[GaProblem[jint]](2)
    
    
    // GaProb 1
    
    var builder:IntegerProbBuilder = JProbBuilder.integerProbBuilder
    var popSize = 400
    var evolutions = 500
    
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
    var mutOp = new DetXingMutationOp(mutProb,ulMutOp)
    
    var crossOp:CrossoverOp[jint] = new SinglePointCrossoverOp[jint](0.15, builder.rgen)
    
    builder evolver (selector, replacer, evaluator, mutOp, crossOp)
    builder.build
    gaProbArr(0) = builder.build 
    
    
    // GaProb 2
    builder = JProbBuilder.integerProbBuilder
    popSize = 400
    evolutions = 500
    
    ff = new JFitnessWrapper(jff2)
    
    builder name "Scheduler"
    builder popSize popSize
    builder chrSize (chrSize,maxGeneValue) 
    builder numEvolutions evolutions
    builder fitnessFunction ff
    
    popDelta = Math.ceil(popSize / 4.0).toInt
    //selector = new TournamentSelector[jint](popDelta, builder.rgen, 10)
    selector = new RouletteWheelSelector[jint](popDelta, builder.rgen)
    replacer = new ApgaReplacer[jint]
    evaluator = new ApgaFitnessEvaluator[jint](ff)
    
    ulMutOp = new RandomResetMutationOp[jint](0.01, builder.rgen)
/*    mutProb = new GenVarMutationProb(0.05,popSize)
    mutOp = new DetXingMutationOp(mutProb,ulMutOp)
*/    
    crossOp = new IdentityCrossoverOp[jint]()
    
    builder evolver (selector, replacer, evaluator, ulMutOp, crossOp)
    builder.build
    gaProbArr(1) = builder.build 

    // wrapper GaProb
  
    new ChainedGaProblems(gaProbArr)
    
  }
}

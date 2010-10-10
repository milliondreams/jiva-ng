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

package net.kogics.jiva.sample.scheduler.apga


import net.kogics.jiva.Predef._
import net.kogics.jiva.gaprob.{GaProblem,ProbBuilder,IntegerProbBuilder,JProbBuilder}
import net.kogics.jiva.evolution.FitnessFunction
import net.kogics.jiva.fitness.JFitnessFunction
import net.kogics.jiva.operators.{RandomResetMutationOp,SinglePointCrossoverOp,DetXingMutationOp}
import net.kogics.jiva.evolution.{TournamentSelector,ApgaReplacer,ApgaFitnessEvaluator}
import net.kogics.jiva.util.ShufflerImpl
import net.kogics.jiva.jbridge.JFitnessWrapper



object SchedulingProblemBuilder {

  // build the problem with the given chromosome size .. max value for int alleles and the fitnessfunction
    
  def buildProb(chrSize:int, maxGeneValue:int,jff:JFitnessFunction[jint]) : GaProblem[jint] = {
    println(" Invoking apga ProblemBuilder")
    val builder:IntegerProbBuilder = JProbBuilder.integerProbBuilder
    val popSize = 400
    val evolutions = 1000
    
    val ff:FitnessFunction[jint] = new JFitnessWrapper(jff)
    
    builder name "Scheduler"
    builder popSize popSize
    builder chrSize (chrSize,maxGeneValue) 
    builder numEvolutions evolutions
    builder fitnessFunction ff
    
    val popDelta = Math.ceil(popSize / 4.0).toInt
    val selector = new TournamentSelector[jint](popDelta, builder.rgen, 4)
    val replacer = new ApgaReplacer[jint]
    val evaluator = new ApgaFitnessEvaluator[jint](ff)
    
    val mutOp = new RandomResetMutationOp[jint](0.05, builder.rgen)

    val crossOp = new SinglePointCrossoverOp[jint](0.15, builder.rgen)
    
    builder evolver (selector, replacer, evaluator, mutOp, crossOp)
      
    builder.build
    
  }
}

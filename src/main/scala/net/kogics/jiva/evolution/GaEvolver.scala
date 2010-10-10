/*
 * Copyright (C) 2007 Lalit Pant <pant.lalit@gmail.com>
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

package net.kogics.jiva.evolution

import net.kogics.jiva.Predef._
import net.kogics.jiva.operators.{CrossoverOp, MutationOp}
import net.kogics.jiva.population.{Population, Chromosome}
import net.kogics.jiva.gaprob.GaProblem
import net.kogics.jiva.util.collection.JList
import net.kogics.util.listener.NonBlockingBroadcaster

class GaEvolver[A](val problem: GaProblem[A], val selector: ParentSelector[A], val mutator: MutationOp[A], 
                   val crossator: CrossoverOp[A], val evaluator: FitnessEvaluator[A], 
                   val replacer: Replacer[A]) 
extends NonBlockingBroadcaster[JGaEvent[A]] {

  def run(ipop: Option[Population[A]]): Population[A] = {
    var pop = ipop.getOrElse(initialPop) 
    println("GA Run Starting...")
    println("Random Number Generator Seed: " + Randomizer().rseed)
    pop.evaluate(evaluator, Population.empty)
    
    for (gen <- Iterator.range(0, problem.numEv)) {
      broadcastEvent(new GaEvent(EventType.GEN_START, pop))
      pop = evolve(pop)
      // Console.println("Generation:" + gen)
      // Console.println("Pop Size:" + pop.size)
      // Console.println("Best Fitness:" + pop.fittestChromosome.fitness.get.toString)
    }
    broadcastEvent(new GaEvent(EventType.RUN_FINISH, pop))
    pop
  }

  def evolve(pop: Population[A]) : Population[A] = {
    var newPop = selector.select(pop); 
    newPop = crossator.operate(newPop)
    newPop = mutator.operate(newPop)
    newPop.evaluate(evaluator, pop)
    return replacer.replace(pop, newPop)
  }
  
  def initialPop(): Population[A] = {
    val candidates = new JList[Chromosome[A]]
    for (idx <- Iterator.range(0, problem.populationSize)) {
      val chr = problem.randomChr
      candidates + chr
    }
    return new Population(candidates)
  }
}

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

import scala.util.Random

import net.kogics.jiva.Predef._
import net.kogics.jiva.population.{Population, Chromosome}
import net.kogics.jiva.util.collection.JList

/**
 * Selects 'num' elements from a supplied population based on
 * a k-way tournament
 * 
 */
class TournamentSelector[A](num: Int, rgen: Random, k: Int) extends ParentSelector[A] {

  checkArg(num > 0, "Invalid Selection Size: %d. Selection Size should be > 0", num)
  checkArg(k > 1, "Invalid Tournament Size: %d. Tournament Size should be > 1", k)
  
  def select(pop: Population[A]) : Population[A] = {
    checkArg(k < pop.size, 
             "Invalid Tournament Size: %d. Tournament Size should be less than Pop size: %d ", k, pop.size)

    val newChrs = new JList[Chromosome[A]]

    for (i <- Iterator.range(0, num)) {
      newChrs + selectChr(pop)
    }
    
    return new Population(newChrs)
  }

  private def selectChr(pop: Population[A]) : Chromosome[A] = {
    var bestIndividual: Chromosome[A] = null
    var tmpIndividual: Chromosome[A] = null
    
    var bestFitness = Double.NegativeInfinity

    val popSize = pop.size
    
    for (i <- Iterator.range(0, k)) {
      val counter = rgen.nextInt(popSize)
      val tmpIndividual = pop(counter) 
      if (tmpIndividual.fitness.get > bestFitness) {
        bestIndividual = tmpIndividual
        bestFitness = tmpIndividual.fitness.get
      }
    }
    return bestIndividual.clone2    
  }
}
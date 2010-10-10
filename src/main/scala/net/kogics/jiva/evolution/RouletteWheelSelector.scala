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

class RouletteWheelSelector[A](num: Int, rgen: Random) extends ParentSelector[A] {

  checkArg(num > 0, "Invalid Selection Size: %d. Selection Size should be > 0", num)
  
  def select(pop: Population[A]) : Population[A] = {
    val probs = new JList[Double]
    val cumProbs = new JList[Double]
    
    var aggrFitness = pop.members.foldLeft(0.0){(aggr, chr) => aggr + chr.fitness.get}

    pop.foreach { chr =>
      probs += chr.fitness.get / aggrFitness
    }
    
    cumProbs += probs(0)
    for (idx <- Iterator.range(1, pop.size)) {
      cumProbs += (probs(idx) + cumProbs(idx-1))
    }
    
    val newChrs = new JList[Chromosome[A]]
    for (idx <- Iterator.range(0, num)) {
      val selected = spinWheel(cumProbs)
      newChrs += pop(selected).clone2
    }
    
    new Population(newChrs)
  }
  
  def spinWheel(cumProbs: Seq[Double]): Int = {
    val spinResult = rgen.nextDouble
    Iterator.range(0, cumProbs.size).find(idx => spinResult < cumProbs(idx)).get
  }
}

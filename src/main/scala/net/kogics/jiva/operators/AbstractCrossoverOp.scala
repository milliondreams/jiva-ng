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

package net.kogics.jiva.operators

import scala.util.Random

import net.kogics.jiva.Predef._
import net.kogics.jiva.population.{Population, Chromosome}
import net.kogics.jiva.util.{Shuffler, ShufflerImpl}
import net.kogics.jiva.util.collection.JList

abstract class AbstractCrossoverOp[A](opc: Option[Double], rgen: Random) 
extends AbstractGeneticOp[A](opc, rgen) with CrossoverOp[A] {
  
  var shuffler: Shuffler[Chromosome[A]] = new ShufflerImpl[Chromosome[A]]  
  protected [operators] def operate(pc: Double, pop: Population[A]): Population[A] = {

    val selectedForCrossover = new JList[Chromosome[A]]
    val candidates = new JList[Chromosome[A]]
    
    pop.foreach { chr => if (rgen.nextDouble < pc) selectedForCrossover += chr else candidates += chr}
    
    if (selectedForCrossover.size % 2 != 0) {
      var add = true
      if (candidates.size == 0) add = false else add = rgen.nextBoolean
      if (add) {
        moveRandomGene(candidates, selectedForCrossover)
      }
      else { 
        moveRandomGene(selectedForCrossover, candidates)
      }
    }
    
    shuffler.shuffle(selectedForCrossover.underlying)
    
    for (i <- Iterator.range(0, selectedForCrossover.size, 2)) {
      val chr1 = selectedForCrossover(i)
      val chr2 = selectedForCrossover(i+1)
      val crossed = crossover(chr1, chr2)
      candidates += crossed._1
      candidates += crossed._2
    }
    
    return new Population(candidates)
  }
  
  def crossover(chr1: Chromosome[A], chr2: Chromosome[A]) : (Chromosome[A], Chromosome[A]) 
  
  private def moveRandomGene(from: JList[Chromosome[A]], to: JList[Chromosome[A]]) : Unit = {
    val idx = rgen.nextInt(from.size)
    val chr = from.remove(idx)
    to + chr
  }
}


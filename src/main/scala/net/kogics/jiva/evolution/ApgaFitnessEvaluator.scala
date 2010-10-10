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

import net.kogics.jiva.population.{Population, Chromosome}

class ApgaFitnessEvaluator[A](ff: FitnessFunction[A]) extends FitnessEvaluator[A] {
  val MIN_LT = 1L
  val MAX_LT = 11L
  val eta = (MAX_LT - MIN_LT) / 2.0

  def evaluate(offspring: Population[A], currPop: Population[A]) : Unit = {
    var avgFit = -1.0
    var minFit = Double.MaxValue
    var maxFit = -1.0
    var sum = 0.0
    
    def processChr(chr: Chromosome[A], f: Chromosome[A] => Double) = {
      val fness = f(chr)
      if (fness > maxFit) {
        maxFit = fness
      } 
      else if (fness < minFit) {
        minFit = fness
      }
      sum += fness
    }
    
    currPop.foreach { chr =>
      processChr(chr, {chr => chr.fitness.get})
    }
    
    offspring.foreach { chr => 
      processChr(chr, {chr => 
                   val fness = ff(chr)
                   chr.fitness = Some(fness)
                   fness})
    }
    
    avgFit = sum / (currPop.size + offspring.size)
    
    offspring.foreach { chr =>
      var lt = 0L
      if (avgFit >= chr.fitness.get) {
        lt = MIN_LT + math.round(eta * (chr.fitness.get - minFit)/(avgFit - minFit))
      }
      else {
        lt = (MIN_LT + MAX_LT)/2 + math.round(eta * (chr.fitness.get - avgFit)/(maxFit - avgFit))
      }
      chr.props += ("lifetime" -> lt)
    }
  }
}

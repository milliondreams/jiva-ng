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
import net.kogics.jiva.util.collection.JList

class ApgaReplacer[A] extends Replacer[A] {
  def replace(currPop: Population[A], offspring: Population[A]) : Population[A] = {
    var oneBestFound = false
    val maxFitness = currPop.fittestChromosome.fitness.get
    
    val candidates = new JList[Chromosome[A]]
    
    currPop.foreach { chr =>
      if (chr.fitness.get == maxFitness && !oneBestFound) {
        oneBestFound = true
      } 
      else {
        chr.props + ("lifetime" -> (chr.props("lifetime") - 1))
      }
      
      if (chr.props("lifetime") > 0) candidates + chr
    }
    
    offspring.foreach { chr => candidates + chr}
    
    return new Population(candidates)
  }

}

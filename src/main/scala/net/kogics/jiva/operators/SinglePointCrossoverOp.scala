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

import net.kogics.jiva.population.Chromosome
import net.kogics.jiva.util.Shuffler
import net.kogics.jiva.util.collection.JList

/**
 * Single Point crossover. 
 * Probabilistically combines two parents after splitting them at a single point
 * to form two offsprings
 */
class SinglePointCrossoverOp[A](pc: Option[Double], rgen: Random) 
extends AbstractCrossoverOp[A](pc, rgen) {

  def this(pc: Double, rgen: Random) = this(Some(pc), rgen)

  def this(rgen: Random) = this(None, rgen)
  
  
  override def crossover(chr1: Chromosome[A], chr2: Chromosome[A]) : (Chromosome[A], Chromosome[A]) = {
    val csite = crossoverSite(chr1)
    val c1 = new Chromosome(chr1.take(csite) ++ chr2.drop(csite))
    val c2 = new Chromosome(chr2.take(csite) ++ chr1.drop(csite))
    return (c1, c2)
  }
  
  def crossoverSite(chr: Chromosome[A]): Int = {
    rgen.nextInt(chr.size)    
  }
}

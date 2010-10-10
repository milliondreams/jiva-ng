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


/**
 * Variant of Single Point crossover that works in chunks
 * The split point has to be at a chunk boundary
 */
class ChunkedCrossoverOp[A](opc: Option[Double], rgen: Random, pas: Seq[Int]) 
extends SinglePointCrossoverOp[A](opc, rgen) {
  
  def this(pc: Double, rgen: Random, pas: Seq[Int]) = this(Some(pc), rgen, pas)

  def this(rgen: Random, pas: Seq[Int]) = this(None, rgen, pas)

  override def crossoverSite(chr: Chromosome[A]): Int = {
    var csite = rgen.nextInt(chr.size)
    pas.find {pos => if (csite < pos) true else false}.getOrElse(pas.last)
  }
}


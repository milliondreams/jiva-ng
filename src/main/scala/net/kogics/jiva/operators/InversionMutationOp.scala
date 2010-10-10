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


class InversionMutationOp(opm: Option[Double], rgen: Random) extends AbstractMutationOp[jint](opm, rgen) {

  def this(pm: Double, rgen: Random) = {
    this(Some(pm), rgen)
  }
  
  def this(rgen: Random) = {
    this(None, rgen)
  }
  
  protected [operators] def mutate(cpm: Double, chr: Chromosome[jint]): Chromosome[jint] = {
      if (rgen.nextDouble < cpm) {
        mutate(chr)
      }
      else {
        chr
      }
  }
  
  private def mutate(chr: Chromosome[jint]): Chromosome[jint] = {
    // find the genes that need to be swapped
    var first = rgen.nextInt(chr.size/2)
    var second = chr.size/2 + rgen.nextInt(chr.size/2)

    if (second - first < 2) return chr
    
    new Chromosome(chr.take(first) ++ chr.slice(first, second).reverse ++ chr.drop(second))
  }
}

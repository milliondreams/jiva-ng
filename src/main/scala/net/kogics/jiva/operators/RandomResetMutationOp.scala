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

class RandomResetMutationOp[A](opm: Option[Double], rgen: Random) extends AbstractMutationOp[A](opm, rgen) {
  
  def this(pm: Double, rgen: Random) = this(Some(pm), rgen)
  
  def this(rgen: Random) = this(None, rgen)
  
  protected [operators] def mutate(cpm: Double, chr: Chromosome[A]): Chromosome[A] = {
      chr.map { gene => 
        if (rgen.nextDouble < cpm) {
          gene.mutate
        }
        else {
          gene
        }
      }
    
  }
  
}

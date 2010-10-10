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
import net.kogics.jiva.population.{Chromosome, Gene}
import net.kogics.jiva.util.Shuffler
import net.kogics.jiva.util.collection.JList

class PartiallyMappedCrossoverOp(opc: Option[Double], rgen: Random) 
extends AbstractCrossoverOp[jint](opc, rgen) {
  
  def this(pc: Double, rgen: Random) = this(Some(pc), rgen)

  def this(rgen: Random) = this(None, rgen)
  
  
  override def crossover(chr1: Chromosome[jint], chr2: Chromosome[jint]) : (Chromosome[jint], Chromosome[jint]) = {
    
    var first = rgen.nextInt(chr1.size/2)
    var second = chr1.size/2 + rgen.nextInt(chr1.size/2)
    
    if (second - first < 2) return (chr1, chr2)
    
    val child1 = pmx(chr1, chr2, first, second)
    val child2 = pmx(chr2, chr1, first, second)
    (child1, child2)
  }
  
  def pmx(chr1: Chromosome[jint], chr2: Chromosome[jint], first: Int, second: Int) : Chromosome[jint] = {
    val child = new JList[Gene[jint]](chr1.size)
    
    val section1 = chr1.slice(first, second)
    val section2 = chr2.slice(first, second)
    
    child(first) = section1
    
    var idx = first
    section2.foreach {gene =>
      if (!section1.contains(gene)) {
        // which gene replaced the current one in section2 within the child
        var replacer = chr1(idx)
        // where does that gene reside in chr2
        var loc = chr2.indexOf(replacer)
        while(loc >= first && loc < second) {
          replacer = chr1(loc)
          loc = chr2.indexOf(replacer)
        }
        // this gene can go at that location in the child
        child(loc) = gene
      }
      idx += 1
    }
    
    idx = 0
    chr2.foreach {gene => if(!child.contains(gene)) child(idx)=gene; idx +=1}
    new Chromosome(child)
  }
}

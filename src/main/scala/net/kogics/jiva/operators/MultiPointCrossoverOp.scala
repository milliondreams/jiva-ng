/*
 * Copyright (C) 2007 Vipul Pandey <vipandey@gmail.com>
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
 * MultiPoint crossover. 
 * Probabilistically combines two parents after splitting them and crossing over at multiple points
 * to form two offsprings
 */
class MultiPointCrossoverOp[A](pc: Option[Double], rgen: Random,num:Int) 
extends AbstractCrossoverOp[A](pc, rgen) {

  def this(pc: Double, rgen: Random,num:Int) = this(Some(pc), rgen,num)

  def this(rgen: Random,num:Int) = this(None, rgen,num)
  
  println("Creating MultiPointCrossoverOp with num = "+num)
  
  override def crossover(chr1: Chromosome[A], chr2: Chromosome[A]) : (Chromosome[A], Chromosome[A]) = {
    var indices:List[Int] = Nil

    while(indices.length < num){
      val index = crossoverSite(chr1)
      if(!indices.exists(x =>  x == index)){
        indices = index::indices
      }
    }

    val csites = indices.sortWith((x,y) => y>x)

    return crossover(chr1,chr2,csites)
  }
  
  def crossoverSite(chr: Chromosome[A]): Int = {
    rgen.nextInt(chr.size)    
  }
  
  def crossover(chr1: Chromosome[A],chr2: Chromosome[A],csites:List[Int]) : (Chromosome[A],Chromosome[A]) = {
             val csite::remaining = csites
		     val c1 = new Chromosome(chr1.take(csite) ++ chr2.drop(csite))
		     val c2 = new Chromosome(chr2.take(csite) ++ chr1.drop(csite))

             remaining match {
             case List() => (c1,c2)
             case _ => crossover(c1,c2,remaining)
             }

  }
  
}


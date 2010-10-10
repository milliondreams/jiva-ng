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

package net.kogics.jiva.population

import net.kogics.jiva.Predef._
import scala.collection.mutable.HashMap
import net.kogics.jiva.util.collection.JList


/*
 * Chromosome companion object. Provides helper methods
 *
 */
object Chromosome {
  
  /**
   * Create a Chromosome out of suppled String of 0s and 1s  
  */
  def apply(genes: String) : Chromosome[jbool] = {
    val g = new JList[Gene[jbool]]
    genes.foreach { allele =>
      allele match {
      case '0' => g + Gene(false)
      case '1' => g + Gene(true)
      case _ => throw new IllegalArgumentException("Only 0s and 1s allowed in gene string")
      }
    }
    return new Chromosome(g)
  }
  
  /**
   * Create a Chromosome out of suppled String of [0-9]s  
  */
  def apply(genes: String, max: Int) : Chromosome[jint] = {
    val g = new JList[Gene[jint]]
    genes.foreach { allele => 
      g += Gene(allele.toInt - '0'.toInt, max)
    }
    return new Chromosome(g)
  }
}

/*
 * A Chromosome represents an individual in Genotype space
 *
 */
class Chromosome[A] (final val genes: Seq[Gene[A]]) extends JChromosome[A] with Ordered[Chromosome[A]]{
  var fitness: Option[Double] = None
  val props = new HashMap[String, long]
  
  def length = genes.length
  def size = genes.size
  def elements = genes.elements
  def apply(n: Int) = genes(n)
  def indexOf(gene: Gene[A]) = genes.indexOf(gene)
  
  override def equals(other: Any) : Boolean = {
    return genes.sameElements(other.asInstanceOf[Chromosome[A]].genes)
  }
  
  override def hashCode = {
    val hc = genes.foldLeft(0){(hc, gene) => hc + gene.hashCode}
    hc / genes.size
  }
  
  def map(f: Gene[A] => Gene[A]): Chromosome[A] = {
    val gs = genes.map(f)
    return new Chromosome(gs)
  }
  
  def foreach(f: Gene[A] => Unit) = genes.foreach(f)
  
  def take(n: Int) = genes.take(n)
  def drop(n: Int) = genes.drop(n)
  def slice(n:Int, m: Int) = genes.slice(n, m)
  def += (a: A) = genes.asInstanceOf[JList[A]] += a

  
  def clone2 : Chromosome[A]= {
    // this.map { gene => gene}
    
    // share underlying immutable genes
    new Chromosome(genes)
  }
  
  def randomize : Chromosome[A]= {
    this.map { gene => gene.randomize}
  }
  
  def matchingGenes(other: Chromosome[A]): Int = {
    var score = 0
    var idx = 0

    /*
    genes.foreach {gene =>
      if (gene.equals(other(idx))) score += 1
      idx += 1
    }
    */
    
    // the functional way of iterating over 'genes' (above) slows SpearsScape down 
    // by ~50%. So we do this the imperitive way (below)
    
    var sz = genes.size
    while (idx < sz) {
      if (genes(idx).equals(other(idx))) score += 1
      idx += 1
      
    }

    return score
  }
  
  override def toString = genes.toString

  // java support:
  // need to make genes be of type JSeq; need to extend JSeq to make that work
  // to get away from the runtime cast
  def iterator = genes.asInstanceOf[JList[JGene[A]]].underlying.iterator
  def fitnessVal = fitness.get

  def compare(that:Chromosome[A]) : Int = fitness.get match {
  case fness if fness < that.fitness.get => -1
  case fness if fness > that.fitness.get => 1
  case _ => 0
  }
 
}

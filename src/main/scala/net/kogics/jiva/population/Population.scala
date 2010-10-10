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
import net.kogics.jiva.evolution.FitnessEvaluator
import net.kogics.jiva.util.collection.JList

import java.util.TreeSet
import scala.collection.JavaConversions._


object Population {
  def empty[A] = new Population[A](Nil)

  def apply[A](chrs: Chromosome[A]*): Population[A] = {
    val chroms = new JList[Chromosome[A]]
    chrs.foreach { chr => chroms += chr} 
    new Population(chroms)
  }
}

class Population[A](final val members: Seq[Chromosome[A]]) extends JPopulation[A] {
  def length = members.length
  def size = members.size
  def elements = members.iterator
  private var fittestChr:Option[Chromosome[A]] = None
  
  def apply(n: Int) = members(n)

  def evaluate(evaluator: FitnessEvaluator[A], prevPop: Population[A]) = {
    evaluator.evaluate(this, prevPop)
  }
  
  def map(f: Chromosome[A] => Chromosome[A]): Population[A] = {
    val chrs = members.map(f)
    return new Population(chrs)
  }
  
  def foreach(f: Chromosome[A] => Unit) = members.foreach(f)
  
  def sameElements[B >: A](that: Iterable[B]): Boolean = members.sameElements(that)   

  def fittestChromosome: Chromosome[A] = {
    fittestChr.getOrElse {
      var maxFitness = -1.0
      var fittest: Chromosome[A] = null 
      members.foreach { chr =>
        if (chr.fitness.get > maxFitness) {
          maxFitness = chr.fitness.get
          fittest = chr
        }
      }
      fittestChr = Some(fittest)
      fittest
    }
  }
  
  def fittestChromosomes(num:Int) : Seq[Chromosome[A]] = {
    val set = new TreeSet[Chromosome[A]]
    members.foreach { chr => set += chr}
    JList(set.toList.reverse.take(num))
  }
  
  override def equals(other: Any) : Boolean = other match {
    case null => false
    case otherP: Population[A] => members.sameElements(otherP.members)
    case _ => false
  }
  
  override def hashCode = throw new UnsupportedOperationException
  
  override def toString = members.toString
  
  // java support:
  // need to make members be of type JSeq; need to extend JSeq to make that work
  // to get away from the runtime cast
  def iterator = members.asInstanceOf[JList[JChromosome[A]]].underlying.iterator
}
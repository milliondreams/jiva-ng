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

/*
 * Gene companion object. Provides helper factory methods that potetially make
 * use of flyweights
 *
 */
object Gene {
  
  type Pair[A, B] = (Gene[A], Gene[B])
  
  final val trueGene = new BooleanGene(true)
  final val falseGene = new BooleanGene(false)
  final val intMap = new java.util.concurrent.ConcurrentHashMap[(Int,Int), IntGene]

  
  def apply(allele: Boolean): Gene[jbool] = if (allele) trueGene else falseGene

  def apply(allele: Int, max: Int): Gene[jint] = {
    checkArg(allele <= max, "allele value: %d - should not be greater than max allowed: %d", allele, max)
    var gene = intMap.get((allele,max))
    if (gene == null) {
      // println("creating gene for: " + allele)
      gene = new IntGene(allele, max)
      intMap.putIfAbsent((allele,max), gene)
    }
    gene
  }

}

/**
 * A Gene is the building block of a Chromosome  
 * 
 */
abstract class Gene[A](final val allele: A) extends JGene[A] {
  override def toString = allele.toString

  /*
  override def equals(other: Any) : Boolean = {
    return allele.equals(other.asInstanceOf[Gene[A]].allele)
  }
   */
  
  def randomize : Gene[A]
  
  def mutate : Gene[A] = {
    var mutated = randomize
    while (mutated.allele == allele) mutated = randomize
    mutated
  }
}

class BooleanGene private[population] (allelex: Boolean) extends Gene[jbool](allelex) {
  def randomize : Gene[jbool] = if (Randomizer().rgen.nextBoolean) Gene(true) else Gene(false)
  override def mutate : Gene[jbool] = if (allele) Gene(false) else Gene(true)
  override def toString = if (allele) "1" else "0"
}

class IntGene private[population] (allelex: Int, max: Int) extends Gene[jint](allelex) {
  def randomize : Gene[jint] = {
    val ra = Randomizer().rgen.nextInt(max+1)
    Gene(ra, max)
  }
}

class GPairGene[A, B] (a: Gene[A], b: Gene[B]) extends Gene[Gene.Pair[A, B]]((a, b)) {
  def randomize : Gene[Gene.Pair[A, B]] = new GPairGene(a.randomize, b.randomize)
  override def equals(other: Any) : Boolean = {
    return allele.equals(other.asInstanceOf[Gene[Gene.Pair[A, B]]].allele)
  }
  override def hashCode = (a.hashCode + b.hashCode)/2
}

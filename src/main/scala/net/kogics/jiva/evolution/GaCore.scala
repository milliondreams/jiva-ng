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
import net.kogics.jiva.Predef._
import net.kogics.jiva.population.{Population, Chromosome}

trait ParentSelector[A] {
  def select(pop: Population[A]) : Population[A]
}

trait FitnessEvaluator[A] {
  def evaluate(offspring: Population[A], currPop: Population[A]) : Unit
}

trait Replacer[A] {
  def replace(currPop: Population[A], offspring: Population[A]) : Population[A]
}

trait FitnessFunction[A] {
  final def apply(chr: Chromosome[A]) : Double = {
    val f = evaluate(chr)
    check(!(f < 0), "Fitness function needs to return a non-negative value; it returned: %f.", f)
    f
  }
  def evaluate(chr: Chromosome[A]) : Double
}



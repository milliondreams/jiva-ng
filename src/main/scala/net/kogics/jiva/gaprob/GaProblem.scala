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

package net.kogics.jiva.gaprob

import net.kogics.jiva.Predef._
import net.kogics.jiva.evolution.{FitnessFunction, GaEvolver}
import net.kogics.jiva.population.{Chromosome, Gene, Population}

class GaProblem[A] {
  private [gaprob] var name: String = _
  private [gaprob] var popSize: Int = _
  private [gaprob] var chrSize: Int = _
  private [gaprob] var evolver: GaEvolver[A] = _
  private [gaprob] var rcf: () => Chromosome[A] = _
  private [gaprob] var numEvolutions: Int = _
  private [gaprob] var fitnessFunction: FitnessFunction[A] = _
  private [gaprob] var rseed: Long = _

  def populationSize = popSize
  def numEv = numEvolutions
  def ff = fitnessFunction
  
  def randomChr: Chromosome[A] = rcf()
  def run: Population[A] = this.run(None)
  def run(initialPop: Population[A]): Population[A] = this.run(Some(initialPop)) 
  def run(initialPop: Option[Population[A]]): Population[A] = evolver.run(initialPop)
}
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

package net.kogics.jiva.jbridge

import net.kogics.jiva.evolution.FitnessFunction
import net.kogics.jiva.population.Chromosome
import net.kogics.jiva.fitness.JFitnessFunction

class JFitnessWrapper[A](jff: JFitnessFunction[A]) extends FitnessFunction[A] {

  def evaluate(chr: Chromosome[A]) : Double = {
    jff.evaluate(chr)
  }

}

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
package net.kogics.jiva.gaprob

import net.kogics.jiva.Predef._
import net.kogics.jiva.population.Population

class ChainedGaProblems[A](val gaProbs: Seq[GaProblem[A]]) extends GaProblem[A] {
  
  checkArg(gaProbs.length != 0, "No Problem Set to run")
  
  override def run() : Population[A] = {
    var pop: Option[Population[A]] = None
    gaProbs.foreach(gaProb => pop = Some(gaProb.run(pop)))
    pop.get
  }
}	

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
package net.kogics.jiva

import net.kogics.jiva.gaprob._


object JivaMain {
  def main(args : Array[String]) : Unit = {
    
    if (args.length != 1) {
      println("Usage: jiva problemFile")
      exit(1)
    }
    
    val problem = ProbLoader.load(args(0))
    
    val t1 = System.currentTimeMillis
    val pop = problem.run
    val t2 = System.currentTimeMillis
    
    println("Best Fitness:" + pop.fittestChromosome.fitness.get.toString)
    println("Fittest Chr:" + pop.fittestChromosome.toString)
    println("Time taken: " + (t2-t1) + " ms")
  }
}

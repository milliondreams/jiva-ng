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
package net.kogics.jiva.sample.scheduler.varparam

import net.kogics.jiva.operators.DynamicVariationProb
import net.kogics.jiva.population.Population

class GenVarMutationProb[A](initProb:Double,numGenerations: Int) extends DynamicVariationProb[A]{
  
  var popNumber:Int = 0
  var probability = initProb
  
  
  def beginGen(pop: Population[A]){
    popNumber += 1
  }
  
  def applyImpl(): Double = {
    if(popNumber != 0 && popNumber%100 == 0){
      probability = initProb/(popNumber/100);      
      println( "Changing mutationProbility at "+popNumber  +" generation to " + probability)
    }
    probability
  }

}

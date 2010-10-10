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
package net.kogics.jiva.sample.scheduler.endnested

import net.kogics.jiva.population.{Chromosome,Gene,IntGene}
import net.kogics.jiva.evolution.FitnessFunction
import net.kogics.jiva.Predef._



class SchedulingEndNestedFFW(ff:FitnessFunction[jint],baseChr:Chromosome[jint],median:jint) extends IntChrNestedFF(ff,baseChr){

  def newGene(baseGene:Gene[jint],delta:Gene[jint]) : Gene[jint] = {
    //added the delta  to the base gene
    // here the delta is measured around the median .... 
      // if deltagene's allele is 20 and median is 15 then delta is 5
      // if deltagene's allele is 10 and median is 15 then delta is -5
   
    var newVal = baseGene.allele+delta.allele-median
    if(newVal < 0) newVal= 0
    Gene.apply(newVal,newVal+1) //the maxValue here doesn't matter because this is a temporary Gene 
  }   

}

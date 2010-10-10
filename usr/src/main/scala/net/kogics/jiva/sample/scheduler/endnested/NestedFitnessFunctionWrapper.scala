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

import net.kogics.jiva.population.{Chromosome,Gene}
import net.kogics.jiva.evolution.FitnessFunction
import net.kogics.jiva.Predef._

abstract class NestedFitnessFunctionWrapper[A](ff:FitnessFunction[A],baseChr:Chromosome[A]) extends FitnessFunction[A] {

    def evaluate(chr: Chromosome[A]) : Double = {
      //check for the length of the choromosome .. it should be equal to that of the baseChr
//        checkArg(chr.length == baseChr.length, "Invalid Chromosome : Length not equal to baseChromosome's")
       
      //create a new chromosome with every gene mapping to original gene affected with the delta gene
                 
	   val chr2 = newChr(chr)
    //chr2.fitness = chr.fitness
       //evaluate the new Chromosome .. which is baseChr affectedWith deltaChr
      ff.evaluate(chr2)
    }

    def newChr(chr:Chromosome[A]) : Chromosome[A] = {
      chr.map(
	      gene => {
	        //index (locus) of the baseGene for this gene should be the same as that of this one
	        // get the baseGene from baseChr and apply 'delta' to it
           val index = chr.indexOf(gene)
	        newGene(baseChr(index),gene)
	      })
      
    }
    
    def newGene(baseGene:Gene[A],delta:Gene[A]) : Gene[A]

}

abstract class IntChrNestedFF(ff:FitnessFunction[jint],baseChr:Chromosome[jint]) extends NestedFitnessFunctionWrapper(ff,baseChr){
  
    def newGene(baseGene:Gene[jint],delta:Gene[jint]) : Gene[jint]   
}

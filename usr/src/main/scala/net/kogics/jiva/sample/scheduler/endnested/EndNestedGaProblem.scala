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

import net.kogics.jiva.gaprob.{GaProblem,ProbBuilder}
import net.kogics.jiva.population.{Population,Chromosome,Gene}
import net.kogics.jiva.evolution.FitnessFunction
import net.kogics.jiva.util.collection.JList

import scala.collection.jcl.TreeSet


abstract class EndNestedGaProblem[A](val mainGaProb:GaProblem[A],buildNestedProblem:(int,FitnessFunction[A]) => GaProblem[A],ff:FitnessFunction[A],nff:FitnessFunction[A],count:int,median:int)  extends GaProblem[A] {

  override def run() : Population[A] = {	
    //run the main GA problem to get the run
    val mainPopulation:Population[A] = mainGaProb run

    //get 'count' fitest solutions
    val mChrs:Seq[Chromosome[A]]  = mainPopulation.fittestChromosomes(count)

    // list of new Chromosomes
    var nChrs = new JList[Chromosome[A]] 
    
    //for each of the best 'count' chorosome of the original ga run
    mChrs.foreach { mChr => {
                    println("main:"+mChr.fitness)
                        println("\nmain:"+mChr)
                   //add the original chromosome to nChrs
                   nChrs =  nChrs + mChr 
                   // run the nested problem for the current chromosome ... add the new 'count' choromosome to nChrs 
                   runNestedProblem(mChr).foreach{ 
                     nChr =>{
                        nChr.fitness = new Some(ff.apply(nChr))
                        println("new:"+nChr.fitness)
                        println("new:"+nChr)
                        nChrs = nChrs + nChr
                     }
                   } 
                 }
    }
    //return the new population
    new Population[A](nChrs)
  }
  
 /*
  * run the nested GA for the given chromosome ... and return a sequence of 'count' good solutions 
  */
  def runNestedProblem(chr:Chromosome[A]) : Seq[Chromosome[A]] = {
    //supply the fitness function to the nested GA
    val ffw = fitnessFunctionWrapper(nff,chr,median)
    //create the new gaProblem
    val nGaProblem = buildNestedProblem(chr.length,ffw)


    // run the new ga problem
	val nPopulation = nGaProblem.run
     //get 'count' best solutions discovered in this run
    val nChromosomes = nPopulation.fittestChromosomes(count)
    
    //reuse fittnesFunctionWrapper's newChr method
    nChromosomes.map(nChr => {
                             val fChr = ffw.newChr(nChr)
                             fChr.fitness = nChr.fitness
                             fChr
                              }
                     )
    
  }
  
  /*
   * 
   */
  def fitnessFunctionWrapper(nff:FitnessFunction[A],chr:Chromosome[A],median:int) : NestedFitnessFunctionWrapper[A]
  
  
    

}

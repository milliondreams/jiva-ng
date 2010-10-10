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

import junit.framework.TestCase
import junit.framework.Assert._

import net.kogics.jiva.Predef._
import net.kogics.jiva.evolution.FitnessFunction
import net.kogics.jiva.population.Chromosome

class TestProbBuilder extends TestCase {
  var noOpFfi: FitnessFunction[jint] = _
  var noOpFfb: FitnessFunction[jbool] = _
  
  override def setUp = {
    noOpFfi = new NoOpFf[jint]
    noOpFfb = new NoOpFf[jbool]
  }
  
  def testPermutedProbBuilder = {
    val name = "N Queens"
    val (ps, cs, nume, pm, pc, seed) = (400, 20, 200, 0.5, 0.9, 20)
    
    
    val problem = ProbBuilder.buildPermutedProb {buildr =>
      buildr name name
      buildr rSeed seed
      buildr popSize ps
      buildr chrSize cs 
      buildr numEvolutions nume
      buildr fitnessFunction noOpFfi 
      buildr defaultEvolver (pm, pc)
    }
    
    assert(problem.name == name)
    assert(problem.popSize == ps)
    assert(problem.chrSize == cs)
    assert(problem.numEvolutions == nume)
    assert(problem.fitnessFunction == noOpFfi)
    assert(problem.evolver.mutator.variationProb == pm)
    assert(problem.evolver.crossator.variationProb == pc)
    assert(Randomizer().rseed == seed)
  }

  def testBooleanProbBuilder = {
    val name = "A Boolean Prob"
    val ps = 403; val cs=201; val nume=2000; val pm = 0.05; val pc = 0.93
    
    val problem = ProbBuilder.buildBooleanProb {buildr =>
      buildr name name
      buildr popSize ps
      buildr chrSize cs 
      buildr numEvolutions nume
      buildr fitnessFunction noOpFfb
      buildr defaultEvolver (pm, pc)
    }
    
    assert(problem.name == name)
    assert(problem.popSize == ps)
    assert(problem.chrSize == cs)
    assert(problem.numEvolutions == nume)
    assert(problem.fitnessFunction == noOpFfb)
    assert(problem.evolver.mutator.variationProb == pm)
    assert(problem.evolver.crossator.variationProb == pc)
  }

  def testIntegerProbBuilder = {
    val name = "An Integrer Prob"
    val ps = 203; val cs=104; val cmax=11; val nume=209; val pm = 0.17; val pc = 0.693
    
    val problem = ProbBuilder.buildIntegerProb {buildr =>
      buildr name name
      buildr popSize ps
      buildr chrSize (cs, cmax)
      buildr numEvolutions nume
      buildr fitnessFunction noOpFfi
      buildr defaultEvolver (pm, pc)
    }
    
    assert(problem.name == name)
    assert(problem.popSize == ps)
    assert(problem.chrSize == cs)
    for (i <- Iterator.range(0, 50)) {
      problem.randomChr.foreach{gene => assert(gene.allele <= cmax)}
    }
    assert(problem.numEvolutions == nume)
    assert(problem.fitnessFunction == noOpFfi)
    assert(problem.evolver.mutator.variationProb == pm)
    assert(problem.evolver.crossator.variationProb == pc)
  }

  def testInvalidNamePermutedProbBuilder = {
    val name = "N Queens"
    val ps = 400; val cs=20; val nume=200; val pm = 0.5; val pc = 0.9
    
    try {
      val problem = ProbBuilder.buildPermutedProb {buildr =>
        // buildr name name
        buildr popSize ps
        buildr chrSize cs 
        buildr numEvolutions nume
        buildr fitnessFunction noOpFfi 
        buildr defaultEvolver (pm, pc)
      }
      fail("Name should be required")
    }
    catch {
    case e: IllegalStateException => assert(true)
    }
  }
  
  def testInvalidPsPermutedProbBuilder = {
    val name = "N Queens"
    val ps = 400; val cs=20; val nume=200; val pm = 0.5; val pc = 0.9
    
    try {
      val problem = ProbBuilder.buildPermutedProb {buildr =>
        buildr name name
        // buildr popSize ps
        buildr chrSize cs 
        buildr numEvolutions nume
        buildr fitnessFunction noOpFfi 
        buildr defaultEvolver (pm, pc)
      }
      fail("Pop size should be required")
    }
    catch {
    case e: IllegalArgumentException => assert(true)
    }
  }
  
  def testInvalidCsPermutedProbBuilder = {
    val name = "N Queens"
    val ps = 400; val cs=20; val nume=200; val pm = 0.5; val pc = 0.9
    
    try {
      val problem = ProbBuilder.buildPermutedProb {buildr =>
        buildr name name
        buildr popSize ps
        // buildr chrSize cs 
        buildr numEvolutions nume
        buildr fitnessFunction noOpFfi 
        buildr defaultEvolver (pm, pc)
      }
      fail("Chr size should be required")
    }
    catch {
    case e: IllegalStateException => assert(true)
    }
  }
  
  def testInvalidNumEvPermutedProbBuilder = {
    val name = "N Queens"
    val ps = 400; val cs=20; val nume=200; val pm = 0.5; val pc = 0.9
    
    try {
      val problem = ProbBuilder.buildPermutedProb {buildr =>
        buildr name name
        buildr popSize ps
        buildr chrSize cs 
        // buildr numEvolutions nume
        buildr fitnessFunction noOpFfi 
        buildr defaultEvolver (pm, pc)
      }
      fail("Num Evolutions should be required")
    }
    catch {
    case e: IllegalStateException => assert(true)
    }
  }
  
  def testInvalidFfPermutedProbBuilder = {
    val name = "N Queens"
    val ps = 400; val cs=20; val nume=200; val pm = 0.5; val pc = 0.9
    
    try {
      val problem = ProbBuilder.buildPermutedProb {buildr =>
        buildr name name
        buildr popSize ps
        buildr chrSize cs 
        buildr numEvolutions nume
        // buildr fitnessFunction noOpFfi 
        buildr defaultEvolver (pm, pc)
      }
      fail("Fitness function should be required")
    }
    catch {
    case e: IllegalStateException => assert(true)
    }
  }
  
  def testInvalidEvPermutedProbBuilder = {
    val name = "N Queens"
    val ps = 400; val cs=20; val nume=200; val pm = 0.5; val pc = 0.9
    
    try {
      val problem = ProbBuilder.buildPermutedProb {buildr =>
        buildr name name
        buildr popSize ps
        buildr chrSize cs 
        buildr numEvolutions nume
        buildr fitnessFunction noOpFfi 
        // buildr defaultEvolver (pm, pc)
      }
      fail("Evolver should be required")
    }
    catch {
    case e: IllegalStateException => assert(true)
    }
  }
  
  def testInvalidRseedPermutedProbBuilder = {
    val name = "N Queens"
    val ps = 400; val cs=20; val nume=200; val pm = 0.5; val pc = 0.9
    
    try {
      val problem = ProbBuilder.buildPermutedProb {buildr =>
        buildr name name
        buildr popSize ps
        buildr chrSize cs 
        buildr numEvolutions nume
        buildr fitnessFunction noOpFfi 
        buildr defaultEvolver (pm, pc)
        buildr rSeed 20
      }
      fail("Rseed should be specified before evolver")
    }
    catch {
    case e: IllegalStateException => assert(true)
    }
  }
  
  class NoOpFf[A] extends  FitnessFunction[A] {
    def evaluate(chr: Chromosome[A]) : Double = 0
  }
}

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

import scala.util.Random

import net.kogics.jiva.Predef._
import net.kogics.jiva.evolution._
import net.kogics.jiva.operators._
import net.kogics.jiva.population.{Chromosome, Gene, GPairGene}
import net.kogics.jiva.util.ShufflerImpl
import net.kogics.jiva.util.collection.JList

object JProbBuilder {
  def integerProbBuilder: IntegerProbBuilder = new IntegerProbBuilder
}

object ProbBuilder {
  
  def buildIntegerProb(f: IntegerProbBuilder => Unit): GaProblem[jint] = {
    val probDef = new IntegerProbBuilder
    f(probDef)
    probDef build
  }
  
  def buildPermutedProb(f: PermutedProbBuilder => Unit): GaProblem[jint] = {
    val probDef = new PermutedProbBuilder
    f(probDef)
    probDef build
  }
  
  def buildBooleanProb(f: BooleanProbBuilder => Unit): GaProblem[jbool] = {
    val probDef = new BooleanProbBuilder
    f(probDef)
    probDef build
  }

  def buildProblem_II(f: Pair_II_ProbBuilder => Unit): GaProblem[Gene.Pair[jint, jint]] = {
    val probDef = new Pair_II_ProbBuilder
    f(probDef)
    probDef build
  }
  
}

abstract class ProbBuilder[A] {
  private [gaprob] var problem:GaProblem[A] = new GaProblem[A]
  
  def name(name: String) = {
    problem.name = name
  }
  
  def apply() = problem
  def rgen = Randomizer().rgen
  
  def popSize(popSize: Int) = problem.popSize = popSize
  def numEvolutions(x: Int) = problem.numEvolutions = x
  def fitnessFunction(ff: Chromosome[A] => Double) = {
    val ff2 = new FitnessFunction[A] {
      def evaluate(chr: Chromosome[A]) : Double = {
        ff(chr)
      }
    }
    problem.fitnessFunction = ff2
  }

  def fitnessFunction(ff: FitnessFunction[A]) = problem.fitnessFunction = ff
  
  def fitnessFunction(jff: net.kogics.jiva.fitness.JFitnessFunction[A]) = {
    val ff = new net.kogics.jiva.jbridge.JFitnessWrapper(jff)
    problem.fitnessFunction = ff
    
  } 
  
  def build: GaProblem[A] = {
    checkState(problem.name != null, "Name undefined for GA Problem.")
    checkState(problem.popSize != 0, "Population Size undefined for GA Problem.")
    checkState(problem.chrSize != 0, "Chromosome Size undefined for GA Problem.")
    checkState(problem.evolver != null, "Evolver undefined for GA Problem.")
    checkState(problem.rcf != null, "Random chromosome generator undefined for GA Problem.")
    checkState(problem.numEvolutions != 0, "Num Evolutions undefined for GA Problem.")
    checkState(problem.fitnessFunction != null, "Fitness function undefined for GA Problem.")
    problem    
  }
  
  def defaultEvolver(pm: Double, pc: Double)

  def evolver(selector: ParentSelector[A], replacer: Replacer[A], evaluator: FitnessEvaluator[A], 
              mutOp: MutationOp[A], crossOp: CrossoverOp[A]) = {
    val e = new GaEvolver(problem, selector, mutOp, crossOp, evaluator, replacer)
    problem.evolver = e
  }
  
  def rSeed(seed: Long) {
    checkState(problem.evolver == null, "Too late to change the random seed. Evolver already seeded.")
    Randomizer.seedable
    Randomizer().rseed = seed
  }
}

class BooleanProbBuilder private [gaprob] extends ProbBuilder[jbool] {

  def chrSize(sz: Int) = {
    problem.chrSize = sz
    problem.rcf = boolRandomChr(sz) _
  }
  
  def boolRandomChr(sz: Int)() = {
    val genes = new JList[Gene[jbool]]()
    for (i <- Iterator.range(0, sz)) {
      genes += Gene(true).randomize
    }
    new Chromosome(genes)
  }
  
  def defaultEvolver(pm: Double, pc: Double) = {
    val popDelta = math.ceil(problem.popSize / 4.0).toInt
    val selector = new TournamentSelector[jbool](popDelta, Randomizer().rgen, 4)
    val replacer = new ApgaReplacer[jbool]
    val evaluator = new ApgaFitnessEvaluator[jbool](problem.fitnessFunction)
    val mutOp = new RandomResetMutationOp[jbool](pm, Randomizer().rgen)
    val crossOp = new SinglePointCrossoverOp[jbool](pc, Randomizer().rgen)
    val e = new GaEvolver(problem, selector, mutOp, crossOp, evaluator, replacer)
    problem.evolver = e
  }
}

class IntegerProbBuilder private [gaprob] extends ProbBuilder[jint] {

  def chrSize(sz: Int, max: Int) = {
    problem.chrSize = sz
    problem.rcf = integerRandomChr(sz, max) _
  }
  
  def integerRandomChr(sz: Int, max: Int)() = {
    val genes = new JList[Gene[jint]]()
    for (i <- Iterator.range(0, sz)) {
      genes += Gene(0, max).randomize
    }
    new Chromosome(genes)
  }
  
  def defaultEvolver(pm: Double, pc: Double) = {
    val popDelta = math.ceil(problem.popSize / 4.0).toInt
    val selector = new TournamentSelector[jint](popDelta, Randomizer().rgen, 4)
    val replacer = new ApgaReplacer[jint]
    val evaluator = new ApgaFitnessEvaluator(problem.fitnessFunction)
    val mutOp = new RandomResetMutationOp[jint](pm, Randomizer().rgen)
    val crossOp = new SinglePointCrossoverOp[jint](pc, Randomizer().rgen)
    val e = new GaEvolver(problem, selector, mutOp, crossOp, evaluator, replacer)
    problem.evolver = e
  }
}

class PermutedProbBuilder private [gaprob] extends ProbBuilder[jint] {

  def chrSize(sz: Int) = {
    problem.chrSize = sz
    problem.rcf = permutedRandomChr(sz) _
  }
  
  def permutedRandomChr(sz: Int)() = {
    val genes = new JList[Gene[jint]]()
    for (i <- Iterator.range(0, sz)) {
      genes += Gene(i, sz-1)
    }
    genes.shuffle
    new Chromosome(genes)
  }
  
  def defaultEvolver(pm: Double, pc: Double) = {
    val popDelta = math.ceil(problem.popSize / 4.0).toInt
    val selector = new TournamentSelector[jint](popDelta, Randomizer().rgen, 4)
    val replacer = new ApgaReplacer[jint]
    val evaluator = new ApgaFitnessEvaluator(problem.fitnessFunction)
    val mutOp = new InversionMutationOp(pm, Randomizer().rgen)
    val crossOp = new PartiallyMappedCrossoverOp(pc, Randomizer().rgen)
    val e = new GaEvolver(problem, selector, mutOp, crossOp, evaluator, replacer)
    problem.evolver = e
  }
}

class Pair_II_ProbBuilder private [gaprob] extends ProbBuilder[Gene.Pair[jint, jint]] {

  def chrSize(sz: Int, max1: Int, max2: Int) = {
    problem.chrSize = sz
    problem.rcf = permutedChr(sz, max1, max2) _
  }
  
  def permutedChr(sz: Int, max1: Int, max2: Int)() = {
    val genes = new JList[Gene[Gene.Pair[jint, jint]]]()
    for (i <- Iterator.range(0, sz)) {
      genes += new GPairGene(Gene(0, max1).randomize, Gene(0, max2).randomize) 
    }
    genes.shuffle
    new Chromosome(genes)
  }
  
  def defaultEvolver(pm: Double, pc: Double) = {
    val popDelta = math.ceil(problem.popSize / 4.0).toInt
    val selector = new TournamentSelector[Gene.Pair[jint, jint]](popDelta, Randomizer().rgen, 4)
    val replacer = new ApgaReplacer[Gene.Pair[jint, jint]]
    val evaluator = new ApgaFitnessEvaluator[Gene.Pair[jint, jint]](problem.fitnessFunction)
    val mutOp = new RandomResetMutationOp[Gene.Pair[jint, jint]](pm, Randomizer().rgen)
    val crossOp = new SinglePointCrossoverOp[Gene.Pair[jint, jint]](pc, Randomizer().rgen)
    val e = new GaEvolver(problem, selector, mutOp, crossOp, evaluator, replacer)
    problem.evolver = e
  }
}


package jiva

import net.kogics.jiva.gaprob._
import net.kogics.jiva.population.Gene

trait ProbDefiner {
  type jbool = net.kogics.jiva.Predef.jbool 
  type jint = net.kogics.jiva.Predef.jint
  type JFitnessWrapper[A] = net.kogics.jiva.jbridge.JFitnessWrapper[A]
  
  def buildIntegerProb(f: IntegerProbBuilder => Unit): GaProblem[jint] = {
    ProbBuilder.buildIntegerProb(f)
  }
  
  def buildPermutedProb(f: PermutedProbBuilder => Unit): GaProblem[jint] = {
    ProbBuilder.buildPermutedProb(f)
  }
  
  def buildBooleanProb(f: BooleanProbBuilder => Unit): GaProblem[jbool] = {
    ProbBuilder.buildBooleanProb(f)
  }

  def buildProblem_II(f: Pair_II_ProbBuilder => Unit): GaProblem[Gene.Pair[jint, jint]] = {
    ProbBuilder.buildProblem_II(f)
  }
}

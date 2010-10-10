package net.kogics.jiva.operators

import net.kogics.jiva.Predef._
import net.kogics.jiva.population.{Population, Chromosome}

trait GeneticOperator[A] {
  def variationProb: Double
  def operate(pop: Population[A]) : Population[A]
  protected [operators] def operate(pv: Double, pop: Population[A]): Population[A]
}

class IdentityGeneticOp[A] extends GeneticOperator[A] {
  def variationProb: Double = 0
  def operate(pop: Population[A]) : Population[A] = pop
  protected [operators] def operate(pm: Double, pop: Population[A]): Population[A] = pop
}

trait MutationOp[A] extends GeneticOperator[A] {
  protected [operators] def mutate(pm: Double, chr: Chromosome[A]): Chromosome[A] 
}

trait CrossoverOp[A] extends GeneticOperator[A] {
  protected [operators] def crossover(chr1: Chromosome[A], chr2: Chromosome[A]) : (Chromosome[A], Chromosome[A]) 
}

class IdentityMutationOp[A] extends IdentityGeneticOp[A] with MutationOp[A] {
  protected [operators] def mutate(pm: Double, chr: Chromosome[A]): Chromosome[A] = chr
} 

class IdentityCrossoverOp[A] extends IdentityGeneticOp[A] with CrossoverOp[A] {
  protected [operators] def crossover(chr1: Chromosome[A], chr2: Chromosome[A]) : (Chromosome[A], Chromosome[A]) = (chr1, chr2) 
} 

trait DynamicVariationProb[A] {
  final def apply(): Double = {
    val p = applyImpl
    checkArg(p >= 0 && p <= 1, "Mutation probability should be between 0 and 1 (inclusive): %f", p)
    p
  }
  def applyImpl(): Double
  def beginGen(pop: Population[A])
}

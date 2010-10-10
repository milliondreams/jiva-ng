package net.kogics.jiva.operators

import scala.util.Random

import net.kogics.jiva.Predef._
import net.kogics.jiva.population.Population

abstract class AbstractGeneticOp[A](opv: Option[Double], rgen: Random) extends GeneticOperator[A] {

  if (opv.isDefined) {
    val pv = opv.get
    checkArg(pv >= 0 && pv <= 1, "Variation probability should be between 0 and 1 (inclusive): %f", pv)
  }

  def variationProb: Double = opv.getOrElse(invalidState)

  def operate(pop: Population[A]) : Population[A] = {
    operate(opv.getOrElse(invalidState), pop)
  }
  
  def invalidState = throw new IllegalStateException("Pm was not specified")
}

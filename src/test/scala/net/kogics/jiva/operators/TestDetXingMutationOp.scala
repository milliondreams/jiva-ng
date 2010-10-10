package net.kogics.jiva.operators

import scala.util.Random

import junit.framework.TestCase
import junit.framework.Assert._
import net.kogics.jiva.population.Population

class TestDetXingMutationOp extends TestCase {

  def testConstruction: Unit = {
    val realOp = new RandomResetMutationOp[Boolean](new Random)
    val dvp = new DynamicVariationProb[Boolean] {
      def applyImpl(): Double = 0.3
      def beginGen(pop: Population[Boolean]) = {}
    }
    val xingOp = new DetXingMutationOp[Boolean](dvp, realOp)
    
    try {
      realOp.operate(Population.empty)
      fail("Direct Usage of wrapped operator should not be allowed")
    }
    catch {
    case e: IllegalStateException => assert(true)
    }
  }
  
}

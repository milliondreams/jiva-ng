package net.kogics.jiva.evolution

import scala.util.Random

import net.kogics.jiva.Predef._
import net.kogics.jiva.population._
import junit.framework._
import junit.framework.Assert._
import org.jmock.Mockery
import org.jmock.lib.legacy.ClassImposteriser
import org.jmock.Expectations
import org.jmock.Expectations._

class TestRouletteWheelSelector extends TestCase with net.kogics.jiva.TestUtils {
  val context = new Mockery() {
    {
      setImposteriser(ClassImposteriser.INSTANCE)
    }
  }
  
  def testSelectorMidRange = {
    val fitnesses = List(0.1, 0.2, 0.3, 0.4)
    val probs = List(0.01, 0.4, 0.7, 0.9)
    val expectedPop = Population(initialPop(0), initialPop(2), initialPop(3), initialPop(3))
    testHelper(fitnesses, probs, initialPop, expectedPop)
  }
  
  def testSelectorLeftEdge = {
    val fitnesses = List(0.05, 0.25, 0.4, 0.3)
    val probs = List(0.01, 0.03, 0.02, 0.04)
    val expectedPop = Population(initialPop(0), initialPop(0), initialPop(0), initialPop(0))
    testHelper(fitnesses, probs, initialPop, expectedPop)
  }
  
  def testSelectorRightEdge = {
    val fitnesses = List(0.05, 0.25, 0.4, 0.3)
    val probs = List(0.8, 0.9, 0.85, 0.95)
    val expectedPop = Population(initialPop(3), initialPop(3), initialPop(3), initialPop(3))
    testHelper(fitnesses, probs, initialPop, expectedPop)
  }
  
  private def testHelper(fitnesses: List[Double], probs: List[Double], 
                         pop: Population[jbool], expectedPop: Population[jbool]) = {
    
    var idx = 0
    pop.foreach {chr => chr.fitness = Some(fitnesses(idx)); idx += 1}
    
    val rg = (context.mock(classOf[Random])).asInstanceOf[Random]
    context.checking(
      new Expectations() {{
          atLeast(1).of(rg).nextDouble
          will(onConsecutiveCalls(returnConsecutiveValues(probs): _*))
        }
      })
 
    val selector = new RouletteWheelSelector[jbool](pop.size, rg)
    val pop2 = selector.select(pop)
 
    assert(pop2 == expectedPop)
    context.assertIsSatisfied
    
  }
  
  def testInvalidSelsize = {
    try {
      new RouletteWheelSelector[jbool](0, new Random)
      fail("Invalid Selection Size should not be allowed")
    }
    catch {
      case e: IllegalArgumentException => assertTrue(true)
    }
  }
  
  
}

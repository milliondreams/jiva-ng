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
  
class TestTournamentSelector extends TestCase with net.kogics.jiva.TestUtils {
 
  val context = new Mockery() {
    {
      setImposteriser(ClassImposteriser.INSTANCE)
    }
  }
  
  def test2Selection = {
    val expectedPop = 
      Population(initialPop(2), initialPop(3), initialPop(1), initialPop(3))
    
    val ifn = Array(0.1, 0.2, 0.3, 0.4) 
    var idx = 0
    initialPop.foreach {chr => chr.fitness = Some(ifn(idx)); idx += 1}
    
    val rg = (context.mock(classOf[Random])).asInstanceOf[Random]
    context.checking(
      new Expectations() {{
          atLeast(1).of(rg).nextInt(`with`(any(classOf[jint])))
          will(onConsecutiveCalls(returnConsecutiveValues(List(2, 0, 1, 3, 0, 1, 2, 3)): _*))
        }
      })
 
    val selector = new TournamentSelector[jbool](initialPop.size, rg, 2)
    val pop2 = selector.select(initialPop)
 
    assert(pop2 == expectedPop)
    context.assertIsSatisfied
  }

  def test3Selection = {
    val expectedPop = 
      Population(initialPop(0), initialPop(2), initialPop(3), initialPop(1))
    
    val ifn = Array(0.4, 0.3, 0.2, 0.1) 
    var idx = 0
    initialPop.foreach {chr => chr.fitness = Some(ifn(idx)); idx += 1}
    
    val rg = (context.mock(classOf[Random])).asInstanceOf[Random]
    context.checking(
      new Expectations {
        atLeast(1).of(rg).nextInt(`with`(any(classOf[jint])))
        will(onConsecutiveCalls(returnConsecutiveValues(List(2, 0, 1, 3, 3, 2, 3, 3, 3, 2, 2, 1)): _*))
      })
 
    val selector = new TournamentSelector[jbool](initialPop.size, rg, 3)
    val pop2 = selector.select(initialPop)
 
    assert(pop2 == expectedPop)
    context.assertIsSatisfied
  }

  def testInvalidK = {
    try {
      new TournamentSelector[jbool](4, new Random, 1)
      fail("Invalid K should not be allowed")
    }
    catch {
      case e: IllegalArgumentException => assertTrue(true)
    }
  }

  def testInvalidSelsize = {
    try {
      new TournamentSelector[jbool](0, new Random, 3)
      fail("Invalid Selection Size should not be allowed")
    }
    catch {
      case e: IllegalArgumentException => assertTrue(true)
    }
  }
  
  def testInvalidPopsize = {
    try {
      val selector = new TournamentSelector[jbool](4, new Random, 4)
      selector.select(initialPop)
      fail("Population size should be greater than K")
    }
    catch {
      case e: IllegalArgumentException => assertTrue(true)
    }
  }
}

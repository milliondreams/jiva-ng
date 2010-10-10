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

package net.kogics.jiva.operators

import scala.util.Random

import junit.framework.TestCase
import junit.framework.Assert._
import org.jmock.Mockery
import org.jmock.lib.legacy.ClassImposteriser
import org.jmock.Expectations
import org.jmock.Expectations._

import net.kogics.jiva.Predef._
import net.kogics.jiva.population.{Population, Chromosome}
import net.kogics.jiva.util.NoOpShuffler

class TestRandomResetMutationOp extends TestCase with net.kogics.jiva.TestUtils with ProbabilityChecker[jbool] {
 
  val context = new Mockery() {
    {
      setImposteriser(ClassImposteriser.INSTANCE)
    }
  }
  
  def testMutation = {

    val expectedPop= 
      Population(Chromosome("001101"), Chromosome("001101"), Chromosome("101101"), Chromosome("100000"))

    
    val rg = (context.mock(classOf[Random])).asInstanceOf[Random]
    context.checking(
      new Expectations() {{		
	    atLeast(1).of(rg).nextDouble()
     will(onConsecutiveCalls(returnConsecutiveValues(List(0.4, 0.4, 0.01,
                                                            0.4, 0.4, 0.001, 0.4, 0.4, 0.4, 0.4, 0.4, 0.4, 0.4,
                                                            0.4, 0.4, 0.4, 0.4, 0.4, 0.4, 0.01, 0.4, 0.4, 0.4, 0.001)): _*))
      }	
      })	
 
    val mutator = new RandomResetMutationOp[jbool](0.01, rg)
    val pop2 = mutator.operate(initialPop)
 
    assert(pop2 == expectedPop)
    context.assertIsSatisfied
    
  }
  
  def testNoMutation = {

    val expectedPop = initialPop  
      
    val rg = (context.mock(classOf[Random])).asInstanceOf[Random]
    context.checking(
      new Expectations() {{		
	    atLeast(1).of(rg).nextDouble()
     will(onConsecutiveCalls(returnConsecutiveValues(List(0.4, 0.4, 0.01,
                                                            0.4, 0.4, 0.001, 0.4, 0.4, 0.4, 0.4, 0.4, 0.4, 0.4,
                                                            0.4, 0.4, 0.4, 0.4, 0.4, 0.4, 0.01, 0.4, 0.4, 0.4, 0.001)): _*))
      }	
      })	
 
    val mutator = new RandomResetMutationOp[jbool](0.0001, rg)
    val pop2 = mutator.operate(initialPop)
 
    assert(pop2 == expectedPop)
    context.assertIsSatisfied
    
  }
  
  def testAllMutation = {

    val expectedPop = initialPop.map {_.map {_.mutate}}  
      
    val rg = (context.mock(classOf[Random])).asInstanceOf[Random]
    context.checking(
      new Expectations() {{		
	    atLeast(1).of(rg).nextDouble()
     will(onConsecutiveCalls(returnConsecutiveValues(List(0.4, 0.4, 0.01,
                                                            0.4, 0.4, 0.001, 0.4, 0.4, 0.4, 0.4, 0.4, 0.4, 0.4,
                                                            0.4, 0.4, 0.4, 0.4, 0.4, 0.4, 0.01, 0.4, 0.4, 0.4, 0.001)): _*))
      }	
      })	
 
    val mutator = new RandomResetMutationOp[jbool](0.75, rg)
    val pop2 = mutator.operate(initialPop)
 
    assert(pop2 == expectedPop)
    context.assertIsSatisfied
    
  }
  
  override def createObjUnderTest(prob: Double): RandomResetMutationOp[jbool] = new RandomResetMutationOp[jbool](prob, new Random)
  
}

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

package net.kogics.jiva.operators

import scala.util.Random

import junit.framework.TestCase
import org.jmock.Mockery
import org.jmock.lib.legacy.ClassImposteriser
import org.jmock.Expectations._
import net.kogics.jiva.util.mock.SExpectations

import net.kogics.jiva.Predef._
import net.kogics.jiva.population.{Population, Chromosome}
import net.kogics.jiva.util.NoOpShuffler

class TestMultiPointCrossoverOp extends TestCase with TestUtils with ProbabilityChecker[jbool] {
 
  val context: Mockery = new Mockery() {
    {
      setImposteriser(ClassImposteriser.INSTANCE)
    }
  }
  
  def testCrossover = {
    val expectedPop= 
      Population(Chromosome("001101"), Chromosome("101101"), Chromosome("000000"), Chromosome("101101"))
    
    val rg = (context.mock(classOf[Random])).asInstanceOf[Random]
    
    context.checking(
	  new SExpectations() {
	    {	
	      atLeast(1).of(rg).nextDouble()	
       will(onConsecutiveCalls(returnConsecutiveValues(List(0.31, 0.66,
                        0.71, 0.39)): _*))
       atLeast(1).of(rg).nextInt(6)
       will(onConsecutiveCalls(returnConsecutiveValues(List(2, 4)): _*))
	    }	
	  })
 
    val crossator = new MultiPointCrossoverOp[jbool](0.5, rg,2)
    crossator.shuffler = new NoOpShuffler[Chromosome[jbool]]
    val pop2 = crossator.operate(initialPop)
 
    assert(pop2 == expectedPop)
    context.assertIsSatisfied
  }

   def testOddNumberCrossoverWithAdd()  = {
    val expectedPop= 
      Population(Chromosome("001100"), Chromosome("001101"), Chromosome("101101"), Chromosome("100001"))
    
    val rg = (context.mock(classOf[Random])).asInstanceOf[Random]

    context.checking(new SExpectations() {
      {
        // pc calls
        atLeast(1).of(rg).nextDouble()
        will(onConsecutiveCalls(returnConsecutiveValues(List(0.31, 0.45, 0.71, 0.39)): _*))

        // add to odd?
        one(rg).nextBoolean()
        will(returnValue(true))
        
        // calls:
        // which one to add to odd
        // crossover site
        // crossover site
	    atLeast(1).of(rg).nextInt(withArg(any(classOf[jint])))
        will(onConsecutiveCalls(returnConsecutiveValues(List(0, 2, 4,2,4)): _*))
       }
     })

        
        
    val crossator = new MultiPointCrossoverOp[jbool](0.5, rg,2)
    crossator.shuffler = new NoOpShuffler[Chromosome[jbool]]
    val pop2 = crossator.operate(initialPop)
        
    assert(pop2 == expectedPop)
    context.assertIsSatisfied
  }

  def testOddNumberCrossoverWithRemove()  = {
    val expectedPop= 
      Population(Chromosome("101101"), Chromosome("001100"), Chromosome("000001"), Chromosome("101101"))
    
    val rg = (context.mock(classOf[Random])).asInstanceOf[Random]

    context.checking(new SExpectations() {
      {
        // pc calls
        atLeast(1).of(rg).nextDouble()
        will(onConsecutiveCalls(returnConsecutiveValues(List(0.31, 0.45, 0.71, 0.39)): _*))

        // add to odd?
        one(rg).nextBoolean()
        will(returnValue(false))
        
        // calls:
        // which one to remove from odd
        // crossover site
	    atLeast(1).of(rg).nextInt(withArg(any(classOf[jint])))
        will(onConsecutiveCalls(returnConsecutiveValues(List(0,1,4)): _*))
       }
     })

    val crossator = new MultiPointCrossoverOp[jbool](0.5, rg,2)
    crossator.shuffler = new NoOpShuffler[Chromosome[jbool]]
    val pop2 = crossator.operate(initialPop)
        
    assert(pop2 == expectedPop)
    context.assertIsSatisfied
  }
  
  
  override def createObjUnderTest(prob: Double): MultiPointCrossoverOp[jbool] = 
    new MultiPointCrossoverOp[jbool](prob, new Random,2)
}
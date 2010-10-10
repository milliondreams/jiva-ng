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
import org.jmock.Mockery
import org.jmock.lib.legacy.ClassImposteriser
import org.jmock.Expectations._
import net.kogics.jiva.util.mock.SExpectations

import net.kogics.jiva.Predef._
import net.kogics.jiva.population.{Population, Chromosome}
import net.kogics.jiva.util.NoOpShuffler

class TestInversionMutationOp extends TestCase with TestUtils with ProbabilityChecker[jint] {
 
  val context = new Mockery() {
    {
      setImposteriser(ClassImposteriser.INSTANCE)
    }
  }
  
  def testMutation = {
    val pop = Population(Chromosome("123456", 6), Chromosome("123456", 6), 
           Chromosome("123456", 6), Chromosome("123456", 6), Chromosome("123456", 6))
    
    
    val expectedPop = Population(Chromosome("143256", 6), Chromosome("123456", 6), 
           Chromosome("543216", 6), Chromosome("123456", 6), Chromosome("132456", 6))
    
    val rg = (context.mock(classOf[Random])).asInstanceOf[Random]
    context.checking(new SExpectations() {
      {
        atLeast(1).of(rg).nextDouble()
        will(onConsecutiveCalls(returnConsecutiveValues(List(0.1, 0.3, 0.15, 0.4, 0.11)): _*))
	    atLeast(1).of(rg).nextInt(withArg(any(classOf[jint])))
        will(onConsecutiveCalls(returnConsecutiveValues(List(1, 1, 0, 2, 1, 0)): _*))
      }
    })
 
    val mutator = new InversionMutationOp(0.2, rg)
    val pop2 = mutator.operate(pop)
 
    assert(pop2 == expectedPop)
    context.assertIsSatisfied
    
  }

  def testNoMutation = {
    val pop = Population(Chromosome("123456", 6))
    
    val expectedPop = pop
    
    val rg = (context.mock(classOf[Random])).asInstanceOf[Random]
    context.checking(new SExpectations() {
      {
        atLeast(1).of(rg).nextDouble()
        will(onConsecutiveCalls(returnConsecutiveValues(List(0.1)): _*))
	    atLeast(1).of(rg).nextInt(withArg(any(classOf[jint])))
        will(onConsecutiveCalls(returnConsecutiveValues(List(2, 0)): _*))
      }
    })
 
    val mutator = new InversionMutationOp(0.2, rg)
    val pop2 = mutator.operate(pop)
 
    assert(pop2 == expectedPop)
    context.assertIsSatisfied
    
  }

  override def createObjUnderTest(prob: Double): InversionMutationOp = 
    new InversionMutationOp(prob, new Random)
}

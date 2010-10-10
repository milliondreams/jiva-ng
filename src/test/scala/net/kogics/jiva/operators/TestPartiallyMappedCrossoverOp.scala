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
import org.jmock.Expectations
import org.jmock.Expectations._

import net.kogics.jiva.Predef._
import net.kogics.jiva.population.{Population, Chromosome}
import net.kogics.jiva.util.NoOpShuffler


class TestPartiallyMappedCrossoverOp extends TestCase with net.kogics.jiva.TestUtils with ProbabilityChecker[jint] {
 
  val context: Mockery = new Mockery() {
    {
      setImposteriser(ClassImposteriser.INSTANCE)
    }
  }
  
  def testCrossover = {
    val pop = new Population(
      List(Chromosome("123456789", 9), Chromosome("123456789", 9), Chromosome("937826514", 9))
    )
    
    val expectedPop = new Population(
      List(Chromosome("123456789", 9), Chromosome("932456718", 9), Chromosome("173826549", 9))
    )
    
    val rg = (context.mock(classOf[Random])).asInstanceOf[Random]
    
    context.checking(
      new Expectations {	
        atLeast(1).of(rg).nextDouble()	
        will(onConsecutiveCalls(returnConsecutiveValues(List(0.7, 0.3,
                        0.4)): _*))
        atLeast(1).of(rg).nextInt(`with`(any(classOf[jint])))
        will(onConsecutiveCalls(returnConsecutiveValues(List(3, 3)): _*))
      })
 
    val crossator = new PartiallyMappedCrossoverOp(0.5, rg)
    crossator.shuffler = new NoOpShuffler[Chromosome[jint]]
    val pop2 = crossator.operate(pop)
 
    assert(pop2 == expectedPop)
    context.assertIsSatisfied
  }

  override def createObjUnderTest(prob: Double): PartiallyMappedCrossoverOp = 
    new PartiallyMappedCrossoverOp(prob, new Random)
}
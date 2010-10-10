package net.kogics.jiva

import net.kogics.jiva.Predef._

import net.kogics.jiva.population._
import org.jmock.Expectations
import org.jmock.api.Action

trait TestUtils {
  
  val initialPop = 
    Population(Chromosome("001100"), Chromosome("001101"), Chromosome("101101"), Chromosome("100001"))
  
  def returnConsecutiveValues[T] (values: List[T]) : Array[Action] =  {
      val actions = new Array[Action](values.size)
      var i = 0
      values.foreach(value => {actions(i) = Expectations.returnValue(value); i += 1})
      return actions
  }

}

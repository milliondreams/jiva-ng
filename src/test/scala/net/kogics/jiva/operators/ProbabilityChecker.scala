package net.kogics.jiva.operators

import junit.framework.Assert._

trait ProbabilityChecker[A] {
  
  def createObjUnderTest(prob: Double): GeneticOperator[A]
  
  def testPmZero = {
    try {
      createObjUnderTest(0.0)
      // new RandomResetMutation[jbool](0.0, new Random)
      assert(true)
    }
    catch {
    case e: IllegalArgumentException => fail("Mutation probability of 0.0 should be allowed") 
    }
  }

  def testPmBelowZero = {
    try {
      createObjUnderTest(-0.01)
      // new RandomResetMutation[jbool](-0.01, new Random)
      fail("Mutation probability cannot be negative")
    }
    catch {
    case e: IllegalArgumentException => assert(true)
    }
  }

  def testPmOne = {
    try {
      createObjUnderTest(1.0)
      // new RandomResetMutation[jbool](1.0, new Random)
      assert(true)
    }
    catch {
    case e: IllegalArgumentException => fail("Mutation probability of 1.0 should be allowed") 
    }
  }

  def testPmAboveOne = {
    try {
      createObjUnderTest(1.01)
      // new RandomResetMutation[jbool](1.01, new Random)
      fail("Mutation probability cannot be greater than 1.0")
    }
    catch {
    case e: IllegalArgumentException => assert(true)
    }
  }
}

package net.kogics.jiva.util

import scala.util.Random

import junit.framework.TestCase
import junit.framework.Assert._

class TestRandomizer extends TestCase {
  
  override def setUp = {
    Randomizer.reset
  }
  
  def testSeeding = {
    Randomizer.seedable
    Randomizer().rseed = 20
    assert(Randomizer().rseed == 20)
  }
  
  def testInvalidThreadUse1 = {
    val t = new Thread(new Runnable {
      def run = {
        Randomizer.seedable
        Randomizer().rseed = 20
      }
    })
    t.start
    t.join
    
    try {
      Randomizer().rseed = 20
      fail("Seedable Randomizer should not be usable in main thread")
    }
    catch {
    case e: IllegalStateException => assert(true)
    }
  }

  def testInvalidThreadUse2 = {
    val t = new Thread(new Runnable {
      def run = {
        Randomizer.seedable
        Randomizer().rseed = 20
      }
    })
    t.start
    t.join
    
    try {
      val rgen = Randomizer().rgen
      fail("Seedable Randomizer should not be usable in main thread")
    }
    catch {
    case e: IllegalStateException => assert(true)
    }
  }

  def testPRandomizer = {
    val rgen1 = Randomizer().rgen
    val ct = System.currentTimeMillis
    Randomizer.reset
    val s2 = Randomizer().rseed
    val rgen2 = Randomizer().rgen
    assert (rgen1 != rgen2)
    assert(ct - s2 < 1000)
  }
  
  def testInvalidPRandomizerSeeding = {
    try {
      Randomizer().rseed = 20
      fail("Default Randomizer not seedable")
    }
    catch {
    case e: UnsupportedOperationException => assert(true)
    }
  }
  
  def testInvalidPRandomizerInit = {
    try {
      Randomizer().rgen = new Random
      fail("Default Randomizer not initable")
    }
    catch {
    case e: UnsupportedOperationException => assert(true)
    }
  }
}

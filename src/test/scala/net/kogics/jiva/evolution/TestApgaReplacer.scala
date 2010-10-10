package net.kogics.jiva.evolution

import net.kogics.jiva.Predef._
import junit.framework._
import net.kogics.jiva.population._

class TestApgaReplacer extends TestCase {

  def testReplacement = {
    val currPop = Population(Chromosome("10110"), Chromosome("10001"), Chromosome("00100"),
                      Chromosome("11000"), Chromosome("11011"), Chromosome("11100"),
                      Chromosome("10001"), Chromosome("00111"))
  
    val newPop = Population(Chromosome("11111"), Chromosome("10111"), Chromosome("01011"),
                      Chromosome("01100"), Chromosome("10000"), Chromosome("00000"))
  
    currPop.foreach {chr => chr.props("lifetime") = 10; chr.fitness = Some(9.9)}
    currPop(2).props("lifetime") = 1
    currPop(7).props("lifetime") = 1

    val replacer = new ApgaReplacer[jbool]
    val pop2 = replacer.replace(currPop, newPop)
    
    var idx = 0
    currPop.foreach {chr =>
      if (idx != 2 && idx != 7) assert(pop2.elements.contains(chr)) 
      idx += 1
    }
    newPop.foreach {chr => assert(pop2.elements.contains(chr))}
  }
}

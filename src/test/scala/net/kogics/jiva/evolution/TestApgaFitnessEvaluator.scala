package net.kogics.jiva.evolution

import net.kogics.jiva.Predef._
import junit.framework._
import net.kogics.jiva.population._

class TestApgaFitnessEvaluator extends TestCase {
  
  
  def testFitnessAndLifetime = {
    val currPop = Population(Chromosome("10110"), Chromosome("10001"), Chromosome("00100"),
                      Chromosome("11000"), Chromosome("11011"), Chromosome("11100"),
                      Chromosome("10001"), Chromosome("00111"))
  
    val newPop = Population(Chromosome("11111"), Chromosome("10111"), Chromosome("01011"),
                      Chromosome("01100"), Chromosome("10000"), Chromosome("00000"))
  
    
    val ff = new FitnessFunction[jbool] {
      def evaluate(chr: Chromosome[jbool]) : Double = {
        var score = 0.0
        chr.foreach {gene => if (gene.allele) score += 1}
        score
      }
    }
    
    val evaluator = new ApgaFitnessEvaluator(ff)
    evaluator.evaluate(currPop, Population.empty)
    evaluator.evaluate(newPop, currPop)
    
    assert(newPop(0).fitness.get == 5)
    assert(newPop(0).props("lifetime") == 11)
    
    assert(newPop(1).fitness.get == 4)
    assert(newPop(1).props("lifetime") == 9)
    
    assert(newPop(2).fitness.get == 3)
    assert(newPop(2).props("lifetime") == 7)
    
    assert(newPop(3).fitness.get == 2)
    assert(newPop(3).props("lifetime") == 5)
    
    assert(newPop(4).fitness.get == 1)
    assert(newPop(4).props("lifetime") == 3)

    assert(newPop(5).fitness.get == 0)
    assert(newPop(5).props("lifetime") == 1)
  }
}

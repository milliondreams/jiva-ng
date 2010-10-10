package net.kogics.jiva

import net.kogics.jiva.evolution._
import net.kogics.jiva.operators._
import net.kogics.jiva.population._
import net.kogics.jiva.gaprob._
import net.kogics.jiva.util._

object AllTests  {
  
  def suite : junit.framework.Test = {
    val suite = new junit.framework.TestSuite    
    suite.addTestSuite(classOf[TestApgaFitnessEvaluator])
    suite.addTestSuite(classOf[TestApgaReplacer])
    suite.addTestSuite(classOf[TestTournamentSelector])
    suite.addTestSuite(classOf[TestRouletteWheelSelector])
    suite.addTestSuite(classOf[TestChunkedCrossoverOp])
    suite.addTestSuite(classOf[TestInversionMutationOp])
    suite.addTestSuite(classOf[TestPartiallyMappedCrossoverOp])
    suite.addTestSuite(classOf[TestRandomResetMutationOp])
    suite.addTestSuite(classOf[TestSinglePointCrossoverOp])
    suite.addTestSuite(classOf[TestMultiPointCrossoverOp])
    suite.addTestSuite(classOf[TestChromosome])
    suite.addTestSuite(classOf[TestProbBuilder])
    suite.addTestSuite(classOf[TestDetXingMutationOp])
    suite.addTestSuite(classOf[TestRandomizer])
    return suite
  }
}

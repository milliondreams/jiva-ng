import net.kogics.jiva.operators._
import net.kogics.jiva.evolution._
import net.kogics.jiva.util.ShufflerImpl

class SpearsScape extends jiva.ProbDefiner {
  def gaProblem = buildBooleanProb {buildr =>
    buildr name "SpearsScape"
    // buildr rSeed 42
    buildr popSize 100
    buildr chrSize 100
    buildr numEvolutions 200
    buildr fitnessFunction new SpearsFitness(buildr)

    val popDelta = Math.ceil(100 / 4.0).toInt
    val selector = new TournamentSelector[jbool](popDelta, buildr.rgen, 4)
    val replacer = new ApgaReplacer[jbool]
    val evaluator = new ApgaFitnessEvaluator[jbool](buildr().ff)
    val mutOp = new RandomResetMutationOp[jbool](0.01, buildr.rgen)
    val crossOp = new SinglePointCrossoverOp[jbool](0.9, buildr.rgen)
    buildr evolver (selector, replacer, evaluator, mutOp, crossOp)
  } 
}

// The fitness function should normally be written in a separate file
// The problem definition should just pull it in 

import net.kogics.jiva.Predef._
import net.kogics.jiva.gaprob._
import net.kogics.jiva.population._
import net.kogics.jiva.util._
import net.kogics.jiva.util.collection._

class SpearsFitness (buildr: ProbBuilder[jbool]) extends FitnessFunction[jbool] {

  object SpearsScale extends Enumeration {
    val Linear, Sqrt, Log = Value
  }

  val numPeaks = 100
  val lowestHeight = 0.5
  val scale = SpearsScale.Linear 
  
  var peaks: List[Chromosome[jbool]] = Nil
  var peakHeights: List[Double] = Nil
  
  val diff = 1.0 - lowestHeight
  val linDiff = diff / (numPeaks - 1)
  
  for (idx <- List.range(0, numPeaks)) {
    var chr = buildr().randomChr
    peaks = chr :: peaks
    val peakHeight: Double = if (numPeaks == 1) 1 else 1 - idx * linDiff
    peakHeights = peakHeight :: peakHeights
  }
  
  def evaluate(chr: Chromosome[jbool]) : Double = {
    var score, highestFitness, nearestPeakAt = 0
    var idx = 0
    peaks.foreach {peak =>
      score = chr.matchingGenes(peak)
      if (score > highestFitness) {
        nearestPeakAt = idx
        highestFitness = score
      }
      idx += 1
      }
    return peakHeights(nearestPeakAt) * highestFitness / chr.size
  }
}

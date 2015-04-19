#### Spears Landscape Problem (SpearsScape.scala) ####
The Spears Landscape consists of a number of random L-bit strings, where each string represents the location of a peak in an L-dimensional space. The problem consists of identifying the location of the highest peak.

The difficulty of the problem depends on:
  * the number of peaks
  * the height of the lowest peak (problem difficulty goes up with this height)
  * the distribution of peak-heights (problem difficulty goes up when the peaks have heights close to the global optimum)
  * the random layout of the peaks in the search space (problem difficulty goes up when the peaks are more isolated in the search space).

The Parameters for the current problem are:
```
Num Peaks: 100
Highest Peak (Global Optima): 1.0
Lowest Peak: 0.5
Peak Height Distribution: Linear
```

Here we define a problem with a population size of 100, a mutation probability of 0.01, a crossover probability of 0.9, and an evolution count of 200:
```
class SpearsScape extends jiva.ProbDefiner {
  def gaProblem = buildBooleanProb {buildr =>
    buildr name "SpearsScape"
    buildr popSize 100
    buildr chrSize 100
    buildr numEvolutions 200
    buildr fitnessFunction new SpearsFitness(buildr)

    val popDelta = Math.ceil(100 / 4.0).toInt
    val selector = new TournamentSelector[jbool](popDelta, buildr().rgen, 4)
    val replacer = new ApgaReplacer[jbool]
    val evaluator = new ApgaFitnessEvaluator[jbool](buildr().ff)
    val mutOp = new RandomResetMutationOp[jbool](0.01, buildr().rgen)
    val crossOp = new SinglePointCrossoverOp[jbool](0.9, buildr().rgen, new ShufflerImpl)
    buildr evolver (selector, replacer, evaluator, mutOp, crossOp)
  } 
}
```
The chromosome size is 100 because we have 100 peaks (and dimensions) in the problem

This sample shows how to create a GA Evolver with fine grained selection of the Genetic Algorithm components.

The Spears Landscape Fitness function assigns a fitness value to a potential solution based on its Hamming Distance from its nearest peak:
```
class SpearsFitness (buildr: ProbBuilder[jbool]) extends FitnessFunction[jbool] {

  object SpearsScale extends Enumeration {
    val Linear, Sqrt, Log = Value
  }

  val numPeaks = 100
  val lowestHeight = 0.5
  val scale = SpearsScale.Linear 
  
  var peaks: List[Chromosome[jbool]] = Nil
  var peakHeights: List[double] = Nil
  
  val diff = 1.0 - lowestHeight
  val linDiff = diff / (numPeaks - 1)
  
  for (idx <- List.range(0, numPeaks)) {
    var chr = buildr().randomChr
    peaks = chr :: peaks
    val peakHeight: double = if (numPeaks == 1) 1 else 1 - idx * linDiff
    peakHeights = peakHeight :: peakHeights
  }
  
  def evaluate(chr: Chromosome[jbool]) : double = {
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
```

If we try running this problem with Jiva:
```
$jivadir> jiva gaprob/SpearsScape.scala
```
Jiva comes back with:
```
Best Fitness:0.9747474747474747
Fittest Chr:[0, 1, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 0, 1, 0, 1, 1, 0, 0, 1, 1]
Time taken: 1555 ms
```
This is not too bad, given that the optimal fitness for this problem is 1.0

Another run gives us:
```
Best Fitness:1.0
Fittest Chr:[1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0, 1, 0, 1, 1, 0, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 0, 1, 0, 1, 0, 0, 0, 1, 1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1, 1, 0, 0, 1, 0, 1]
Time taken: 1463 ms
```
That's the optimal solution for the problem.
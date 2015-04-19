To use Jiva in command line mode, you need to do the following:

  * Define you problem in a script file in the gaprob directory. More details on this are provided on the [Samples Page](SampleProblems.md), but here's a quick look at what a problem definition script looks like:
```
class NQueens extends jiva.ProbDefiner {
  def gaProblem = buildPermutedProb {probDef =>
    val numq = 10
    probDef name "N Queens"
    probDef popSize 400
    probDef chrSize numq 
    probDef numEvolutions 200
    probDef fitnessFunction new NqFitness(numq)
    probDef defaultEvolver (0.5, 0.9)
  } 
}
```
  * Get Jiva to run your problem:
```
jiva gaprob/yourProblem.scala
```
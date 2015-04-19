#### N Queens (NQueens.scala) ####
This sample shows how to solve the N Queens problem using GAs.

Here we define an NQueens problem with a population size of 50, a mutation probability of 0.5, a crossover probability of 0.9, and an evolution count of 100:

```
class NQueens extends jiva.ProbDefiner {
  def gaProblem = buildPermutedProb {buildr =>
    val numq = 8
    buildr name "N Queens"
    buildr popSize 50
    buildr chrSize numq 
    buildr numEvolutions 100
    buildr fitnessFunction new NqFitness(numq)
    buildr defaultEvolver (0.5, 0.9)
  } 
}
```

For this problem, we use a permutation based Chromosome representation. The size of the Chromosome is the same as the number of queens that we are trying to place on a chess-board. In this case, that is: 8.

The defaultEvolver call in the sample script asks Jiva to assign the best possible GA Evolver to this problem, given the problem type.

The Nqueens Fitness function assigns a fitness value to a potential solution:
```
class NqFitness(numq: int) extends FitnessFunction[jint] {
  def evaluate(chr: Chromosome[jint]) : double = {
    var qs : List[int] = Nil
    chr.foreach {gene =>
      qs = gene.allele :: qs 
    }
    qs = qs.reverse
    
    val c = numq - conflicts(qs)
    if (c < 0) 0 else c
  }
  
  def conflicts(qs: List[int]): double = qs match {
  case Nil => 0
  case hd :: tl => conflicts(hd, tl) + conflicts(tl)
  }
  
  def conflicts(pos1: int, tl: List[int]): double = {
    var idx = 1
    var conflicts = 0
    tl.foreach {pos2 => 
      if (Math.abs(pos2 - pos1) == idx) conflicts += 1
      idx += 1
    }	
    conflicts
  }
}
```


If we try running this problem with Jiva:
```
$jivadir> jiva gaprob/NQueens.scala
```
Jiva comes back with:
```
Best Fitness:8.0
Fittest Chr:[4, 1, 5, 0, 6, 3, 7, 2]
Time taken: 161 ms
```
That's a good solution to the problem.
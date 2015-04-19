#### Knapsack Problem (Knapsack.scala) ####
This is a Combinatorial Optimization problem. The general idea is that we want to pack a Knapsack with a given set of items. Each item has a cost (its volume) and a value assigned to it. The Knapsack has a given (max) volume. The problem is - to pack the Knapsack in such a way that the value of items in the Knapsack is maximized - given the cost (volume) constraint on the Knapsack.

The Parameters for the current problem are:
```
Max Cost: 17
Item Costs: 3.0, 3.0, 3.0, 3.0, 3.0, 4.0, 4.0, 4.0, 7.0, 7.0, 8.0, 8.0, 9.0
Item Values: 4.0, 4.0, 4.0, 4.0, 4.0, 5.0, 5.0, 5.0, 10.0, 10.0, 11.0, 11.0, 13.0
```

Here we define a Knapsack problem with a population size of 40, a mutation probability of 0.1, a crossover probability of 0.9, and an evolution count of 50:
```
class Knapsack extends jiva.ProbDefiner {
  def gaProblem = buildBooleanProb {buildr =>
    buildr name "Knapsack"
    buildr popSize 40
    buildr chrSize 13 
    buildr numEvolutions 50
    buildr fitnessFunction new KnapsackFf
    buildr defaultEvolver (0.1, 0.9)
  } 
}
```
The chromosome size is 13 because 13 items need to be packed into the Knapsack.

The defaultEvolver call in the sample script asks Jiva to assign the best possible GA Evolver to this problem, given the problem type.

The Knapsack Fitness function assigns a fitness value to a potential solution:
```
class KnapsackFf extends FitnessFunction[jbool] {
  
  val maxCost = 17.0
  val itemValues = Array(4.0, 4.0, 4.0, 4.0, 4.0, 5.0, 5.0, 5.0,
                10.0, 10.0, 11.0, 11.0, 13.0)
  
  val itemCosts = Array(3.0, 3.0, 3.0, 3.0, 3.0, 4.0, 4.0, 4.0, 7.0,
                7.0, 8.0, 8.0, 9.0)
  
  def evaluate(chr: Chromosome[jbool]) : double = {
    var value=0.0
    var cost = 0.0
    var idx = 0
    chr.foreach { gene => 
      if (gene.allele) {
        if (cost + itemCosts(idx) <= maxCost) {
          value += itemValues(idx)
        }
        else {
          value = 0
        }
        cost += itemCosts(idx)
      }
      idx += 1
    }
    return value
  }
}
```

If we try running this problem with Jiva:
```
$jivadir> jiva gaprob/Knapsack.scala
```
Jiva comes back with:
```
Best Fitness:23.0
Fittest Chr:[0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0]
Time taken: 98 ms
```
This is not too bad, given that the optimal fitness for this problem is 24.0

Another run gives us:
```
Best Fitness:24.0
Fittest Chr:[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1]
Time taken: 98 ms
```
You can't get any better than that!
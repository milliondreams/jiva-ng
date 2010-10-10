
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

// The fitness function should normally be written in a separate file
// The problem definition should just pull it in 

import net.kogics.jiva.Predef._
import net.kogics.jiva.gaprob._
import net.kogics.jiva.evolution._
import net.kogics.jiva.population._
import net.kogics.jiva.operators._
import net.kogics.jiva.util._
import net.kogics.jiva.util.collection._


class KnapsackFf extends FitnessFunction[jbool] {
  
  val maxCost = 17.0
  val itemValues = Array(4.0, 4.0, 4.0, 4.0, 4.0, 5.0, 5.0, 5.0,
                10.0, 10.0, 11.0, 11.0, 13.0)
  
  val itemCosts = Array(3.0, 3.0, 3.0, 3.0, 3.0, 4.0, 4.0, 4.0, 7.0,
                7.0, 8.0, 8.0, 9.0)
  
  def evaluate(chr: Chromosome[jbool]) : Double = {
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

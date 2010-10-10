
class NQueens extends jiva.ProbDefiner {
  def gaProblem = buildPermutedProb {buildr =>
    val numq = 8
    buildr name "N Queens"
    // buildr rSeed 42
    buildr popSize 50
    buildr chrSize numq 
    buildr numEvolutions 100
    buildr fitnessFunction new NqFitness(numq)
    buildr defaultEvolver (0.5, 0.9)
  } 
}

// The fitness function should normally be written in a separate file
// The problem definition should just pull it in 

import net.kogics.jiva.Predef._
import net.kogics.jiva.population.{Chromosome, Gene}
import net.kogics.jiva.evolution.FitnessFunction
import net.kogics.jiva.util.collection.JList


class NqFitness(numq: Int) extends FitnessFunction[jint] {
  def evaluate(chr: Chromosome[jint]) : Double = {
    var qs : List[Int] = Nil
    chr.foreach {gene =>
      qs = gene.allele :: qs 
    }
    qs = qs.reverse
    
    val c = numq - conflicts(qs)
    if (c < 0) 0 else c
  }
  
  def conflicts(qs: List[Int]): Double = qs match {
  case Nil => 0
  case hd :: tl => conflicts(hd, tl) + conflicts(tl)
  }
  
  def conflicts(pos1: Int, tl: List[Int]): Double = {
    var idx = 1
    var conflicts = 0
    tl.foreach {pos2 => 
      if (Math.abs(pos2 - pos1) == idx) conflicts += 1
      idx += 1
    }	
    conflicts
  }
}

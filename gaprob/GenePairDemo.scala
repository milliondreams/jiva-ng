
class GenePairDemo extends jiva.ProbDefiner {
  def gaProblem = buildProblem_II {buildr =>
    buildr name "GenePairDemo"
    buildr popSize 100
    buildr chrSize(1, 5, 5)
    buildr numEvolutions 100
    buildr fitnessFunction new GenePairDemoFf
    buildr defaultEvolver (0.1, 0.9)
  } 
}

// The fitness function should normally be written in a separate file
// The problem definition should just pull it in 

import net.kogics.jiva.Predef._
import net.kogics.jiva.population.{Chromosome, Gene}
import net.kogics.jiva.evolution.FitnessFunction
import net.kogics.jiva.util.collection.JList


class GenePairDemoFf extends FitnessFunction[Gene.Pair[jint, jint]] {
  def evaluate(chr: Chromosome[Gene.Pair[jint, jint]]) : Double = {
  	val gene = chr(0)
  	val gene1 = gene.allele._1
	val gene2 = gene.allele._2
	gene1.allele * gene2.allele
  }
}

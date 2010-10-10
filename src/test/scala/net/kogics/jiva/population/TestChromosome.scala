package net.kogics.jiva.population

import junit.framework.TestCase
import java.util.HashSet

import net.kogics.jiva.Predef._

class TestChromosome extends TestCase {
  
  def testHashcode = {
    val set = new HashSet[Chromosome[jbool]]
    val chr1 = Chromosome("10110110")
    val chr2 = Chromosome("10110110")
    val chr3 = Chromosome("1010010")
    set.add(chr1)
    set.add(chr2)
    assert(set.size == 1)
    set.add(chr3)
    assert(set.size == 2)
    assert(set.contains(chr1))
    assert(set.contains(chr2))
    assert(set.contains(chr3))
  }
  
  def testFittestChromosome = {
    
    val chr1 = Chromosome("10110110")
     chr1.fitness = new Some(1)
    val chr2 = Chromosome("11110110")
     chr2.fitness = new Some(2)
    val chr3 = Chromosome("1010010")
     chr3.fitness = new Some(3)
    val chr4 = Chromosome("10010110")
     chr4.fitness = new Some(4)
    val chr5 = Chromosome("10100111")
     chr5.fitness = new Some(5)
    val chr6 = Chromosome("10100101")
     chr6.fitness = new Some(6)
    val chr7 = Chromosome("11111110")
     chr7.fitness = new Some(7)
    val chr8 = Chromosome("10110110")
     chr8.fitness = new Some(8)
    
    val list = chr1 :: chr2 :: chr3 :: chr4 :: chr5 :: chr6 :: chr7 :: chr8 :: Nil

    val pop = new Population[jbool](list)
    assert(pop.fittestChromosome() == chr8)
  }
  
  
  def testFittestChromosomes = {
    
    val chr1 = Chromosome("00000001")
     chr1.fitness = new Some(1)
    val chr2 = Chromosome("00000010")
     chr2.fitness = new Some(2)
    val chr3 = Chromosome("00000011")
     chr3.fitness = new Some(3)
    val chr4 = Chromosome("00000100")
     chr4.fitness = new Some(4)
    val chr5 = Chromosome("00000101")
     chr5.fitness = new Some(5)
    val chr6 = Chromosome("00000110")
     chr6.fitness = new Some(6)
    val chr7 = Chromosome("00000111")
     chr7.fitness = new Some(7)
    val chr8 = Chromosome("00001000")
     chr8.fitness = new Some(8)
    
    val list = chr1 :: chr2 :: chr3 :: chr4 :: chr5 :: chr6 :: chr7 :: chr8 :: Nil

    val seq = (new Population[jbool](list)).fittestChromosomes(4)
    
    assert(seq(0) == chr8)
    assert(seq(1) == chr7)
    assert(seq(2) == chr6)
    assert(seq(3) == chr5)
  }
}

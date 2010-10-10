/*
 * Copyright (C) 2007 Vipul Pandey <vipandey@gmail.com>
 *
 * The contents of this file are subject to the GNU General Public License 
 * Version 3 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.gnu.org/copyleft/gpl.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 */
package net.kogics.jiva.population

import junit.framework.TestCase
import net.kogics.jiva.Predef._

class TestFittestChromosomes extends TestCase{
  
 
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
    assert(pop.fittestChromosome().equals(chr8))
  }

}

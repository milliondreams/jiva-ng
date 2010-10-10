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
package net.kogics.jiva.sample.scheduler.endnested

import net.kogics.jiva.Predef._
import net.kogics.jiva.gaprob.{ProbBuilder,GaProblem}
import net.kogics.jiva.evolution.FitnessFunction
import net.kogics.jiva.population.Chromosome

class SchedulingEndNestedGaProblem(override val mainGaProb:GaProblem[jint], buildNestedProblem:(Int,FitnessFunction[jint]) => GaProblem[jint] ,ff:FitnessFunction[jint],nff:FitnessFunction[jint],count:Int,median:Int)
         extends EndNestedGaProblem[jint](mainGaProb,buildNestedProblem,ff,nff,count,median){

     def fitnessFunctionWrapper(nff:FitnessFunction[jint],chr:Chromosome[jint],median:Int) : NestedFitnessFunctionWrapper[jint] = {
      new SchedulingEndNestedFFW(nff,chr,median)
    }
}

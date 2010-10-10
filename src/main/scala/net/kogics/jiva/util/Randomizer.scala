/*
 * Copyright (C) 2007 Lalit Pant <pant.lalit@gmail.com>
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

package net.kogics.jiva.util

import scala.util.Random

import net.kogics.jiva.Predef._

/**
 * A 'Service Locator' that servers as a source for random numbers for the
 * rest of Jiva. Defaults to providing a Randomizer that is suitable for production 
 * use. But once the 'seedable' or 'mockable' method is called, it starts providing
 * a Randomizer that is suitable for testing/debugging. 
 * This debugging Randomizer can be:
 * - seeded with a Long for reproducable GA runs
 * - provided a Mock Random number generator for testing
 * - used only in one thread
 */
object Randomizer {
  @volatile var randomizer: Randomizer = _
  reset
  
  def apply() = randomizer
  def reset: Unit = {randomizer = new PRandomizer}
  def seedable: Unit = {randomizer = new SeedableRandomizer}
  def mockable: Unit = {randomizer = new SeedableRandomizer}
}

trait Randomizer {
  def rgen: Random
  def rgen_=(r: Random): Unit
  
  def rseed: Long
  def rseed_=(nseed: Long): Unit
}

class SeedableRandomizer extends Randomizer {
  private var seed = System.currentTimeMillis
  private var rg = new Random(seed)
  private val creatorThr = Thread.currentThread
  
  def rgen = {checkThread; rg}
  def rgen_=(r: Random): Unit = {checkThread; rg = r; seed = 0}
  
  def rseed = {checkThread; seed}
  def rseed_=(nseed: Long): Unit = {checkThread; seed = nseed; rg = new Random(seed)}
  
  def checkThread = {
    checkState(creatorThr == Thread.currentThread, "Seedable Randomizer can only be used in a single thread")
  }
}

class PRandomizer extends Randomizer {
  private final val seed = System.currentTimeMillis
  private final val rg = new Random(seed)
  
  def rgen = rg
  def rgen_=(r: Random): Unit = throw new UnsupportedOperationException("Production Randomizer cannot be modified")
  
  def rseed = seed
  def rseed_=(nseed: Long): Unit = throw new UnsupportedOperationException("Production Randomizer cannot be modified")
}
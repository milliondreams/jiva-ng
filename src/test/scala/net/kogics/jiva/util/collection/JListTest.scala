package net.kogics.jiva.util.collection

import junit.framework._

class JListTest extends TestCase {

  def testDrop = {
    val jlist = JList(1,2,3,4,5)
    assert(jlist.drop(2).equals(JList(3,4,5)))
  } 
  
}

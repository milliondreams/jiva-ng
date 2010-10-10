package net.kogics

object AllTests {

  def suite : junit.framework.Test = {
    val suite = new junit.framework.TestSuite    
    suite.addTest(net.kogics.jiva.AllTests.suite)
    suite.addTestSuite(classOf[net.kogics.jiva.util.collection.JListTest])
    return suite
  }

}

package net.kogics.jiva

object Predef {

  type jint = java.lang.Integer
  type jbool = java.lang.Boolean
  val Randomizer = net.kogics.jiva.util.Randomizer
  
  implicit def jbool2Boolean(b: jbool) : Boolean = b.booleanValue
  implicit def jint2int(i: jint) : Int = i.intValue
  
  implicit def seq2Array(s: Seq[Any]): Array[AnyRef] = {
    // Borrowed from Console
    val res = new Array[AnyRef](s.length)
    var i: Int = 0
    val iter = s.elements
    while (iter.hasNext) {
      res(i) = iter.next match {
        case x: Boolean => new java.lang.Boolean(x)
        case x: byte    => new java.lang.Byte(x)
        case x: short   => new java.lang.Short(x)
        case x: char    => new java.lang.Character(x)
        case x: int => new java.lang.Integer(x)
        case x: long    => new java.lang.Long(x)
        case x: float   => new java.lang.Float(x)
        case x: Double  => new java.lang.Double(x)
        case x: Unit    => "()"
        case x: AnyRef  => x
      }
      i += 1
    }
    res
  }
  
  /*
  implicit def iter2iter[A](iter: Iterator[A]) : java.util.Iterator[A] = {
    new java.util.Iterator[A] {
      def hasNext = iter.hasNext
      def next = iter.next
      def remove = iter.remove
    }
  }
  */
  
  def checkArg(assertion: Boolean) {
    if (!assertion)
      throw new IllegalArgumentException()
  }

  def checkArg(assertion: Boolean, msg: String, msgArgs: Any*) {
    if (!assertion)
      throw new IllegalArgumentException(String.format(msg, msgArgs))
  }
  
  def check(assertion: Boolean) {
    if (!assertion)
      throw new RuntimeException()
  }

  def check(assertion: Boolean, msg: String, msgArgs: Any*) {
    if (!assertion)
      throw new RuntimeException(String.format(msg, msgArgs))
  }
  
  def checkState(assertion: Boolean) {
    if (!assertion)
      throw new IllegalStateException()
  }

  def checkState(assertion: Boolean, msg: String, msgArgs: Any*) {
    if (!assertion)
      throw new IllegalStateException(String.format(msg, msgArgs))
  }
  
}

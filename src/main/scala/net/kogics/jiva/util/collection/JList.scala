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

package net.kogics.jiva.util.collection

trait JSeq[+A] extends Seq[A] {
  def remove(idx: Int): A
}

object JList {
  def apply[A](xs: A*): JList[A] = {
    val jlist = new JList[A]
    xs.foreach {x => jlist + x}
    jlist
  }

  def apply[A](xs: List[A]): JList[A] = {
    val jlist = new JList[A]
    xs.foreach {x => jlist + x}
    jlist
  }
}

class JList[A](val underlying: java.util.ArrayList[A]) extends JSeq[A] {
  def this() = this(new java.util.ArrayList[A])
  def this(n: Int) = {
    this(new java.util.ArrayList[A](n))
    for (idx <- Iterator.range(0, n)) underlying.add(null.asInstanceOf[A])
  }
  
  val shuffler = new ShufflerImpl[A]
  
  def length = underlying.size
  def apply(idx: Int) = underlying.get(idx)
  
  def elements = new Iterator[A] {
    val underlyingIter = underlying.iterator
    def next = underlyingIter.next
    def hasNext = underlyingIter.hasNext
  }
  
  def + (a: A): JList[A] = {
    underlying.add(a)
    this
  }
  
  def += (a: A): Unit = {
    underlying.add(a)
  }
  
  override def take(n: Int): JList[A] = {
    var m = 0
    val result = new JList[A]
    val i = elements
    while (m < n && i.hasNext) {
      result += i.next; m = m + 1
    }
    result
  }
  
  override def drop(n: Int): JList[A] = {
    var m = 0
    val result = new JList[A]
    val i = elements
    while (m < n && i.hasNext) {
      i.next; m = m + 1
    }
    while (i.hasNext) result += i.next
    
    result
  }
  
  override def reverse : JList[A] = {
    val result = new JList[A]
    val li = underlying.listIterator(underlying.size)
    while (li.hasPrevious) {
      result += li.previous
    }
    result
  }
  
  override def map[B](f: A => B): JList[B] = {
    val buf = new JList[B]
    val elems = elements
    while (elems.hasNext) buf += f(elems.next)
    buf
  }
  
  override def foreach(f: A => Unit) {
    val elems = elements
    while (elems.hasNext) f(elems.next) 
  }  
  
  override def ++ [B >: A](that: Iterable[B]): Seq[B] = {
    val newlist = new JList[B]
    foreach {b => newlist += b}
    that.foreach {b => newlist += b}
    newlist
  }
  
  def update(n: Int, e: A) = {
    underlying.set(n, e)
  }
  
  def update(n: Int, es: Seq[A]) = {
    var idx = n
    es.foreach {e => underlying.set(idx, e); idx += 1}
  }
  
  override def toString = underlying.toString
  
  override def equals(other: Any) = underlying.equals(other.asInstanceOf[JList[A]].underlying)

  def remove(idx: Int) = underlying.remove(idx)
  
  def shuffle = shuffler.shuffle(underlying)
}



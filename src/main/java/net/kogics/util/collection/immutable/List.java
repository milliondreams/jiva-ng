/*
 * Copyright (C) 2007 Lalit Pant <pant.lalit@gmail.com>
 *
 * The contents of this file are subject to the GNU General Public License 
 * Version 2 or later (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.gnu.org/copyleft/gpl.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 */

package net.kogics.util.collection.immutable;

import java.util.Iterator;
import java.util.ListIterator;


/**
 * An immutable list. Based on ideas borowed from Scala and Lisp.
 * 
 * Some advantages of an immutable list: (1) Read access within concurrent
 * programs requires no locking or CAS checks. (2) It is a great data structure
 * to use within bottom-up, recursive algorithms
 * 
 * @author lalitp
 */
public abstract class List<T>
        implements Iterable<T>
{
    public static <T> List<T> create()
    {
        return new Nil<T>();
    }

    public static <T> List<T> fromJclList(java.util.List<T> buffer)
    {
        List<T> ret = new Nil<T>();
        for (ListIterator<T> iter = buffer.listIterator(buffer.size()); iter
                .hasPrevious();) {
            T elem = iter.previous();
            ret = ret.prepend(elem);
        }
        return ret;
    }

    public List<T> prepend(T elem)
    {
        return new Cons<T>(elem, this);
    }

    public abstract List<T> remove(T elem);

    public abstract T head();

    public abstract List<T> tail();

    public abstract boolean isEmpty();

    public boolean contains(T elem)
    {
        if (isEmpty()) {
            return false;
        }

        if (head().equals(elem)) {
            return true;
        }
        else {
            return tail().contains(elem);
        }
    }

    public int size()
    {
        if (isEmpty()) {
            return 0;
        }

        return 1 + tail().size();
    }

    public List<T> drop(int n)
    {
        List<T> newList = this;
        for (int i = 0; i < n; i++) {
            newList = newList.tail();
        }
        return newList;
    }

    public List<T> take(int n)
    {
        ListBuffer<T> buf = new ListBuffer<T>();
        List<T> newList = this;
        for (int i = 0; i < n; i++) {
            buf.add(newList.head());
            newList = newList.tail();
        }
        return List.fromJclList(buf);
    }

    public List<T> slice(int from, int to)
    {
        return drop(from).take(to - from);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(toStringInternal());
        sb.append("]");
        return sb.toString();
    }

    private String toStringInternal()
    {
        if (isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(head().toString());
        if (!tail().isEmpty()) {
            sb.append(",");
        }
        sb.append(tail().toStringInternal());
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof List)) {
            return false;
        }

        List<T> other = (List<T>)obj;

        if (isEmpty()) {
            if (other.isEmpty()) {
                return true;
            }
            else {
                return false;
            }
        }

        return head().equals(other.head()) && tail().equals(other.tail());
    }

    public Iterator<T> iterator()
    {
        return new Iter();
    }

    class Iter
            implements Iterator<T>
    {
        List<T> mylist;

        public Iter()
        {
            mylist = List.this;
        }

        public boolean hasNext()
        {
            return !mylist.isEmpty();
        }

        public T next()
        {
            T retVal = mylist.head();
            mylist = mylist.tail();
            return retVal;
        }

        public void remove()
        {
            throw new UnsupportedOperationException(
                    "remove() on immutable list iterator");
        }
    }
}


class Cons<T>
        extends List<T>
{
    private final T elem;
    private final List<T> next;

    public Cons(T elem, List<T> next)
    {
        this.elem = elem;
        this.next = next;
    }

    @Override
    public List<T> remove(T elem)
    {
        if (this.elem.equals(elem)) {
            return next;
        }
        else {
            return new Cons<T>(this.elem, next.remove(elem));
        }
    }

    @Override
    public T head()
    {
        return elem;
    }

    @Override
    public List<T> tail()
    {
        return next;
    }

    @Override
    public boolean isEmpty()
    {
        return false;
    }

}


class Nil<T>
        extends List<T>
{
    @Override
    public List<T> remove(T elem)
    {
        throw new UnsupportedOperationException("remove() on an empty list");
    }

    @Override
    public T head()
    {
        throw new UnsupportedOperationException("head() on an empty list");
    }

    @Override
    public List<T> tail()
    {
        throw new UnsupportedOperationException("tail() on an empty list");
    }

    @Override
    public boolean isEmpty()
    {
        return true;
    }
}

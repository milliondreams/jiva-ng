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

package net.kogics.util.listener;
import net.kogics.util.collection.immutable.List;

/**
 * A thread-safe broadcaster that does not acquire any locks while
 * broadcasting events. 
 * The broadcaster is based on an immutable list structure 
 * 
 * @author lalitp
 */
public class NonBlockingBroadcaster<T>
    implements EventBroadcaster<T>
{
  volatile List<FilteredListener<T>> listeners = List.create();
  
  public void addListener(EventListener<T> listener)
  {
    addListener(listener, new TrueFilter<T>());
  }

  public void addListener(EventListener<T> listener, Filter<T> filter)
  {
    if (filter == null) {
        throw new IllegalArgumentException("Null filter");
    }
    
    FilteredListener<T> fl = new FilteredListener<T>(listener, filter);
    synchronized (this) {
      listeners = listeners.prepend(fl);
    }
  }

  public void removeListener(EventListener<T> listener)
  {
    removeListener(listener, new TrueFilter<T>());
  }

  public void removeListener(EventListener<T> listener, Filter<T> filter)
  {
    if (filter == null) {
        throw new IllegalArgumentException("Null filter");
    }
    
    FilteredListener<T> fl = new FilteredListener<T>(listener, filter);
    synchronized (this) {
      listeners = listeners.remove(fl);
    }
  }

  public void broadcastEvent(T event)
  {
    for (FilteredListener<T> listener : listeners) {
        listener.eventFired(event);
    }
  }

  static class FilteredListener<T>
      implements EventListener<T>
  {
    final EventListener<T> listener;
    final Filter<T> filter;

    /**
     * @param listener
     * @param filter
     */
    public FilteredListener(EventListener<T> listener, Filter<T> filter)
    {
      this.listener = listener;
      this.filter = filter;
    }

    public void eventFired(T event)
    {
      if (filter.pass(event)) {
        listener.eventFired(event);
      }
    }

    @SuppressWarnings("unchecked")
    public boolean equals(Object other)
    {
      if (other instanceof FilteredListener) {
        FilteredListener<T> otherFl = (FilteredListener<T>)other;
        boolean ret = listener.equals(otherFl.listener);
        if (ret) {
          return filter.equals(otherFl.filter);
        }
        else {
          return false;
        }
      }
      else {
        return false;
      }
    }

    public int hashCode()
    {
      return listener.hashCode() + filter.hashCode();
    }
  }
}
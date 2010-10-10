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

import java.util.ArrayList;

/**
 * A helper class for immutable Lists - for situations where you need to add
 * elements to the end of a list within an algorithms. Immutable lists do not
 * have an add operation; they only support prepending.
 * 
 * @author lalitp
 */
public class ListBuffer<T>
        extends ArrayList<T>
{
	
	private static final long serialVersionUID = 1L;

	public List<T> toList()
    {
        return List.fromJclList(this);
    }
}

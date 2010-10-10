/*
 * Copyright (C) 2007 Vipul Pandey<vipandey@gmail.com>
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
package net.kogics.jiva.sample.scheduler;

import java.util.HashMap;
import java.util.Set;

/**
 * @author vipul
 *
 */
public class ResourcePool {
	
	/**
	 * working pool
	 */
	private HashMap<ActivityType,Integer> pool = new HashMap<ActivityType,Integer>();
	/**
	 * original pool ... to find out the max-values of the resources in this pool
	 */
	private HashMap<ActivityType,Integer> originalPool = new HashMap<ActivityType,Integer>();
	/**
	 * 
	 *
	 */
	public ResourcePool() {}
	
	/**
	 * 
	 * @param type
	 * @param count
	 */
	public void addResources(ActivityType type,Integer count) {
		pool.put(type, count);
		originalPool.put(type,count);
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	public int getResourceCount(ActivityType type) {
		return pool.get(type);
	}
	
	/**
	 * 
	 * 
	 * @param type
	 * @return
	 */
	public boolean checkoutResource(ActivityType type) {
		//not threadsafe -- assuming no concurrant operations ... 
		
		Integer count = pool.get(type);
		if(count == 0) {
			//System.out.println("Checkint out "+type.name()+" - NONE Available ");
			return false;
		}
		//System.out.println("Checkint out "+type.name()+" - Available "+count);
		
		pool.put(type, count-1);
		return true;
			
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	public boolean checkinResource(ActivityType type) {
		//not threadsafe -- assuming no concurrant operations ... 
		Integer count = pool.get(type);
		
		Integer maxCount = originalPool.get(type);
		
		if(count == maxCount) {
			//System.out.println("Ignoring Checking in of "+type.name()+" - Already Available Max resources : "+count);
			return false;
		}else {
			//System.out.println("Checking in "+type.name()+" - Already Available "+count);
			pool.put(type, count+1);
			return true;
		}
			
	}

	/**
	 * 
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		ResourcePool newPool = new ResourcePool();
		newPool.pool = (HashMap)pool.clone();
		newPool.originalPool= (HashMap)originalPool.clone();
		return newPool;
	}
	
	/**
	 * 
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer("Resource Pool ");
		Set<ActivityType> types= pool.keySet();
		for(ActivityType type : types) {
			Integer count = pool.get(type);
			sb.append("\n\t").append(type.name()).append(":").append(count).append(" (").append(originalPool.get(type)).append(")");
		}
		return sb.toString();
	}


	/**
	 * 
	 * @return
	 */
	public String getPrintablePool(){
		StringBuffer sb = new StringBuffer();
		Set<ActivityType> types= pool.keySet();
		for(ActivityType type : types) {
			Integer count = pool.get(type);
			sb.append("\n").append(type.name()).append(",").append(type.code()).append(",").append(count);
		}
		
		return sb.toString();
	}
	
	/**
	 * 
	 */
	public void reset(){
		pool.clear();
		pool.putAll(originalPool);
	}
}
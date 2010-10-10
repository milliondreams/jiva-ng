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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author vipul
 *
 */
public class TimeSeries {

	/**
	 * 
	 * @author vipul
	 *
	 */
	private enum MODE{BEGIN,END};
	
	/**
	 * 
	 */
	Map<Integer,Map<MODE,List<Project.Activity>>> map = null;
	
	/**
	 * 
	 */
	ResourcePool resourcePool = null;
	
	/**
	 * 
	 */
	int numActivities = 0;
	
	/**
	 * 
	 */
	int earlyness = 0 ;
	/**
	 * 
	 */
	int tardiness = 0;
	/**
	 * 
	 */
	int clashes= 0;
	
	/**
	 * 
	 */
	List<Project> projects = null;
	/**
	 * 
	 */
	Integer[] delayList = null;
	
	
	/**
	 * 
	 * @param resourcePool
	 * @param list 
	 * @param projects 
	 */
	public TimeSeries(ResourcePool resourcePool, List<Project> projects, Integer[] delayList) {
		this.resourcePool = resourcePool;
		map = new TreeMap<Integer,Map<MODE,List<Project.Activity>>>();
		this.projects = projects;
		this.delayList = delayList;
		
	}
	
	/**
	 * 
	 * @param time
	 * @param activity
	 */
	private void addStartingActivity(Integer time, Project.Activity activity) {
		numActivities++;
		MODE mode = MODE.BEGIN;
		addActivity(time, activity, mode);
	}
	/**
	 * 
	 * @param time
	 * @param activity
	 */
	private void addEndingActivity(Integer time, Project.Activity activity) {
		MODE mode = MODE.END;
		addActivity(time, activity, mode);
	}

	/**
	 * @param time
	 * @param activity
	 * @param mode
	 */
	private void addActivity(Integer time, Project.Activity activity, MODE mode) {
		Map<MODE,List<Project.Activity>> timeMap = map.get(time);
		if(timeMap == null) {
			timeMap = new HashMap<MODE,List<Project.Activity>>();
			map.put(time, timeMap);
		}
		
		List<Project.Activity> activities = timeMap.get(mode);
		if(activities == null) {
			activities = new ArrayList<Project.Activity>();
			timeMap.put(mode, activities);
		}
		activities.add(activity);
	}
	
	/**
	 * 
	 */
	public void generate(){
		//absolute index in iArray
		int iArrayIndex = 0;


		//populate the time Series for all the projects
		for(Project project :projects) {
			//get all the activities for this project 
			List<Project.Activity> activities= project.getActivities();
			int projectActivityCount = activities.size();

			int beginTime = 0;
			int endTime = 0;//set the end time of last (phantom) activity to 0
			//loop through all the activities in the project and figure out their begin and end time
			for(int i = 0; i< projectActivityCount ;i++) {
				Project.Activity activity = activities.get(i);
				//add the delay value to the end time of last activity to calculate the begin time of this activity. 
				beginTime = endTime+delayList[iArrayIndex+i];
				// set the delay
				activity.setDelay(delayList[iArrayIndex+i]);
				//calculate the end time of the activity by adding the duration of the activity to the begin time
				endTime = beginTime+activity.getDuration();
				
				//set the current activity as an activity starting at the 'beginTime' in time series
				addStartingActivity(beginTime, activity);
				//set the current activity as an activity ending at the 'endTime' in time series
				addEndingActivity(endTime, activity);
				
			}
			
			//get the latest end time and compare with the end time .... here 'endTime' represnets the 
			// end time of the last activity and hence that of the project
			int latestEndTime = project.getLatestEndTime();
			//set the endtime for this schedule
			project.setScheduledEndTime(endTime);
			
			if(latestEndTime > endTime) {
				earlyness = earlyness + (latestEndTime - endTime);
			}else {
				tardiness = tardiness  + (endTime - latestEndTime);
			}
			
			
			iArrayIndex = iArrayIndex+projectActivityCount;
		}
		
		calculateClashes();
	}
	
	/**
	 * 
	 * @return
	 */
	private void calculateClashes() {
		
		
		Set<Integer> timeValues = map.keySet();

		for(Integer time : timeValues){
			//System.out.println(" TIME : "+time);
			Map<MODE,List<Project.Activity>> timeMap = map.get(time);
			
			List<Project.Activity> endingActivities = timeMap.get(MODE.END);
			if(endingActivities != null) {
				for(Project.Activity activity : endingActivities) {
					ActivityType type = activity.getType();
					//System.out.print("<< "+activity.getProject().getName()+" ");
					resourcePool.checkinResource(type);
					
				}
			}

			List<Project.Activity> beginingActivities = timeMap.get(MODE.BEGIN);
			if(beginingActivities  != null) {
				for(Project.Activity activity : beginingActivities ) {
					ActivityType type = activity.getType();
					
				//	System.out.print(">> "+activity.getProject().getName()+" ");
					boolean available = resourcePool.checkoutResource(type);
					if(!available) {
						activity.setConflicted(true);
						clashes++;
					}
					
				}
			}

		}

	}

	/**
	 * 
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(" Time Series for "+numActivities+" activities");
		Set timeValues = map.keySet();
		Iterator<Integer> i = timeValues.iterator();
		while(i.hasNext()) {
			Integer timeValue = i.next();
			Map<MODE,List<Project.Activity>> modes = map.get(timeValue);
			sb.append("\n").append("Time : ").append(timeValue);
			
			List<Project.Activity> eActivities= modes.get(MODE.END);
			if(eActivities != null && eActivities.size() > 0) {
				sb.append("\n\t").append("Ending Activities: ");
				for(Project.Activity activity : eActivities) {
					sb.append("\n\t\t").append(activity.toString());
				}
			}
			
			List<Project.Activity> bActivities= modes.get(MODE.BEGIN);
			if(bActivities != null && bActivities.size() > 0) {
				sb.append("\n\t").append("Begining Activities: ");
				for(Project.Activity activity : bActivities) {
					sb.append("\n\t\t").append(activity.toString());
				}
			}
			
			
			
		}
		
		
		return sb.toString();
		
	}
	
	
	/**
	 * 
	 */
	public void print(){
//		System.out.println("Resource Pool : ");
//		System.out.println(resourcePool);
		
		for(Project project : projects){
			int earlyness = project.getEarlyness();
			int totalTime = project.getLatestEndTime() - project.getEarlyness();
			String earlyDelayed = (earlyness > 0)?"early":"delayed";
			int by = (earlyness > 0)?project.getEarlyness():project.getTardiness();
			String status = earlyDelayed + " by "+by;
			
			System.out.print("\n"+project.getName()+"("+project.getLatestEndTime()+") - totalTime "+totalTime+" "+status+"\n\t\t|");
			for(Project.Activity activity : project.getActivities()){
				for(int i = 0 ; i < activity.getDelay(); i++)
					System.out.print("-");
				if(activity.isConflicted())
					System.out.print("X");
				else
					System.out.print("<");
				System.out.print(activity.getType().code().toLowerCase());
				for(int i = 0 ; i < activity.getDuration()-2; i++)
					System.out.print("=");
				System.out.print(">");
			}
			System.out.print("|");
		}
		
		
	}

	public int getEarlyness() {
		return earlyness;
	}

	public void setEarlyness(int earlyness) {
		this.earlyness = earlyness;
	}

	public int getTardiness() {
		return tardiness;
	}

	public void setTardiness(int tardiness) {
		this.tardiness = tardiness;
	}

	public void setClashes(int clashes) {
		this.clashes = clashes;
	}

	public int getClashes() {
		return clashes;
	}
	
	
	

}
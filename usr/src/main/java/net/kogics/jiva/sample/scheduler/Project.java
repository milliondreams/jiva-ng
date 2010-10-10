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
 */package net.kogics.jiva.sample.scheduler;

import java.util.List;

/**
 * @author vipul
 *
 */
public class Project {
	
	/**
	 * name of the project
	 */
	private String name = null;
 
	
	/**
	 * latest endtime of the project .... starting from time 0
	 */
	private int latestEndTime;
	
	/**
	 * list of activities
	 */
	private List<Activity> activities = null;
	
	
	/**
	 * 
	 */
	private int scheduledEndTime = -1;
	

	/**
	 * 
	 *
	 */
	public Project() {}


	/**
	 * @return the activities
	 */
	public List<Activity> getActivities() {
		return activities;
	}

	/**
	 * @param activities the activities to set
	 */
	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

 

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	

	/**
	 * 
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Project -> " +name);
		sb.append(" " +activities.size()+" activities ");
		sb.append(", Latest End time-> " +latestEndTime);
		
		return sb.toString();
	}
	
	/**
	 * 
	 * @author vipul
	 *
	 */
	public  final class Activity{
		/**
		 *  type of the activity
		 */
		ActivityType type = null;
		/**
		 * duration of the activity -  in hours
		 */
		int duration = 0;
		
		/**
		 * 
		 */
		int delay = 0;

		/**
		 * 
		 */
		boolean conflicted = false;
		
		
		public boolean isConflicted() {
			return conflicted;
		}

		public void setConflicted(boolean conflicted) {
			this.conflicted = conflicted;
		}

		/**
		 * 
		 * @param type
		 * @param duration
		 */
		public Activity(ActivityType type, int duration) {
			this.type = type;
			this.duration = duration;
		}

		/**
		 * @return the duration
		 */
		public int getDuration() {
			return duration;
		}

		/**
		 * @return the type
		 */
		public ActivityType getType() {
			return type;
		}
		
		/**
		 * 
		 * @return
		 */
		public Project getProject() {
			return Project.this;
		}
		
		/**
		 * 
		 */
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append(name).append(" -> ").append(type.name()).append(" Duration -> ").append(duration);
			return sb.toString();
			
		}

		public int getDelay() {
			return delay;
		}

		public void setDelay(int delay) {
			this.delay = delay;
		}

		/**
		 * 
		 */
		private void reset(){
			setConflicted(false);
			setDelay(0);
		}
		
		
	}

	/**
	 * @return the latestEndTime
	 */
	public int getLatestEndTime() {
		return latestEndTime;
	}


	/**
	 * @param latestEndTime the latestEndTime to set
	 */
	public void setLatestEndTime(int latestEndTime) {
		this.latestEndTime = latestEndTime;
	}

	/**
	 * 
	 * @return
	 */
	public int getScheduledEndTime() {
		return scheduledEndTime;
	}

	/**
	 * 
	 * @param scheduledEndTime
	 */
	public void setScheduledEndTime(int scheduledEndTime) {
		this.scheduledEndTime = scheduledEndTime;
	}

	/**
	 * 
	 * @return
	 */
	public String getPrintableProject() {
		StringBuffer sb = new StringBuffer();
		sb.append(name).append(",").append(latestEndTime);

		for(Activity activity : activities){
			sb.append(",").append(activity.getType().code()).append(",").append(activity.getDuration());
		}
		return sb.toString();
	}
	
	
	/**
	 * reset the project ...as if it is not scheduled yet 
	 */
	public void reset(){
		scheduledEndTime = -1;
		for(Activity activity : activities){
			activity.reset();
		}
	}

	/**
	 * 
	 * @return
	 */
	public int getTardiness() {
		return  scheduledEndTime - latestEndTime ;
	}

	/**
	 * 
	 * @return
	 */
	public int getEarlyness() {
		return latestEndTime - scheduledEndTime;
	}

}

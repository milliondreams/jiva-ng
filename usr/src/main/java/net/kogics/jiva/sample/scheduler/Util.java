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

import java.util.List;
import java.util.StringTokenizer;

/**
 * Utility class
 * 
 * @author vipul
 *
 */
public class Util {
	
	/**
	 * 
	 * @param projects Projects to be scheduled
	 * @param commaSeperatedDelays Delay vaules generate by GAs
	 */
	public static void scheduleProjects(List<Project> projects,String commaSeperatedDelays){
		StringTokenizer st = new StringTokenizer(commaSeperatedDelays,",");
		
		for(Project project : projects){
			List<Project.Activity> activities = project.getActivities();
			for(Project.Activity activity : activities){
				String delayStr = st.nextToken();
				int delay = Integer.parseInt(delayStr);
				activity.setDelay(delay);
			}
			
		}
	}
	
	/**
	 * 
	 * This method resets the projects  to non=delay shedules 
	 * 
	 * @param commaSeperatedDelays
	 */
	public static void resetSchedule(List<Project> projects){

		for(Project project : projects){
			List<Project.Activity> activities = project.getActivities();
			for(Project.Activity activity : activities){
				activity.setDelay(0);
				activity.setConflicted(false);
			}
			
		}

	}

	/**
	 * 
	 * @param resourcePool
	 * @param projects
	 * @param delays
	 */
	public static void printSchedule(ResourcePool resourcePool,
			List<Project> projects, Integer[] delays) {
		TimeSeries timeSeries = new TimeSeries(resourcePool,projects,delays);
		timeSeries.generate();
		System.out.println("Clashes "+timeSeries.getClashes());
		System.out.println("Earlyness "+timeSeries.getEarlyness());
		System.out.println("Tardiness "+timeSeries.getTardiness());
		timeSeries.print();
		
		for(Project project : projects){
			project.reset();
		}
	}

}

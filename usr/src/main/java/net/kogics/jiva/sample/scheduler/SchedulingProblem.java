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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author vipul
 *
 */
public class SchedulingProblem {
	// NON HARDCODED
	private static ResourcePool pool = null;
	private static List<Project> projects = null;
	
	private static AtomicBoolean init = new AtomicBoolean(false);
	
	/**
	 * get hardcoded resource pool... a helper method to avoid using 
	 * a config file.
	 * 
	 * @return
	 */
	private static ResourcePool getHardcodedResourcePool(){

		pool = new ResourcePool();
		pool.addResources(ActivityType.REQUIREMENTS_GATHERING,5);
		pool.addResources(ActivityType.ARCHITECTURE_DESIGN,3);
		pool.addResources(ActivityType.PROOF_OF_CONCEPT,5);
		pool.addResources(ActivityType.QA,3);
		pool.addResources(ActivityType.JAVA_DEVELOPMENT,3);
		pool.addResources(ActivityType.CPP_DEVELOPMENT,3);
		pool.addResources(ActivityType.DB_DEVELOPMENT,3);
		
		return pool;
	
	}
	
	/**
	 * get hardcoded projects... a helper method to avoid using 
	 * a config file.
	 * 
	 * @return
	 */
	private static List<Project> getHardcodedProjects(){

		 projects = new ArrayList<Project>();
		
		Project project1 = new Project();
		project1.setName("BugTrackingSystem");
		List<Project.Activity> activities = new ArrayList<Project.Activity>();
		activities.add(project1.new Activity(ActivityType.REQUIREMENTS_GATHERING,2));
		activities.add(project1.new Activity(ActivityType.ARCHITECTURE_DESIGN,5));
		activities.add(project1.new Activity(ActivityType.PROOF_OF_CONCEPT,10));
		activities.add(project1.new Activity(ActivityType.QA,2));
		activities.add(project1.new Activity(ActivityType.JAVA_DEVELOPMENT,15));
		activities.add(project1.new Activity(ActivityType.CPP_DEVELOPMENT,15));
		activities.add(project1.new Activity(ActivityType.DB_DEVELOPMENT,15));
		activities.add(project1.new Activity(ActivityType.QA,6));
		project1.setActivities(activities);
		project1.setLatestEndTime(95);
		projects.add(project1);
		
		Project project2 = new Project();
		project2.setName("HRPortal");
		activities = new ArrayList<Project.Activity>();
		activities.add(project2.new Activity(ActivityType.REQUIREMENTS_GATHERING,2));
		activities.add(project2.new Activity(ActivityType.ARCHITECTURE_DESIGN,4));
		activities.add(project2.new Activity(ActivityType.PROOF_OF_CONCEPT,20));
		activities.add(project2.new Activity(ActivityType.QA,3));
		activities.add(project2.new Activity(ActivityType.JAVA_DEVELOPMENT,25));
		activities.add(project2.new Activity(ActivityType.CPP_DEVELOPMENT,25));
		activities.add(project2.new Activity(ActivityType.QA,10));
		project2.setActivities(activities);
		project2.setLatestEndTime(105);
		projects.add(project2);
		
		
		
		Project project3 = new Project();
		project3.setName("PointOfSales");
		activities = new ArrayList<Project.Activity>();
		activities.add(project3.new Activity(ActivityType.REQUIREMENTS_GATHERING,3));
		activities.add(project3.new Activity(ActivityType.ARCHITECTURE_DESIGN,4));
		activities.add(project3.new Activity(ActivityType.PROOF_OF_CONCEPT,16));
		activities.add(project3.new Activity(ActivityType.QA,4));
		activities.add(project3.new Activity(ActivityType.CPP_DEVELOPMENT,25));
		activities.add(project3.new Activity(ActivityType.DB_DEVELOPMENT,25));
		activities.add(project3.new Activity(ActivityType.QA,5));
		project3.setActivities(activities);
		project3.setLatestEndTime(100);
		projects.add(project3);
		
		
		Project project4 = new Project();
		project4.setName("MusicStore");
		activities = new ArrayList<Project.Activity>();
		activities.add(project4.new Activity(ActivityType.REQUIREMENTS_GATHERING,3));
		activities.add(project4.new Activity(ActivityType.ARCHITECTURE_DESIGN,4));
		activities.add(project4.new Activity(ActivityType.PROOF_OF_CONCEPT,12));
		activities.add(project4.new Activity(ActivityType.QA,3));
		project4.setActivities(activities);
		project4.setLatestEndTime(30);
		projects.add(project4);
		

		
		
		Project project5 = new Project();
		project5.setName("BusinessActivityMonitoringSystem");
		activities = new ArrayList<Project.Activity>();
		activities.add(project5.new Activity(ActivityType.REQUIREMENTS_GATHERING,2));
		activities.add(project5.new Activity(ActivityType.ARCHITECTURE_DESIGN,4));
		activities.add(project5.new Activity(ActivityType.PROOF_OF_CONCEPT,20));
		activities.add(project5.new Activity(ActivityType.QA,3));
		activities.add(project5.new Activity(ActivityType.JAVA_DEVELOPMENT,25));
		activities.add(project5.new Activity(ActivityType.CPP_DEVELOPMENT,25));
		activities.add(project5.new Activity(ActivityType.QA,10));
		project5.setActivities(activities);
		project5.setLatestEndTime(105);
		projects.add(project5);
		
		
		
		Project project6 = new Project();
		project6.setName("ReportingTool");
		activities = new ArrayList<Project.Activity>();
		activities.add(project6.new Activity(ActivityType.REQUIREMENTS_GATHERING,3));
		activities.add(project6.new Activity(ActivityType.ARCHITECTURE_DESIGN,4));
		activities.add(project6.new Activity(ActivityType.PROOF_OF_CONCEPT,16));
		activities.add(project6.new Activity(ActivityType.QA,4));
		activities.add(project6.new Activity(ActivityType.CPP_DEVELOPMENT,25));
		activities.add(project6.new Activity(ActivityType.DB_DEVELOPMENT,25));
		activities.add(project6.new Activity(ActivityType.QA,5));
		project6.setActivities(activities);
		project6.setLatestEndTime(95);
		projects.add(project6);

		return projects;
			
	}
	
	
	
	/**
	 * 
	 * @param pool
	 * @param project
	 */
	public static void printProblem(ResourcePool rPool, List<Project> projects){
		String poolStr = rPool.getPrintablePool();
		System.out.println(poolStr);
		System.out.println();
		for(Project project : projects){
			String projectStr = project.getPrintableProject();
			System.out.println(projectStr);
		}
		
	}

	/**
	 * 
	 * @throws Exception
	 */
	public static void initHardcoded() throws Exception {
		if(init.get()){
			throw new Exception("Already initialized");
		}
		pool = getHardcodedResourcePool();
		projects = getHardcodedProjects();
		init.set(false);
	}

	/**
	 * Read the resource-pool and project details from a file
	 * the config file should be in the format as below 
	 * 
	
REQUIREMENTS_GATHERING,5  // i.e.(activity name and corresponding number of resources in the common pool)
QA,4
::
::
CPP_DEVELOPMENT,3
	//followed by a line feed and then the list of projects 
BugTrackingSystem,95
	REQUIREMENTS_GATHERING,2
	:
	:
	JAVA_DEVELOPMENT,15
	QA,6

HRPortal,105
	REQUIREMENTS_GATHERING,2
	:
	:
	JAVA_DEVELOPMENT,25
	CPP_DEVELOPMENT,25

	 * 
	 * 
	 * 
	 * @param string
	 * @throws Exception 
	 */
	public static void initFromfile(String filePath) throws Exception {
		if(init.get()){
			throw new Exception("Already initialized");
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
		String line = br.readLine();
		pool = new ResourcePool();
		projects = new ArrayList<Project>();
		//read resource pool
		while(line != null && !line.equalsIgnoreCase("")){
			StringTokenizer tokenizer = new StringTokenizer(line,",");
			String activityName = tokenizer.nextToken();
			String numOfResourcesStr = tokenizer.nextToken();

			Integer numOfResources = Integer.parseInt(numOfResourcesStr); 
			pool.addResources(ActivityType.valueOf(activityName), numOfResources);
			
			line = br.readLine();
		}
		
		line = br.readLine();

		while(line != null){
			boolean readNameDuration = false;
			Project project = new Project();
			List<Project.Activity>  activities = new ArrayList<Project.Activity>();
			while(line != null && !line.equalsIgnoreCase("")){

				StringTokenizer projectString = new StringTokenizer(line.trim(),",");
				String str = projectString.nextToken();
				String numStr = projectString.nextToken();
				Integer num = Integer.parseInt(numStr);
				
				if(!readNameDuration){
					project.setName(str);
					project.setLatestEndTime(num);
					readNameDuration = true;
				}else{
					activities.add(project.new Activity(ActivityType.valueOf(str),num));
				}
				line = br.readLine();
			}
			
			project.setActivities(activities);
			projects.add(project);
			
			line = br.readLine();
		}

		init.set(false);
	}

	public static ResourcePool getResourcePool() {
		return pool;
	}

	public static List<Project> getProjects() {
		return projects;
	}
	

}

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

/**
 * @author vipul
 *
 */
public enum ActivityType {

	REQUIREMENTS_GATHERING("RG"),
	ARCHITECTURE_DESIGN("AD"),
	PROOF_OF_CONCEPT("PC"),
	QA("QA"),
	JAVA_DEVELOPMENT("JD"),
	RUBY_DEVELOPMENT("RD"),
	CPP_DEVELOPMENT("CD"),
	DB_DEVELOPMENT	("DD");
	
	/**
	 * 
	 */
	private final String code;
	/**
	 * 
	 * @param code
	 */
	ActivityType(String code){
		this.code = code;
	}
	/**
	 * 
	 * @return
	 */
	public String code() {
		return code;
	}

	
}

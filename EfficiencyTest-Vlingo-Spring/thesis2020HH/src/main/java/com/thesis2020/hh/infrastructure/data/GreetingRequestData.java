/**
 * 
 */
package com.thesis2020.hh.infrastructure.data;

/**
 * @author hadydab
 *
 */
public class GreetingRequestData {
	
	public final String message;
	public final String description;
	
	public GreetingRequestData (String message,String description) {
		this.message = message;
		this.description = description;
	}
}

/**
 * 
 */
package codes.hady.vlingoHelloWorldInSpring.dtos;

import codes.hady.vlingoHelloWorldInSpring.entities.Greeting;

/**
 * @author hamzahassan
 *
 */
public class GreetingRequest {
	
	private String message;
	private String description;
	

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
	public Greeting getGreeting() {
		Greeting greeting = new Greeting();
		greeting.setMessage(this.message);
		greeting.setDescription(this.description);
		greeting.setMessageCounter(0);
		greeting.setDescriptionCounter(0);
		return greeting;
	}
	

}

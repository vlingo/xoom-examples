/**
 * 
 */
package codes.hady.vlingoHelloWorldInSpring.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.GenericGenerator;

/**
 * @author hamzahassan
 *
 */
@Entity
public class Greeting {
	
	
	@Id @GeneratedValue(generator = "greeting-uuid")
	@GenericGenerator(name = "greeting-uuid", strategy = "uuid2")
	private String id;
	private String message;
	private int messageCounter;
	private String description;
	private int descriptionCounter;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getMessageCounter() {
		return messageCounter;
	}
	public void setMessageCounter(int messageCounter) {
		this.messageCounter = messageCounter;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getDescriptionCounter() {
		return descriptionCounter;
	}
	public void setDescriptionCounter(int descriptionCounter) {
		this.descriptionCounter = descriptionCounter;
	}
	
	

}

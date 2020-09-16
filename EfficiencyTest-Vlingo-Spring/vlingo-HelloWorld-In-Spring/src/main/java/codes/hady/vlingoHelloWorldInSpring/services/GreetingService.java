/**
 * 
 */
package codes.hady.vlingoHelloWorldInSpring.services;

import codes.hady.vlingoHelloWorldInSpring.dtos.GreetingUpdateRequest;
import codes.hady.vlingoHelloWorldInSpring.entities.Greeting;

/**
 * @author hamzahassan
 *
 */
public interface GreetingService {
	
	Greeting changeMessage(String greetingId,GreetingUpdateRequest request);
	Greeting changeDescription(String greetingId,GreetingUpdateRequest request);

}

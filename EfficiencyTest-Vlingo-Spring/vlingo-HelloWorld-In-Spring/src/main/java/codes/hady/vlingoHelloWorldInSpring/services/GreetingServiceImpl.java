/**
 * 
 */
package codes.hady.vlingoHelloWorldInSpring.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import codes.hady.vlingoHelloWorldInSpring.dtos.GreetingUpdateRequest;
import codes.hady.vlingoHelloWorldInSpring.entities.Greeting;
import codes.hady.vlingoHelloWorldInSpring.repositories.GreetingRepository;

/**
 * @author hamzahassan
 *
 */
@Service
public class GreetingServiceImpl implements GreetingService{
	
	
	
	@Autowired
	private GreetingRepository greetingRepository;
	
	
	@Override
	public Greeting changeMessage(String greetingId,GreetingUpdateRequest request) {	
		Optional<Greeting> optional = greetingRepository.findById(greetingId);
		if(optional.isPresent()) {
			Greeting greeting  = optional.get();
			greeting.setMessage(request.getValue());
			greeting.setMessageCounter(greeting.getMessageCounter() + 1);
			return greetingRepository.save(greeting);
		}else {
			return null;
		}
	}

	@Override
	public Greeting changeDescription(String greetingId,GreetingUpdateRequest request) {
		Optional<Greeting> optional = greetingRepository.findById(greetingId);
		if(optional.isPresent()) {
			Greeting greeting  = optional.get();
			greeting.setDescription(request.getValue());
			greeting. setDescriptionCounter(greeting.getDescriptionCounter() + 1);
			return greetingRepository.save(greeting);
		}else {
			return null;
		}
	}

}

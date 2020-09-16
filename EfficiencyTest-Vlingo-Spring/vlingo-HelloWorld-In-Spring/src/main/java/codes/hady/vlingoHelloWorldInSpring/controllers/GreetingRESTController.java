/**
 * 
 */
package codes.hady.vlingoHelloWorldInSpring.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import codes.hady.vlingoHelloWorldInSpring.dtos.GreetingRequest;
import codes.hady.vlingoHelloWorldInSpring.dtos.GreetingUpdateRequest;
import codes.hady.vlingoHelloWorldInSpring.entities.Greeting;
import codes.hady.vlingoHelloWorldInSpring.repositories.GreetingRepository;
import codes.hady.vlingoHelloWorldInSpring.services.GreetingService;

/**
 * @author hamzahassan
 *
 */
@RestController
@RequestMapping("/greetings")
public class GreetingRESTController {

	
	private GreetingRepository greetingRepository;
	private GreetingService greetingService;

	@Autowired
	public GreetingRESTController(GreetingRepository greetingRepository, GreetingService greetingService){
		this.greetingRepository = greetingRepository;
		this.greetingService = greetingService;
	}
	
	@GetMapping
	public List<Greeting> getGreetings(){
		return greetingRepository.findAll();
	}
	
	@PostMapping
	public Greeting createNewGreeting(@RequestBody GreetingRequest request) {
		return greetingRepository.save(request.getGreeting());
	}

	@GetMapping("/{id}")
	public Greeting getGreetingWithID(@PathVariable("id") String id){
		return greetingRepository.findById(id).get();
	}

	@PatchMapping("/{id}/message")
	public Greeting updateGreetingMessage(@RequestBody GreetingUpdateRequest request, @PathVariable("id") String id) {
		return greetingService.changeMessage(id, request);
	}
	@PatchMapping("/{id}/description")
	public Greeting updateGreetingDescription(@RequestBody GreetingUpdateRequest request,@PathVariable("id") String id) {
		return greetingService.changeDescription(id,request);
	}
	
	
	
	
	
}

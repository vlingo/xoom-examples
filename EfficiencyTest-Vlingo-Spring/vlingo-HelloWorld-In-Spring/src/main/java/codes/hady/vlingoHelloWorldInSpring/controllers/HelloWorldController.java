/**
 * 
 */
package codes.hady.vlingoHelloWorldInSpring.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hamzahassan
 *
 */
@RestController
public class HelloWorldController {
	
	@GetMapping("/hello")
	public String hello() {
		return "Hello World!";
	}
	
	
	@GetMapping("/hello/{whom}")
	public String helloWhom(@PathVariable String whom) {
		String hello = "Hello "+ whom + "!";
		return hello;
	}

}

/**
 * 
 */
package codes.hady.vlingoHelloWorldInSpring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import codes.hady.vlingoHelloWorldInSpring.entities.Greeting;

/**
 * @author hamzahassan
 *
 */
public interface GreetingRepository extends JpaRepository<Greeting, String>{
	
}

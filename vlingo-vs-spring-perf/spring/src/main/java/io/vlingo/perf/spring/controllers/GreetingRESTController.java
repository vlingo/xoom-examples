// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.perf.spring.controllers;

import java.util.List;

import io.vlingo.perf.spring.dtos.GreetingRequest;
import io.vlingo.perf.spring.dtos.GreetingUpdateRequest;
import io.vlingo.perf.spring.entities.Greeting;
import io.vlingo.perf.spring.repositories.GreetingRepository;
import io.vlingo.perf.spring.services.GreetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
		return greetingService.changeDescription(id, request);
	}
}

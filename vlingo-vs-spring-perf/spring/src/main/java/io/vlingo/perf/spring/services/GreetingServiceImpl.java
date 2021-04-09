// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.perf.spring.services;

import java.util.Optional;

import io.vlingo.perf.spring.dtos.GreetingUpdateRequest;
import io.vlingo.perf.spring.entities.Greeting;
import io.vlingo.perf.spring.repositories.GreetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GreetingServiceImpl implements GreetingService{

	@Autowired
	private GreetingRepository greetingRepository;

	@Override
	public Greeting changeMessage(String greetingId, GreetingUpdateRequest request) {
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

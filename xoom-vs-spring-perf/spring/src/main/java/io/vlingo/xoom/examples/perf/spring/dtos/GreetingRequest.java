// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.perf.spring.dtos;

import io.vlingo.xoom.examples.perf.spring.entities.Greeting;

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

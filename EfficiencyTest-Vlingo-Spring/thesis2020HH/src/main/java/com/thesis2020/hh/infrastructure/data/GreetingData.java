package com.thesis2020.hh.infrastructure.data;

import com.thesis2020.hh.model.greeting.GreetingState;

@SuppressWarnings("unused")
public class GreetingData {

	public final String id;
	public final String message;
	public final String description;
	public final int messageChangedCount;
	public final int descriptionChangedCount;

	
	public static GreetingData Empty = new GreetingData("", "", 0, "", 0);

	public GreetingData(String id, String message, int messageChangedCount, String description,
			int descriptionChangedCount) {
		this.id = id;
		this.message = message;
		this.messageChangedCount = messageChangedCount;
		this.description = description;
		this.descriptionChangedCount = descriptionChangedCount;
	}

	public static GreetingData from(GreetingState state) {
		return new GreetingData(state.id, state.message, state.messageChangedCount, state.description,
				state.descriptionChangedCount);
	}
	
	public static GreetingData from(String id, String message, int messageChangedCount, String description,
			int descriptionChangedCount) {
		return new GreetingData(id, message, messageChangedCount, description,descriptionChangedCount);
	}

}

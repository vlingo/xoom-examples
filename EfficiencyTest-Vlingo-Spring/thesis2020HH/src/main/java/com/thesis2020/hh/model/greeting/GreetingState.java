package com.thesis2020.hh.model.greeting;


import com.thesis2020.hh.infrastructure.data.GreetingRequestData;
import com.thesis2020.hh.infrastructure.data.GreetingChangeRequestData;

import io.vlingo.symbio.store.object.StateObject;

public final class GreetingState extends StateObject {

	private static final long serialVersionUID = -1759618299391550537L;
	public final String id;
	public final String message;
	public final String description;
	public final int messageChangedCount;
	public final int descriptionChangedCount;

	public static GreetingState empty() {
		return new GreetingState("", "", 0, "", 0);
	}

	public static GreetingState identifiedBy(final String id) {
		return new GreetingState(id);
	}

	public boolean doesNotExist() {
		return id == null;
	}

	public boolean isIdentifiedOnly() {
		return id != null && message == null && description == null;
	}

	
	public static GreetingState defineNewGreeting(final String id,GreetingRequestData data) {
		return new GreetingState(id, data.message, 0, data.description, 0);
	}
	

	public GreetingState changeMessage(final GreetingChangeRequestData data) {
		return new GreetingState(this.id, data.value, this.messageChangedCount + 1, this.description,
				this.descriptionChangedCount);
	}

	public GreetingState changeDescription(final GreetingChangeRequestData data) {
		return new GreetingState(this.id, this.message, this.messageChangedCount, data.value, this.descriptionChangedCount + 1);
	}

	GreetingState(final long persistenceId, final long version, final String id, final String message,
			final int messageCounter, final String description, final int descriptionCounter) {
		super(persistenceId, version);
		this.id = id;
		this.message = message;
		this.messageChangedCount = messageCounter;
		this.description = description;
		this.descriptionChangedCount = descriptionCounter;
	}

	GreetingState(final String id, final String message, final int messageCounter, final String description,
			final int descriptionCounter) {
		this(Unidentified, 0, id, message, messageCounter, description, descriptionCounter);
	}

	private GreetingState(final String id) {
		this(Unidentified, 0, id, null, 0, null, 0);
	}

	@SuppressWarnings("unused")
	private GreetingState() {
		this(Unidentified, 0, null, null, 0, null, 0);
	}
}

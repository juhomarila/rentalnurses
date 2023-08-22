package com.rental.nursing.exception;

import java.util.List;

public class UpdateDataException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private final List<String> messages;

	public UpdateDataException(List<String> messages) {
		super("Data update failed: " + messages.toString());
		this.messages = messages;
	}

	public List<String> getMessages() {
		return messages;
	}

}

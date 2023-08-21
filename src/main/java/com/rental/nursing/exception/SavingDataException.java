package com.rental.nursing.exception;

import java.util.List;

public class SavingDataException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final List<String> messages;

	public SavingDataException(List<String> messages) {
		super("Data saving failed: " + messages.toString());
		this.messages = messages;
	}

	public List<String> getMessages() {
		return messages;
	}
}

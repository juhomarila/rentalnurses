package com.rental.nursing.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NurseLogger {

	private String getClassName() {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		if (stackTrace.length > 3) {
			return stackTrace[3].getClassName();
		}
		return NurseLogger.class.getName();
	}

	public void getLogStart(String requestDescription) {
		String className = getClassName();
		Logger logger = LoggerFactory.getLogger(className);
		logger.info("GET start: {}", requestDescription);
	}

	public void getLogEnd(String requestDescription) {
		String className = getClassName();
		Logger logger = LoggerFactory.getLogger(className);
		logger.info("GET end: {}", requestDescription);
	}

	public void postLogStart(String requestDescription) {
		String className = getClassName();
		Logger logger = LoggerFactory.getLogger(className);
		logger.info("POST start: {}", requestDescription);
	}

	public void postLogEnd(String requestDescription) {
		String className = getClassName();
		Logger logger = LoggerFactory.getLogger(className);
		logger.info("POST end: {}", requestDescription);
	}

	public void putLogStart(String requestDescription) {
		String className = getClassName();
		Logger logger = LoggerFactory.getLogger(className);
		logger.info("PUT start: {}", requestDescription);
	}

	public void putLogEnd(String requestDescription) {
		String className = getClassName();
		Logger logger = LoggerFactory.getLogger(className);
		logger.info("PUT end: {}", requestDescription);
	}

	public void deleteLogStart(String requestDescription) {
		String className = getClassName();
		Logger logger = LoggerFactory.getLogger(className);
		logger.info("DELETE start: {}", requestDescription);
	}

	public void deleteLogEnd(String requestDescription) {
		String className = getClassName();
		Logger logger = LoggerFactory.getLogger(className);
		logger.info("DELETE end: {}", requestDescription);
	}

	public void logValidationFailure(String errorMsg) {
		String className = getClassName();
		Logger logger = LoggerFactory.getLogger(className);
		logger.error("Validation failure: {}", errorMsg);
	}

	public void logValidationAndIdFailure(String errorMsg, String entityId) {
		String className = getClassName();
		Logger logger = LoggerFactory.getLogger(className);
		logger.error("Validation failure: {}", errorMsg);
		logger.error("Entity ID: {}", entityId);
	}

	public void logEntityNotFound(String entityType, Long entityId) {
		String className = getClassName();
		Logger logger = LoggerFactory.getLogger(className);
		logger.error("Entity not found: {} with ID: {}", entityType, entityId);
	}

	public void logUnexpectedError(String errorMsg) {
		String className = getClassName();
		Logger logger = LoggerFactory.getLogger(className);
		logger.error("Unexpected error: {}", errorMsg);
	}

	public void logForbidden(String errorMsg) {
		String className = getClassName();
		Logger logger = LoggerFactory.getLogger(className);
		logger.error("Forbidden: {}", errorMsg);
	}

	public void logError(String msg) {
		String className = getClassName();
		Logger logger = LoggerFactory.getLogger(className);
		logger.error(msg);
	}
}

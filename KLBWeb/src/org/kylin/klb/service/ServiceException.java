package org.kylin.klb.service;

public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = 3583566093089790852L;

	public ServiceException() {
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}

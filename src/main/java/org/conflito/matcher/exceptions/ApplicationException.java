package org.conflito.matcher.exceptions;

public class ApplicationException extends Exception {

	private static final long serialVersionUID = -7851331616545234386L;

	public ApplicationException(String message) {
		super(message);
	}
	
	public ApplicationException(String message, Exception e) {
		super(message, e);
	}
}

package business.event.exceptions;

import facade.exceptions.ApplicationException;

public class NoEventFoundException extends ApplicationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9103990006871330619L;

	public NoEventFoundException(String message) {
		super(message);

	}

	public NoEventFoundException(String message, Exception e) {
		super(message, e);

	}

}

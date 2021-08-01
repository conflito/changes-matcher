package business.event.exceptions;

import facade.exceptions.ApplicationException;

public class NameNotUniqueException extends ApplicationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -859466521947400621L;

	public NameNotUniqueException(String message) {
		super(message);

	}

	public NameNotUniqueException(String message, Exception e) {
		super(message, e);

	}

}

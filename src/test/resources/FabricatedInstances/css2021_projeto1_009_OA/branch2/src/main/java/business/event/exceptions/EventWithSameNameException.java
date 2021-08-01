package business.event.exceptions;

import facade.exceptions.ApplicationException;

public class EventWithSameNameException extends ApplicationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3482496288684550855L;

	public EventWithSameNameException(String message) {
		super(message);

	}

	public EventWithSameNameException(String message, Exception e) {
		super(message, e);

	}

}

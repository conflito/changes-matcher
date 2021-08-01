package business.event.exceptions;

import facade.exceptions.ApplicationException;

public class SeatedVenueLargerThanEventException extends ApplicationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7648042519336293136L;

	public SeatedVenueLargerThanEventException(String message) {
		super(message);

	}

	public SeatedVenueLargerThanEventException(String message, Exception e) {
		super(message, e);

	}

}

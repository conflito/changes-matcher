/**
 * 
 */
package business.event.exceptions;

import facade.exceptions.ApplicationException;

public class EventIsNotSeatedException extends ApplicationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2538263313044290563L;

	/**
	 * @param message
	 */
	public EventIsNotSeatedException(String message) {
		super(message);

	}

	/**
	 * @param message
	 * @param e
	 */
	public EventIsNotSeatedException(String message, Exception e) {
		super(message, e);
	}

}

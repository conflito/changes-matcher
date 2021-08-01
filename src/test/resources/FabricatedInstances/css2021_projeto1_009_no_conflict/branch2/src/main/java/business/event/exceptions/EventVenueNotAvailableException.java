/**
 * 
 */
package business.event.exceptions;

import facade.exceptions.ApplicationException;

/**
 *
 */
public class EventVenueNotAvailableException extends ApplicationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4871460117291846046L;

	/**
	 * @param message
	 */
	public EventVenueNotAvailableException(String message) {
		super(message);

	}

	/**
	 * @param message
	 * @param e
	 */
	public EventVenueNotAvailableException(String message, Exception e) {
		super(message, e);

	}

}

/**
 * 
 */
package business.ticket.exceptions;

import facade.exceptions.ApplicationException;

/**
 *
 */
public class TicketNotAvailableException extends ApplicationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -864891017317738493L;

	/**
	 * @param message
	 */
	public TicketNotAvailableException(String message) {
		super(message);

	}

	/**
	 * @param message
	 * @param e
	 */
	public TicketNotAvailableException(String message, Exception e) {
		super(message, e);

	}

}

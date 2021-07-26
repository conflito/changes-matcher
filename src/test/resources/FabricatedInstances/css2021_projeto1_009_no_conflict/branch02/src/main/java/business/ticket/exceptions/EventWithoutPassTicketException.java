package business.ticket.exceptions;

import facade.exceptions.ApplicationException;

public class EventWithoutPassTicketException extends ApplicationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8444963183378552132L;

	public EventWithoutPassTicketException(String message) {
		super(message);
	}

	public EventWithoutPassTicketException(String message, Exception e) {
		super(message, e);
	}

}

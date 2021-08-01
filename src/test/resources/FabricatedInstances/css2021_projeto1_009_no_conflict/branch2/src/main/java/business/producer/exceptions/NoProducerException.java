package business.producer.exceptions;

import facade.exceptions.ApplicationException;

public class NoProducerException extends ApplicationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1490655168892203130L;

	public NoProducerException(String message) {
		super(message);
	}
	
	public NoProducerException(String message,Exception e) {
		super(message,e);
	}

}

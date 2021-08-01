package business.event.exceptions;


public class DateInvalidException extends Exception {


	/**
	 * 
	 */
	private static final long serialVersionUID = -8911037324703952843L;
	
	public DateInvalidException(String message, Exception e) {
		super(message, e);
	}
	public DateInvalidException(String message) {
		super(message);
	}

}

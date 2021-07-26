package facade.exceptions;

public class InvalidEventTypeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates an exception given an error message
	 * 
	 * @param message The error message
	 */
	public InvalidEventTypeException() {
		super ("O evento nao tem lugares marcados.");
	}

}

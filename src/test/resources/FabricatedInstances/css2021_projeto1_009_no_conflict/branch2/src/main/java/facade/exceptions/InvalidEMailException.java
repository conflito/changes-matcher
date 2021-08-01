/**
 * 
 */
package facade.exceptions;

/**
 *
 */
public class InvalidEMailException extends ApplicationException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2463137880645324920L;


	/**
	 * Creates an exception given an error message
	 * 
	 * @param message The error message
	 */
	public InvalidEMailException(String eMail) {
		super (eMail);
	}
	
	
	/**
	 * Creates an exception wrapping a lower level exception.
	 * 
	 * @param message The error message
	 * @param e The wrapped exception.
	 */
	public InvalidEMailException(String eMail, Exception e) {
		super (eMail, e);
	}
}

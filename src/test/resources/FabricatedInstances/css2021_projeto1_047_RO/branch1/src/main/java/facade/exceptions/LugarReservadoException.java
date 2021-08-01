package facade.exceptions;

public class LugarReservadoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates an exception given an error message
	 * 
	 * @param message The error message
	 */
	public LugarReservadoException(String lugar) {
		super ("O lugar "+ lugar +" nao esta mais disponivel.");
	}

}

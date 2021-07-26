package facade.exceptions;

public class EventoNaoExisteException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7836333848359749455L;

	public EventoNaoExisteException(String string) {
		super("O evento com nome " + string + " nao existe.");
	}

}

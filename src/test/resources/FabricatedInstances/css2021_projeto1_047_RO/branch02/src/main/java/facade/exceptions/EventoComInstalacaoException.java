package facade.exceptions;

public class EventoComInstalacaoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4319794410137677436L;

	public EventoComInstalacaoException(String string) {
		super("O evento " + string + " ja tem uma instalacao atribuida.");
	}

}

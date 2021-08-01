package facade.exceptions;

public class BilhetePasseNaoDisponivelException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BilhetePasseNaoDisponivelException() {
		super("Nao existem bilhetes passe disponiveis.");
	}

}

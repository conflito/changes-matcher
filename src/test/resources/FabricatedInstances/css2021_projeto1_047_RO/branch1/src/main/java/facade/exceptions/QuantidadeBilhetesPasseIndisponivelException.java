package facade.exceptions;

public class QuantidadeBilhetesPasseIndisponivelException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public QuantidadeBilhetesPasseIndisponivelException() {
		super("O evento nao tem bilhetes-passe suficientes disponiveis");
	}

}

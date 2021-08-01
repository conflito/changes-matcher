package facade.exceptions;

public class QuantidadeInferioraUmException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public QuantidadeInferioraUmException() {
		super("Eh preciso pelo menos um bilhete-passe.");
	}

}

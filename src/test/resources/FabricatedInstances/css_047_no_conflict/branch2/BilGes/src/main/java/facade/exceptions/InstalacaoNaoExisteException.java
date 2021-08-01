package facade.exceptions;

public class InstalacaoNaoExisteException extends Exception {

	private static final long serialVersionUID = 8484711058239613710L;

	public InstalacaoNaoExisteException(String string) {
		super("Instalacao com nome " + string + " não existe!");
	}

}

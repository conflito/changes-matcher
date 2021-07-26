package facade.exceptions;

public class PrecoInvalidoException extends Exception {

	private static final long serialVersionUID = 4672290399071447684L;

	public PrecoInvalidoException(String preco) {
		super( preco + " - nao eh um numero valido");
	}

}

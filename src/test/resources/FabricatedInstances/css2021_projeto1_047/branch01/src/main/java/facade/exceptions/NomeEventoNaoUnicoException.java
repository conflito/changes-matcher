package facade.exceptions;

public class NomeEventoNaoUnicoException extends Exception {
	
	private static final long serialVersionUID = 7524303999559927896L;

	public NomeEventoNaoUnicoException(String nome) {
		super("O nome de evento " + nome + " ja esta a ser utilizado.");
	}
	
}

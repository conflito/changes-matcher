package facade.exceptions;

public class TipoEventoInvalidoException extends Exception {

	private static final long serialVersionUID = -6004706053355791142L;

	public TipoEventoInvalidoException(String nomeTipo) {
		super("O tipo de evento " + nomeTipo + " nao existe.");
	}
	
}

package facade.exceptions;

public class NaoHaTiposEventoException extends Exception {

	private static final long serialVersionUID = -1502436753657717524L;

	public NaoHaTiposEventoException() {
		super("Nao existem tipos de evento.");
	}
}

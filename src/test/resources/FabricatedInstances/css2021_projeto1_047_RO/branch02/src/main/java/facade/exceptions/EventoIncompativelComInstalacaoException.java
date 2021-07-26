package facade.exceptions;

public class EventoIncompativelComInstalacaoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5683858699279624414L;

	public EventoIncompativelComInstalacaoException(String nomeEvento) {
		super("Evento " + nomeEvento + " nao eh compativel com o tipo de instalacao.");
	}

}

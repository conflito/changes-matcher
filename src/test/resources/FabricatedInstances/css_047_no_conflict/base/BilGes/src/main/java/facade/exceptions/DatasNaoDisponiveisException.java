package facade.exceptions;

public class DatasNaoDisponiveisException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5953424766531947003L;

	public DatasNaoDisponiveisException() {
		super("Evento sem datas disponiveis.");
	}

}

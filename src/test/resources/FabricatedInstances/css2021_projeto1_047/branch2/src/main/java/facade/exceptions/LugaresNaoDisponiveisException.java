package facade.exceptions;

public class LugaresNaoDisponiveisException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5687675953732928346L;
	
	public LugaresNaoDisponiveisException() {
		super("Nao existem lugares disponiveis.");
	}

}

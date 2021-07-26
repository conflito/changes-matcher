package facade.exceptions;

public class MaisLugaresQueCapacidadeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6209743334134760795L;

	public MaisLugaresQueCapacidadeException() {
		super("A capacidade da instalação, de lugares sentados, é superior ao número máximo de participantes que o evento pode ter");
	}
}

package facade.exceptions;

public class MaisLugaresQueCapacidadeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6209743334134760795L;

	public MaisLugaresQueCapacidadeException() {
		super("A capacidade da instala��o, de lugares sentados, � superior ao n�mero m�ximo de participantes que o evento pode ter");
	}
}

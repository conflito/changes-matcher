package facade.exceptions;

public class PromotoraNaoExisteException extends Exception {

	private static final long serialVersionUID = 5608397918915800753L;

	public PromotoraNaoExisteException(int id) {
		super("A promotora com o id " + id + " nao esta registada no sistema.");
	}
	
}

package facade.exceptions;

public class PromotoraNaoCertificadaException extends Exception {

	private static final long serialVersionUID = 113622476755014552L;
	
	public PromotoraNaoCertificadaException(String tipoEvento, int id) {
		super("A promotora com id " + id + " nao esta certificada para o tipo de evento " + tipoEvento + ".");
	}

}

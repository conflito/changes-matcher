package facade.exceptions;

public class InstalacaoNaoExisteDatasException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5416683851034642820L;

	public InstalacaoNaoExisteDatasException() {
		super("Não existem instalacoes disponiveis nessas datas!");
	}

}

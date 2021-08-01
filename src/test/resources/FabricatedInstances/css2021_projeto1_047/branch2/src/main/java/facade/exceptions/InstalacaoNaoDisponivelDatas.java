package facade.exceptions;

import java.util.Date;

public class InstalacaoNaoDisponivelDatas extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8495406745386766646L;

	public InstalacaoNaoDisponivelDatas(String string, Date date, Date date2) {
		super(string + " nao esta disponivel entre as datas " + date.toString() + " e " + date2.toString());
	}

}

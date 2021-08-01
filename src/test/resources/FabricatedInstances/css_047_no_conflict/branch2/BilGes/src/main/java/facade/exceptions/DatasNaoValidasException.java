package facade.exceptions;

public class DatasNaoValidasException extends Exception {

	private static final long serialVersionUID = 5196205284424205103L;

	public DatasNaoValidasException() {
		super("Os pares data e horas de inicio e fim escolhidos nao sao validos.");
	}
	
}

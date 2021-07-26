package facade.exceptions;

public class AGApplicationException extends Exception{

	private static final long serialVersionUID = 5508181497404652801L;

	public AGApplicationException(String errorMessage, Exception e) {
		super(errorMessage, e);
	}

}

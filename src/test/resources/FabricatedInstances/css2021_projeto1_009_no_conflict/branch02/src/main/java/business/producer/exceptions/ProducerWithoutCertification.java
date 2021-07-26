package business.producer.exceptions;

import facade.exceptions.ApplicationException;

public class ProducerWithoutCertification extends ApplicationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7797707787953917163L;

	public ProducerWithoutCertification(String message) {
		super(message);

	}

	public ProducerWithoutCertification(String message, Exception e) {
		super(message, e);

	}

}

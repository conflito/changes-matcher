package business.catalogs;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import business.entities.EventType;
import business.entities.Producer;
import facade.exceptions.ApplicationException;

public class ProducerCatalog {
	
	private EntityManager em;
	
	/**
	 * Constructs a Producer's catalog given a entity manager factory
	 */
	public ProducerCatalog(EntityManager em) {
		this.em = em;
	}

	
	public Producer getProducer (int registrationNumber) throws ApplicationException {
		TypedQuery<Producer> query = em.createNamedQuery(Producer.FIND_BY_REGNUM, Producer.class);
		query.setParameter(Producer.INT_REGNUM, registrationNumber);
		try {
			return query.getSingleResult();
		} catch (PersistenceException e) {
			throw new ApplicationException("EventType not found.", e);
		}
	}

}

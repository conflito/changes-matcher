package business.catalogs;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import business.entities.EventType;
import facade.exceptions.ApplicationException;

public class EventTypeCatalog {
	
	private EntityManager em;
	
	/**
	 * Constructs a EventType's catalog given a entity manager factory
	 */
	public EventTypeCatalog(EntityManager em) {
		this.em = em;
	}

	
	public EventType getEventType (String name) throws ApplicationException {
		TypedQuery<EventType> query = em.createNamedQuery(EventType.FIND_BY_DESIGNATION, EventType.class);
		query.setParameter(EventType.STRING_DESIGNATION, name);
		try {
			return query.getSingleResult();
		} catch (PersistenceException e) {
			throw new ApplicationException("EventType not found.", e);
		}
	}

}

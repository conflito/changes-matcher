package business.catalogs;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import business.entities.Event;
import business.entities.EventType;
import business.entities.Manager;
import business.entities.Producer;
import facade.exceptions.ApplicationException;

public class EventCatalog {
	
	private EntityManager em;
	
	/**
	 * Constructs a Event's catalog given a entity manager factory
	 */
	public EventCatalog(EntityManager em) {
		this.em = em;
	}

	
	public Event getEvent (String name) throws ApplicationException {
		TypedQuery<Event> query = em.createNamedQuery(Event.FIND_BY_NAME, Event.class);
		query.setParameter(Event.STRING_NAME, name);
		try {
			return query.getSingleResult();
		} catch (PersistenceException e) {
			throw new ApplicationException("Event not found.", e);
		}
	}

	
	public void addEvent (Manager manager, Producer producer, EventType eventType, List<List<Date>> eventDates) {
		Event e = new Event(manager, producer, eventType, eventDates);
		em.persist(e);
	}
}

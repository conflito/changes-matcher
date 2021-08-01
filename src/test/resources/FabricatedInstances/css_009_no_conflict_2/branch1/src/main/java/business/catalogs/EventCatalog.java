package business.catalogs;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import business.entities.Event;
import business.entities.EventDate;
import business.entities.EventType;
import business.entities.Manager;
import business.entities.Producer;
import business.entities.Seat;
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
	
	
	public List<EventDate> getDatesWithAvailableTicketsForEvent(String eventName, Date fromDate) throws ApplicationException {
		TypedQuery<EventDate> query = em.createNamedQuery(Event.DATES_WITH_AVAILABLE_TICKETS, EventDate.class);
		query.setParameter(Event.STRING_NAME, eventName);
		query.setParameter(Event.DATE_DATE, fromDate);
		try {
			return query.getResultList();
		} catch (Exception e) {
			throw new ApplicationException("Error retrieving available event dates.", e);
		}
	}
	
	public List<Seat> getAvailableSeatsForEventDate(String eventName, Date date) throws ApplicationException {
		TypedQuery<Seat> query = em.createNamedQuery(Event.AVAILABLE_SEATS_FOR_EVENT_DATE, Seat.class);
		query.setParameter(Event.STRING_NAME, eventName);
		query.setParameter(Event.DATE_DATE, date);
		try {
			return query.getResultList();
		} catch (Exception e) {
			throw new ApplicationException("Error retrieving available seats for event date.", e);
		}
	}
	
}


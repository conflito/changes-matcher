package business.event;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import business.util.Triplet;

import business.event.exceptions.NoEventFoundException;
import business.producer.Producer;
import business.ticket.PassTicket;
import business.ticket.Ticket;
import business.ticket.TicketState;
import business.ticket.exceptions.EventWithoutPassTicketException;
import facade.exceptions.ApplicationException;

/**
 * 
 *
 * A catalog of Events
 */

public class EventCatalog {

	private EntityManager em;

	/**
	 * Constructs a Event's catalog given a entity manager factory
	 */
	public EventCatalog(EntityManager em) {
		this.em = em;
	}

	/**
	 * Gets the event from the database
	 * @param name of the Event
	 * @return the event
	 * @throws ApplicationException if the Event is not found
	 */
	public Event getEvent(String name) throws ApplicationException {
		TypedQuery<Event> query = em.createNamedQuery(Event.FIND_BY_NAME, Event.class);
		query.setParameter(Event.STRING_NAME, name);
		try {
			return query.getSingleResult();
		} catch (PersistenceException e) {
			throw new ApplicationException("Event not found.", e);
		}
	}

	/**
	 * Adds the Event in the database
	 * @param producer the name of the producer
	 * @param eventType the type of the Event
	 * @param nameEvent name of the Event
	 * @param eventDates the dates of the Event
	 */
	public void addEvent(Producer producer, String eventType,String nameEvent
			,List<Triplet<LocalDate, LocalTime, LocalTime>> eventDates) {

		Event e = new Event(producer,eventType,nameEvent, eventDates);
		em.persist(e);
	}

	/**
	 * Checks if an Event with the given name exists.
	 * @param name of the event
	 * @throws ApplicationException if the event exists.
	 * @see checkExistentEvent(), it does the opposite of this method.
	 */
	public void checkEvent(String name) throws ApplicationException {
		TypedQuery<String> query = em.createNamedQuery(Event.FIND_BY_NAME, String.class);
		query.setParameter(Event.STRING_NAME, name);
		try {
			if (!query.getResultList().isEmpty()) {
				throw new ApplicationException("Event " + name + " exists.");
			}
		} catch (PersistenceException e) {
			throw new ApplicationException("Error checking if the Event exists .", e);
		}
	}
	
	/**
	 * Gets the types that exist on the database
	 * @return all the types on the database
	 */
	public List<String> getEventsTypes() {
		TypedQuery<String> query = em.createNamedQuery(Producer.GET_EVENT_TYPES_CERTIFICATION, String.class);
		try {
			return query.getResultList().stream().distinct().collect(Collectors.toList());
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}
	/**
	 * Checks if the Event has tickets of the type Pass
	 * And if it does returns the number  of passes available
	 * @param eventName the name of the event
	 * @return the number of passes available
	 * @throws ApplicationException if there are no passes available
	 */
	public int checkNumberPassesAvailable(String eventName) throws ApplicationException {
		TypedQuery<Ticket> query = em.createNamedQuery(Event._TICKETS, Ticket.class);
		query.setParameter(Event.STRING_NAME, eventName);
		try {

			List<Ticket> listTickets = query.getResultList();

			boolean available = false;
			int numberAvailable = 0;
			for (Ticket t : listTickets) {
				if (t.getState() == TicketState.AVAILABLE && t.getClass().equals(PassTicket.class)) {
					available = true;
					numberAvailable++;
				}
			}

			if (!available) {
				throw new EventWithoutPassTicketException("Error, there are no passes available.");
			}
			return numberAvailable;

		} catch (Exception e) {
			throw new ApplicationException("Error checking Number of Passes.", e);
		}

	}

	/**
	 * Checks if the Event with the name exists
	 * @param eventName the name of the event
	 * @throws ApplicationException if the Event does not exists
	 * @see checkEvent(), it does the opposite of this method.
	 */
	public void checkExistentEvent(String eventName) throws ApplicationException {
		TypedQuery<String> query = em.createNamedQuery(Event.FIND_BY_NAME, String.class);
		query.setParameter(Event.STRING_NAME, eventName);
		try {
			if (query.getResultList().isEmpty()) {
				throw new NoEventFoundException("Event " + eventName + " does not exist.");
			}
		} catch (PersistenceException e) {
			throw new NoEventFoundException("Error finding Event.", e);
		}
	}
	
	
	public List<EventDate> getDatesWithAvailableTicketsForEvent(String eventName, LocalDate fromDate) throws ApplicationException {
		TypedQuery<EventDate> query = em.createNamedQuery(Event.DATES_WITH_TICKETS_BY_STATE, EventDate.class);
		query.setParameter(Event.STRING_NAME, eventName);
		query.setParameter(Event.DATE_DATE, fromDate);
		query.setParameter(Event.ENUM_TICKET_STATE, TicketState.AVAILABLE);
		try {
			return query.getResultList();
		} catch (Exception e) {
			throw new ApplicationException("Error retrieving available event dates.", e);
		}
	}
	
	public List<Seat> getAvailableSeatsForEventDate(String eventName, LocalDate date) throws ApplicationException {
		TypedQuery<Seat> query = em.createNamedQuery(Event.SEATS_FOR_EVENT_DATE_BY_STATE, Seat.class);
		query.setParameter(Event.STRING_NAME, eventName);
		query.setParameter(Event.DATE_DATE, date);
		query.setParameter(Event.ENUM_TICKET_STATE, TicketState.AVAILABLE);
		try {
			return query.getResultList();
		} catch (Exception e) {
			throw new ApplicationException("Error retrieving available seats for event date.", e);
		}
	}
}

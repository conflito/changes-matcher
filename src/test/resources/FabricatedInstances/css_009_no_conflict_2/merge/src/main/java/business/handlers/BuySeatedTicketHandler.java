package business.handlers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import business.catalogs.EventCatalog;
import business.catalogs.TicketCatalog;
import business.entities.Event;
import business.entities.EventDate;
import business.entities.Seat;
import business.entities.Ticket;
import business.entities.TicketState;
import business.entities.TicketType;
import facade.exceptions.ApplicationException;

public class BuySeatedTicketHandler {

	/**
	 * Entity manager factory for accessing the persistence service 
	 */
	private EntityManagerFactory emf;
	
	/**
	 * Creates a handler for the add customer use case given
	 * the application's entity manager factory
	 *  
	 * @param emf The entity manager factory of tha application
	 */
	public BuySeatedTicketHandler(EntityManagerFactory emf) {
		this.emf = emf;
	}
	
	
	public List<EventDate> getAvailableEventDates(String eventName) throws ApplicationException {
		EntityManager em = emf.createEntityManager();
		EventCatalog eventCatalog = new EventCatalog(em);
		List<EventDate> availableDates = new ArrayList<>();

		try {
			em.getTransaction().begin();
			// retrieve event. Throws exception if not found.
			Event event = eventCatalog.getEvent(eventName);
			if (!event.getType().isSeated())
				throw new ApplicationException("Event is not seated!");
			// retrieve event dates after current dates and with available tickets. Throws exception if query fails.
			availableDates = eventCatalog.getDatesWithAvailableTicketsForEvent(event.getName(), new Date());
			em.getTransaction().commit();			
		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			throw new ApplicationException("Error buying ticket.", e);
		}
		return availableDates;
	}
	
	
	public List<Seat> pickEventDate(String eventName, Date date) throws ApplicationException {
		EntityManager em = emf.createEntityManager();
		EventCatalog eventCatalog = new EventCatalog(em);
		List<Seat> availableSeats = new ArrayList<>();
		
		try {
			em.getTransaction().begin();
			// retrieve event. Throws exception if not found.
			Event event = eventCatalog.getEvent(eventName);
			if (!event.getType().isSeated())
				throw new ApplicationException("Event is not seated!");
			// retrieve available seats for the given date. Throws exception if query fails.
			availableSeats = eventCatalog.getAvailableSeatsForEventDate(event.getName(), date);
			em.getTransaction().commit();			
		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			throw new ApplicationException("Error picking event date.", e);
		}
		return availableSeats;
	}
	
	
	public String reserveSingleTicketForSeatedEventDate(String eventName, Date date, String rowLetters, int seatNumber, String customerEMail) throws ApplicationException {
		EntityManager em = emf.createEntityManager();
		EventCatalog eventCatalog = new EventCatalog(em);
		TicketCatalog ticketCatalog = new TicketCatalog(em);
		
		// TODO: implement lock for Ticket
		try {
			em.getTransaction().begin();
			// retrieve ticket for given event date and seat. Throws exception if not found 
			Ticket ticket = ticketCatalog.getTicketForEventDateSeat(eventName, date, rowLetters, seatNumber);
			// just to be safe check ticket type is SINGLE
			if (ticket.getType() != TicketType.SINGLE)
				throw new ApplicationException("Wrong ticket type: expected SINGLE and got " + ticket.getType().name());
			ticket.setState(TicketState.RESERVERD);
			em.persist(ticket);
			
			em.getTransaction().commit();			
		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			throw new ApplicationException("Error picking event date.", e);
		}
	}
}


package business.handlers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import business.util.Pair;

import business.event.Event;
import business.event.EventCatalog;
import business.event.EventDate;
import business.event.Seat;
import business.event.exceptions.EventIsNotSeatedException;
import business.ticket.ReservationCatalog;
import business.ticket.Ticket;
import business.ticket.TicketCatalog;
import business.ticket.TicketState;
import business.ticket.exceptions.TicketNotAvailableException;
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
	
	
	public List<EventDate> getAvailableSeatedEventDates(String eventName) throws ApplicationException {
		EntityManager em = emf.createEntityManager();
		EventCatalog eventCatalog = new EventCatalog(em);
		List<EventDate> availableDates = new ArrayList<>();

		try {
			em.getTransaction().begin();
			// retrieve event. Throws exception if not found.
			Event event = eventCatalog.getEvent(eventName);
			if (!event.getType().isSeated())
				throw new EventIsNotSeatedException("Event is not seated!");
			// retrieve event dates after current dates and with available tickets. Throws exception if query fails.
			availableDates = eventCatalog.getDatesWithAvailableTicketsForEvent(event.getName(), LocalDate.now());
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			throw new ApplicationException("Error buying ticket.", e);
		}
		return availableDates;
	}
	
	
	public List<Seat> pickEventDate(String eventName, LocalDate date) throws ApplicationException {
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
	
	
	public String reserveSingleTicketsForSeatedEventDate(String eventName, LocalDate date, List<Pair<String, Integer>> seatLabels, String customerEMail) throws ApplicationException {
		EntityManager em = emf.createEntityManager();
		TicketCatalog ticketCatalog = new TicketCatalog(em);
		ReservationCatalog reservationCatalog = new ReservationCatalog(em);
		
		// TODO: implement lock for Ticket
		// TODO: implement lock for Customer
		try {
			em.getTransaction().begin();
			// retrieve ticket for given event date and seat. Throws exception if not found 
			List<Ticket> tickets = ticketCatalog.getTicketsForEventDateSeats(eventName, date, seatLabels);
			for (Ticket t : tickets) {
				if (t.getState() != TicketState.AVAILABLE) {
					throw new TicketNotAvailableException("At least one of the seats is not available");
				}
			}
			for (Ticket t:tickets) t.setState(TicketState.RESERVED);
			reservationCatalog.addReservationSeated(tickets, eventName, customerEMail);
			em.getTransaction().commit();		
		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			throw new ApplicationException("Error reserving single tickets for seated event date.", e);
		}
		
		return "TODO: MB reference for payment";
	}
}


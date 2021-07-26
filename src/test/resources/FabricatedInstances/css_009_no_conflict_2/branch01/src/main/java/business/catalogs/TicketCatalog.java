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
import business.entities.Ticket;
import facade.exceptions.ApplicationException;

public class TicketCatalog {
	
	private EntityManager em;
	
	/**
	 * Constructs a Ticket's catalog given a entity manager factory
	 */
	public TicketCatalog(EntityManager em) {
		this.em = em;
	}

	
	public Ticket getTicketForEventDateSeat (String eventName, Date eventDate, String rowLetters, int seatNumber) throws ApplicationException {
		TypedQuery<Ticket> query = em.createNamedQuery(Ticket.TICKET_FOR_EVENT_SEAT, Ticket.class);
		query.setParameter(Ticket.STRING_EVENT_NAME, eventName);
		query.setParameter(Ticket.DATE_EVENT_DATE, eventDate);
		query.setParameter(Ticket.STRING_SEAT_ROW_LETTERS, rowLetters);
		query.setParameter(Ticket.INT_SEAT_NUMBER, seatNumber);
		try {
			return query.getSingleResult();
		} catch (PersistenceException e) {
			throw new ApplicationException("No ticket found for given event date and seat.", e);
		}
	}

}


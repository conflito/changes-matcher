package business.ticket;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import business.util.Pair;

import business.ticket.exceptions.TicketNotAvailableException;
import facade.exceptions.ApplicationException;


public class TicketCatalog {
	
	private EntityManager em;
	
	/**
	 * Constructs a Ticket's catalog given a entity manager factory
	 */
	public TicketCatalog(EntityManager em) {
		this.em = em;
	}

	
	public List<Ticket> getTicketsForEventDateSeats(String eventName, LocalDate eventDate, List<Pair<String, Integer>> seatLabels) throws ApplicationException {
		List<Ticket> tickets = new ArrayList<>();
		try {
			for (Pair<String, Integer> seatLabel: seatLabels) {
				TypedQuery<Ticket> query = em.createNamedQuery(SingleTicket.TICKET_FOR_EVENT_SEAT, Ticket.class);
				query.setParameter(SingleTicket.STRING_EVENT_NAME, eventName);
				query.setParameter(SingleTicket.DATE_EVENT_DATE, eventDate);
				query.setParameter(SingleTicket.STRING_SEAT_ROW_LETTERS, seatLabel.getValue0());
				query.setParameter(SingleTicket.INT_SEAT_NUMBER, seatLabel.getValue1());
				tickets.add(query.getSingleResult());
			}
		} catch (PersistenceException e) {
			throw new TicketNotAvailableException("No ticket found for given event date and seat.", e);
		}
		return tickets;
	}

}


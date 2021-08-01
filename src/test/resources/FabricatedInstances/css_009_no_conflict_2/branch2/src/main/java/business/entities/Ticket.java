/**
 * 
 */
package business.entities;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

/**
 * @author rui.fartaria
 *
 */
@Entity
@NamedQuery(name=Ticket.TICKET_FOR_EVENT_SEAT, query="SELECT t FROM Ticket t JOIN t.date ed JOIN t.seat s WHERE ed.event.name = :"+Ticket.STRING_EVENT_NAME+" AND ed.date = :"+Ticket.DATE_EVENT_DATE+" AND t.seat.rowLetters = :"+Ticket.STRING_SEAT_ROW_LETTERS+" AND t.seat.seatNumber = :"+Ticket.INT_SEAT_NUMBER)
public class Ticket {
	
	// Named query name constants
	public static final String TICKET_FOR_EVENT_SEAT = "Event.ticketForEventSeat";
	public static final String STRING_EVENT_NAME = "eventName";
	public static final String DATE_EVENT_DATE = "eventDate";
	public static final String STRING_SEAT_ROW_LETTERS = "seatRowLetters";
	public static final String INT_SEAT_NUMBER = "seatNumber";

	/**
	 * Primary key for JPA
	 */
	@Id @GeneratedValue private int id;
	
	/**
	 * 
	 */
	@Enumerated(EnumType.STRING)
	private TicketType type;
	
	/**
	 * 
	 */
	@Enumerated(EnumType.STRING)
	private TicketState state;
	
	/**
	 * 
	 */
	@ManyToOne @JoinColumn(nullable = false)
	private Event event;
	
	
	@ManyToOne @JoinColumn(nullable = true)
	private EventDate date;
	
	@ManyToOne @JoinColumn(nullable = true)
	private Seat seat;
	
	@ManyToOne @JoinColumn(nullable = true)
	private Customer customer;

	public TicketState getState() {
		return state;
	}

	public void setState(TicketState state) {
		this.state = state;
	}

	public TicketType getType() {
		return type;
	}

	public Event getEvent() {
		return event;
	}

	public EventDate getDate() {
		return date;
	}

	public Seat getSeat() {
		return seat;
	}
	
	
}

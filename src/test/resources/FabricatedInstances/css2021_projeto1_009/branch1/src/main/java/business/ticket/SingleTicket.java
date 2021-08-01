package business.ticket;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;

import business.event.Event;
import business.event.EventDate;
import business.event.Seat;

@Entity
@DiscriminatorValue("SINGLE")
@NamedQuery(name=SingleTicket.TICKET_FOR_EVENT_SEAT,
query="SELECT t FROM SingleTicket t JOIN t.date ed JOIN t.seat s WHERE ed.event.name = :"+SingleTicket.STRING_EVENT_NAME
		+" AND ed.date = :"+SingleTicket.DATE_EVENT_DATE
		+" AND t.seat.rowLetters = :"+SingleTicket.STRING_SEAT_ROW_LETTERS
		+" AND t.seat.seatNumber = :"+SingleTicket.INT_SEAT_NUMBER)

public class SingleTicket extends Ticket {

	// Named query name constants
	public static final String TICKET_FOR_EVENT_SEAT = "Event.ticketForEventSeat";
	public static final String STRING_EVENT_NAME = "eventName";
	public static final String DATE_EVENT_DATE = "eventDate";
	public static final String STRING_SEAT_ROW_LETTERS = "seatRowLetters";
	public static final String INT_SEAT_NUMBER = "seatNumber";
		
	// has to be nullable because we are using InheritanceType.SINGLE_TABLE
	@JoinColumn(nullable = true)
	private EventDate date;
	
	/**
	 * JPA constructor
	 */
	public SingleTicket() {
	}
	
	public SingleTicket(Event event, int value, EventDate date) {
		this(event, value, date, null);
	}
	
	public SingleTicket(Event event, int value, EventDate date, Seat seat) {
		super(event, value, seat);
		this.date = date;
	}
	
	public EventDate getDate() {
		return this.date;
	}

	public int doubleValue(){
		return getValue() * 2;
	}

}

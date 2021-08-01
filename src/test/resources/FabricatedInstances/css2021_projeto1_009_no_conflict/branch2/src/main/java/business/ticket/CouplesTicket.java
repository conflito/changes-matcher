package business.ticket;

import business.event.Event;
import business.event.EventDate;
import business.event.Seat;

public class CouplesTicket extends Ticket {

    private EventDate date;

    public CouplesTicket(){

    }

    public CouplesTicket(Event event, int value, EventDate date) {
		this(event, value, date, null);
	}
	
	public CouplesTicket(Event event, int value, EventDate date, Seat seat) {
		super(event, value, seat);
		this.date = date;
	}
	
	public EventDate getDate() {
		return this.date;
	}
}

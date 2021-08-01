package business.ticket;

import business.event.Event;
import business.event.EventDate;
import business.event.Seat;

public class FamilyTicket extends Ticket {

    private EventDate date;

    public FamilyTicket(){

    }

    public FamilyTicket(Event event, int value, EventDate date) {
		this(event, value, date, null);
	}
	
	public FamilyTicket(Event event, int value, EventDate date, Seat seat) {
		super(event, value, seat);
		this.date = date;
	}
	
	public EventDate getDate() {
		return this.date;
	}
}

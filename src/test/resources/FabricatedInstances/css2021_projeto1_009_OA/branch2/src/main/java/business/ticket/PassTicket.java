package business.ticket;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import business.event.Event;

import business.event.Seat;

@Entity
@DiscriminatorValue("PASS")
public class PassTicket extends Ticket {

// 	TODO: DELETE: pass ticket is for all event dates, therefore, no need for this field
//	@OneToMany @JoinColumn
//	private List<EventDate> dates;
	
	/**
	 * JPA constructor
	 */
	public PassTicket() {
		
	}
	
	public PassTicket(Event event, int value) {
		this(event, value, null);
	}
	
	public PassTicket(Event event, int value, Seat seat) {
		super(event, value, seat);
	}

}

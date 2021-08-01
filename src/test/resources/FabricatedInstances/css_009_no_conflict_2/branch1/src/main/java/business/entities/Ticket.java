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

/**
 * @author rui.fartaria
 *
 */
@Entity
public class Ticket {

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

/**
 * 
 */
package business.ticket;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import business.event.Event;
import business.event.Seat;

/**
 *
 */
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="TICKETTYPE", discriminatorType=DiscriminatorType.STRING)
public abstract class Ticket {
	
	/**
	 * Primary key for JPA
	 */
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private TicketState state;
	
	private int value;
	
	@JoinColumn(nullable = true)
	private Seat seat;

	@ManyToOne @JoinColumn(nullable = false)
	private Event event;
	
	@ManyToOne @JoinColumn(nullable = true)
	private Reservation reservation;

	
	/**
	 * Constructor for JPA
	 */
	protected Ticket() {
		this.state = TicketState.AVAILABLE;
	}
	
	protected Ticket(Event event, int value) {
		this(event, value, null);
	}
	
	protected Ticket(Event event, int value, Seat seat) {
		this();
		this.event = event;
		this.value = value;
		this.seat = seat;
		state = TicketState.AVAILABLE;
	}
	
	public Seat getSeat() {
		return this.seat;
		
	}
	
	public int getValue() {
		return this.value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	public TicketState getState() {
		return state;
	}

	public void setState(TicketState state) {
		this.state = state;
	}

	public Event getEvent() {
		return event;
	}

	public void changeState(TicketState state) {
		this.state = state;
	}
	
	
}

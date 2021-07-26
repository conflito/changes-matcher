/**
 * 
 */
package business.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * @author rui.fartaria
 *
 */
@Entity
@NamedQueries({
	@NamedQuery(name=Event.FIND_BY_NAME, query="SELECT e FROM Event e WHERE e.name = :" + Event.STRING_NAME),
	@NamedQuery(name=Event.DATES_WITH_AVAILABLE_TICKETS, query="SELECT DISTINCT ed FROM Ticket t JOIN t.date ed WHERE ed.event.name = :"+Event.STRING_NAME+" AND ed.date > :"+Event.DATE_DATE+" AND t.state='AVAILABLE' ORDER BY ed.date ASC"),
	@NamedQuery(name=Event.AVAILABLE_SEATS_FOR_EVENT_DATE, query="SELECT DISTINCT s FROM Ticket t JOIN t.date ed JOIN t.seat s WHERE ed.event.name = :"+Event.STRING_NAME+" AND ed.date = :"+Event.DATE_DATE+" AND t.state='AVAILABLE' ORDER BY s.rowLetters ASC, s.seatNumber ASC"),	
})
public class Event {
	
	// Named query name constants
	public static final String FIND_BY_NAME = "Event.findByName";
	public static final String DATES_WITH_AVAILABLE_TICKETS = "Event.datesWithAvailableTickets";
	public static final String AVAILABLE_SEATS_FOR_EVENT_DATE = "Event.availableSeatsForEventDate";
	public static final String STRING_NAME = "name";
	public static final String DATE_DATE = "date";

	/**
	 * Primary key for JPA
	 */
	@Id @GeneratedValue private int id;
	
	/**
	 * Event name
	 */
	@Column(nullable = false, unique = true)
	private String name;

	/**
	 * 
	 */
	@Temporal(TemporalType.DATE)
	private java.util.Date startSellingDate;

	/**
	 * 
	 */
	@ManyToOne
	@JoinColumn(nullable = false)
	private EventType type;
	
	/**
	 * 
	 */
	@OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
	private Collection<EventDate> dates;
	
	/**
	 * 
	 */
	@OneToMany(mappedBy = "event",  cascade = CascadeType.ALL, orphanRemoval = true)
	private Collection<Ticket> tickets;
	
	/**
	 * 
	 */
	@ManyToOne @JoinColumn(nullable = true)
	private EventVenue venue;
	
	@ManyToOne @JoinColumn(nullable = false)
	private Producer producer;
	
	@ManyToOne @JoinColumn(nullable = false)
	private Manager manager;
	
	
	public Event() {
		this.dates = new ArrayList<>();
		this.tickets = new ArrayList<>();
	}
	
	public Event(Manager manager, Producer producer, EventType eventType, List<List<Date>> eventDates) {
		// TODO: 1. Associate Manager, Producer and EventType; 2. create event dates
	}
	
	public java.util.Date getStartSellingDate() {
		return startSellingDate;
	}

	public void setStartSellingDate(java.util.Date startSellingDate) {
		this.startSellingDate = startSellingDate;
	}
	
	public EventVenue getVenue() {
		return venue;
	}

	public void setVenue(EventVenue venue) {
		this.venue = venue;
	}

	public String getName() {
		return name;
	}

	public EventType getType() {
		return type;
	}

	public Collection<Ticket> getTickets() {
		return tickets;
	}

	public Producer getProducer() {
		return producer;
	}

	public void setName(String name) {
		this.name = name;
	}
}

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
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * @author rui.fartaria
 *
 */
@Entity
@NamedQuery(name=Event.FIND_BY_NAME, query="SELECT e FROM Event e WHERE e.name = :" + 
		Event.STRING_NAME)
public class Event {
	
	// Named query name constants
	public static final String FIND_BY_NAME = "Event.findByName";
	public static final String STRING_NAME = "name";

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

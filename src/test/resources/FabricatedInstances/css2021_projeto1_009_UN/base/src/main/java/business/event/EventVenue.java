/**
 * 
 */
package business.event;

import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 *
 */
@Entity
@NamedQueries({ @NamedQuery(name = EventVenue.FIND_ALL, query = "SELECT ev.name FROM EventVenue ev"),
		@NamedQuery(name = EventVenue.FIND_BY_NAME, query = "SELECT ev FROM EventVenue ev WHERE ev.name = :"
				+ EventVenue.STRING_VENUE_NAME),
		@NamedQuery(name = EventVenue.EVENTVENUE_AVAILABLE, query = "SELECT ed FROM EventDate ed JOIN ed.event e JOIN e.venue ev WHERE ev.name = :"
				+ EventVenue.STRING_VENUE_NAME + " AND ed.date IN (SELECT ed1.date FROM Event e1 JOIN e1.dates ed1 where e1.name = :" + EventVenue.STRING_EVENT_NAME + ")")
})

//@NamedQuery(name = EventVenue.EVENTVENUE_AVALIABLE, query = "SELECT ed FROM EventDate ed JOIN ed.event e JOIN e.venue ev WHERE ev.name = :"
//		+ EventVenue.STRING_NAME + " AND ed.date = :" + EventVenue.DATE_DATE),


public class EventVenue {

	// Named query name constants
	public static final String FIND_BY_NAME = "EventVenue.findByName";
	public static final String STRING_VENUE_NAME = "venue_name";
	public static final String STRING_EVENT_NAME = "event_name";
	public static final String FIND_ALL = "EventVenues";
	public static final String EVENTVENUE_AVALIABLE = "EventVenue.EVENTVENUE_AVALIABLE";
	public static final String EVENTVENUE_AVAILABLE = "EventVenue.EVENTVENUE_AVAILABLE";
	public static final String EVENTVENUE_ID = "id";
	public static final String DATE_DATE = "date";

	/**
	 * Primary key for JPA
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	/**
	 * The name of the Venue
	 */
	@Column(nullable = false, unique = true)
	private String name;

	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private boolean seated;

	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private int capacity;

	/**
	 * List of the seats of the Venue
	 */
	@OneToMany(mappedBy = "venue")
	private List<Seat> seats;

	// Getters and Setters

	/**
	 * Gets the name of venue
	 * 
	 * @return the name of the venue
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the Venue
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Checks if the venue is seated
	 * 
	 * @return if the venue is seated or not
	 */
	public boolean isSeated() {
		return seated;
	}

	/**
	 * 
	 * @param seated
	 */
	public void setSeated(boolean seated) {
		this.seated = seated;
	}

	/**
	 * 
	 * @return
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * 
	 * @param capacity
	 */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	/**
	 * 
	 * @return
	 */
	public Collection<Seat> getSeats() {
		return seats;
	}
}

/**
 * 
 */
package business.event;



import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import business.util.Triplet;

import business.producer.Producer;
import business.ticket.Ticket;

/**
 * 
 * An Event
 */
@Entity
@NamedQueries({
		@NamedQuery(name = Event.FIND_BY_NAME, query = "SELECT e FROM Event e WHERE e.name = :" + Event.STRING_NAME),
		@NamedQuery(name = Event.DATES_WITH_TICKETS_BY_STATE, query = "SELECT DISTINCT ed FROM SingleTicket t JOIN t.date ed WHERE ed.event.name = :"
				+ Event.STRING_NAME + " AND ed.date > :" + Event.DATE_DATE
				+ " AND t.state=:" + Event.ENUM_TICKET_STATE + " ORDER BY ed.date ASC"),
		@NamedQuery(name = Event.SEATS_FOR_EVENT_DATE_BY_STATE, query = "SELECT DISTINCT s FROM SingleTicket t JOIN t.date ed JOIN t.seat s WHERE ed.event.name = :"
				+ Event.STRING_NAME + " AND ed.date = :" + Event.DATE_DATE
				+ " AND t.state=:" + Event.ENUM_TICKET_STATE + " ORDER BY s.rowLetters ASC, s.seatNumber ASC"),
		@NamedQuery(name= Event._TICKETS, query= "SELECT e.tickets FROM Event e WHERE e.name = :" + Event.STRING_NAME),
		})

public class Event {

	// Named query name constants
	public static final String FIND_BY_NAME = "Event.findByName";
	public static final String DATES_WITH_TICKETS_BY_STATE = "Event.datesWithTicketsByState";
	public static final String SEATS_FOR_EVENT_DATE_BY_STATE = "Event.seatsForEventDateByState";
	public static final String STRING_NAME = "name";
	public static final String DATE_DATE = "date";
	public static final String _TICKETS = "tickets";
	public static final String ENUM_TICKET_STATE = "ticket_state";

	/**
	 * 
	 */
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	/**
	 * Event name
	 */
	@Column(nullable = false, unique = true, name="Name")
	private String name;

	/**
	 * the starting date when the tickets of the Event are available
	 */
	@Convert(converter = LocalDateAttributeConverter.class)
	private LocalDate ticketStartSellingDate;

	/**
	 * the type of the Event
	 */
	@Enumerated(EnumType.STRING)
	private String type;

	/**
	 * the dates of the Event
	 */
	@OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<EventDate> dates;

	/**
	 * the tickets of the Event
	 */
	@OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Ticket> tickets;

	/**
	 * the venue where the Event will occur 
	 */
	@ManyToOne
	private EventVenue venue;

	/**
	 * the producer responsible for the Event
	 */
	@ManyToOne
	private Producer producer;


	public Event() {
		this.dates = new ArrayList<>();
		this.tickets = new ArrayList<>();
	}
	
	/**
	 * Constructor of the Event
	 * @param producer producer of the event
	 * @param eventType type of the Event
	 * @param eventName name of the Event
	 * @param eventDates dates of the Event
	 */
	public Event(Producer producer, String eventType, String eventName, List<Triplet<LocalDate, LocalTime, LocalTime>> eventDates) {


		this.producer = producer;
		this.type = eventType;
		this.dates = new ArrayList<>();
		this.name = eventName;
		this.tickets = new ArrayList<>();
		for (Triplet<LocalDate, LocalTime, LocalTime> triple : eventDates) {
			dates.add(new EventDate(triple.getValue0(),triple.getValue1(),triple.getValue2(),this));
		}
	}
	
	
	// Getters and Setters
	
	public int getId() {
		return id;
	}
	
	/**
	 * gets the starting date of the tickets
	 * @return the starting date of the tickets
	 */
	public LocalDate getStartSellingDate() {
		return ticketStartSellingDate;
	}

	/**
	 * sets the starting date of the tickets
	 * @param startSellingDate
	 */
	public void setStartSellingDate(LocalDate startSellingDate) {
		this.ticketStartSellingDate = startSellingDate;
	}
	
	/**
	 * gets the venue of the Event
	 * @return the venue of the Event
	 */
	public EventVenue getVenue() {
		return venue;
	}
	/**
	 * sets the venue of the Event
	 * @param venue of the event
	 */
	public void setVenue(EventVenue venue) {
		this.venue = venue;
	}
	/**
	 * gets the name of the Event
	 * @return the name of the Event
	 */
	public String getName() {
		return name;
	}
	/**
	 * gets the type of the Event
	 * @return the type of the Event
	 */
	public String getType() {
		return type;
	}
	/**
	 * get the tickets of the Event
	 * @return the tickets
	 */
	public List<Ticket> getTickets() {
		return tickets;
	}
	/**
	 * gets the producer responsible of the Event
	 * @return the producer of the Event
	 */
	public Producer getProducer() {
		return producer;
	}

	public void setProducer(Producer p){
		producer = p;
	}
	
	/**
	 * sets the tickets of the Event
	 * @param tickets
	 */
	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}
	
	/**
	 * sets the name of the Event
	 * @param name of the Event
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return list of EventDate sorted by EventDate.date
	 */
	public List<EventDate> getDates() {
		dates.sort(EventDate.ED_ASC);
		return dates;
	}
}

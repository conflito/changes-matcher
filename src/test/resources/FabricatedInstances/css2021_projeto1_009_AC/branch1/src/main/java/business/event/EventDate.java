/**
 * 
 */
package business.event;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


/**
 *
 */
@Entity
public class EventDate {
	
	/**
	 * Primary key for JPA
	 */
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	/**
	 * Date of the Event.
	 */
	@Column
	@Convert(converter = LocalDateAttributeConverter.class)
	private LocalDate date;
	
	/**
	 * Start time of the Event.
	 */
	@Column
	@Convert(converter = LocalTimeAttributeConverter.class)
	private LocalTime startsAt;
	
	/**
	 * Ending time of the Event.
	 */
	@Column
	@Convert(converter = LocalTimeAttributeConverter.class)
	private LocalTime finishesAt;
	
	/**
	 * The Event associated with this Date.
	 */
	@ManyToOne @JoinColumn(nullable = false)
	private Event event;
	
	public EventDate() {
		
	}
	/**
	 * Constructor of the Date
	 * 
	 * @param eventDate date of the Event
	 * @param startsAt the start time of the Event
	 * @param finishesAt the ending time of the Event
	 * @param e Event associated with this Date
	 */
	public EventDate(LocalDate eventDate, LocalTime startsAt, LocalTime finishesAt, Event e) {
		this.date = eventDate;
		this.startsAt = startsAt;
		this.finishesAt = finishesAt;
		this.event = e;
	}
	
	// Getters and Setters
	
	/**
	 * Gets the Date. 
	 * @return the date.
	 */
	public LocalDate getDate() {
		return date;
	}
	
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	/**
	 * Gets the start time. 
	 * @return
	 */
	public LocalTime getStartsAt() {
		return startsAt;
	}
	
	public void setStartsAt(LocalTime startsAt) {
		this.startsAt = startsAt;
	}
	
	/**
	 * Gets the ending time.
	 * @return
	 */
	public LocalTime getFinishesAt() {
		return finishesAt;
	}
	
	public void setFinishesAt(LocalTime finishesAt) {
		this.finishesAt = finishesAt;
	}

	/**
	 * Gets the Event associated.
	 * @return
	 */
	public Event getEvent() {
		return event;
	}
	
	public static final Comparator<EventDate> ED_ASC = new Comparator<EventDate>() {
	     public int compare(EventDate ev1, EventDate ev2) {
	        return ev1.getDate().isAfter(ev2.getDate()) ? -1 : 1;
	     }
   };
}


/**
 * 
 */
package business.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author rui.fartaria
 *
 */
@Entity
public class Seat {

	/**
	 * Primary key for JPA
	 */
	@Id @GeneratedValue
	private int id;
	
	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private String rowLetters;
	
	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private int seatNumber;
	
	/**
	 * 
	 */
	@ManyToOne
	@JoinColumn(nullable = false)
	private EventVenue venue;
	
	public Seat() {
		super();
	}
	
	public Seat(EventVenue venue, String rowLetters, int seatNumber) {
		this.venue = venue;
		this.rowLetters = rowLetters;
		this.seatNumber = seatNumber;
	}

	public String getRowLetters() {
		return rowLetters;
	}

	public int getSeatNumber() {
		return seatNumber;
	}

	public EventVenue getVenue() {
		return venue;
	}
	
}

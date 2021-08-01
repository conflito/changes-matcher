/**
 * 
 */
package business.event;

import javax.persistence.Column;
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
public class Seat {

	/**
	 * Primary key for JPA
	 */
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	/**
	 * 
	 */
	@Column
	private String rowLetters;
	
	/**
	 * 
	 */
	@Column
	private int seatNumber;
	
	/**
	 * 
	 */
	@ManyToOne
	@JoinColumn(nullable = false)
	private EventVenue venue;

	private boolean vip;
	
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

	public boolean isVip(){
		return vip;
	}

	public void setVip(boolean vip){
		this.vip = vip;
	}
	
}

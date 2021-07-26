/**
 * 
 */
package business.entities;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * @author rui.fartaria
 *
 */
@Entity
public class EventVenue {

	/**
	 * Primary key for JPA
	 */
	@Id @GeneratedValue private int id;
	
	/**
	 * 
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
	 * 
	 */
	@OneToMany(mappedBy = "venue")
	private Collection<Seat> seats;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSeated() {
		return seated;
	}

	public void setSeated(boolean seated) {
		this.seated = seated;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public Collection<Seat> getSeats() {
		return seats;
	}
}

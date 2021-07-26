package business.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

/**
 * @author rui.fartaria
 *
 */
@Entity
@NamedQuery(name=EventType.FIND_BY_DESIGNATION, query="SELECT et FROM EventType et WHERE et.designation = :" + 
		EventType.STRING_DESIGNATION)
public class EventType {
	
	// Named query name constants
		public static final String FIND_BY_DESIGNATION = "EventType.findByDesignation";
		public static final String STRING_DESIGNATION = "designation";
	
	/**
	 * Primary key for JPA
	 */
	@Id @GeneratedValue private int id;

	/**
	 * 
	 */
	@Column(nullable=false, unique=true)
	private String designation;
	
	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private int maxAudience;
	
	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private boolean seated;

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public int getMaxAudience() {
		return maxAudience;
	}

	public void setMaxAudience(int maxAudience) {
		this.maxAudience = maxAudience;
	}

	public boolean isSeated() {
		return seated;
	}

	public void setSeated(boolean seated) {
		this.seated = seated;
	}
}

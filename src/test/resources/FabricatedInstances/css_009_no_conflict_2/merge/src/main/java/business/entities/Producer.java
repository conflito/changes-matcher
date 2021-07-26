package business.entities;

import java.lang.String;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;


/**
 * @author rui.fartaria
 *
 */
@Entity
@NamedQuery(name=Producer.FIND_BY_REGNUM, query="SELECT p FROM Producer p WHERE p.resgistrationNumber = :" + 
		Producer.INT_REGNUM)
public class Producer {

	// Named query name constants
	public static final String FIND_BY_REGNUM = "Producer.findByRegistrationNumber";
	public static final String INT_REGNUM = "resgistrationNumber";
	   
	/**
	 * 
	 */
	@Id @GeneratedValue
	private int id;
	
	/**
	 * 
	 */
	@Column(unique = true)
	private int resgistrationNumber;
	
	/**
	 * 
	 */
	private String name;
	
	/**
	 * 
	 */
	@ManyToMany  
	private Collection<EventType> certifiedFor;
	
	@OneToMany(mappedBy = "producer")
	private Collection<Event> produces;

	public Producer() {
		super();
	}   
	public int getId() {
		return this.id;
	}

	public int getResgistrationNumber() {
		return this.resgistrationNumber;
	}

	public void setResgistrationNumber(int resgistrationNumber) {
		this.resgistrationNumber = resgistrationNumber;
	}   

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}   
	
	public Collection<EventType> getCertifiedFor() {
		return this.certifiedFor;
	}

	public Collection<Event> getProduces() {
		return produces;
	}
}

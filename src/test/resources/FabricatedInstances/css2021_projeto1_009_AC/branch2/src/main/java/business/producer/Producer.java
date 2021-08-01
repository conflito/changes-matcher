package business.producer;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import business.event.Event;
import business.event.EventType;

/**
 * 
 * A Producer
 */
@Entity
@NamedQueries({
@NamedQuery(name=Producer.FIND_BY_REGNUM, 
query="SELECT p FROM Producer p WHERE p.registrationNumber = :" + Producer.INT_REGNUM),
@NamedQuery(name=Producer.GET_THIS_PRODUCER_CERTIFICATION,
query="SELECT p.certifiedFor FROM Producer p WHERE p.registrationNumber= :" + Producer.INT_REGNUM),
@NamedQuery(name=Producer.GET_EVENT_TYPES_CERTIFICATION,
query="SELECT p.certifiedFor FROM Producer p"),



})
public class Producer {

	// Named query name constants
	public static final String FIND_BY_REGNUM = "Producer.findByRegistrationNumber";
	public static final String INT_REGNUM = "registrationNumber";
	public static final String GET_EVENT_TYPES_CERTIFICATION = "Producer.getEventTypesCertification";
	public static final String GET_THIS_PRODUCER_CERTIFICATION = "Producer.getThisProducerCertification";
	   
	
	/**
	 * 
	 */
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private int registrationNumber;
	
	/**
	 * name of the producer
	 */
	private String name;
	
	/**
	 * the types of event that the producer is certified for.
	 */
	@ElementCollection
	@Enumerated(EnumType.STRING)
	private List<EventType> certifiedFor;
	
	/**
	 * Events that the producer is responsible 
	 */
	@OneToMany(mappedBy = "producer")
	@JoinColumn(name="producer_id")
	private List<Event> produces;

	/**
	 * Constructor of the Producer
	 */
	public Producer() {
		super();
		certifiedFor = new ArrayList<>();
	}   

	// Getters and Setters
	
	/**
	 * Gets the registration number of the producer
	 * @return the number of the producer
	 */
	public int getRegistrationNumber() {
		return this.registrationNumber;
	}

	public void setResgistrationNumber(Integer resgistrationNumber) {
		this.registrationNumber = resgistrationNumber.intValue() + 10;
	}   

	/**
	 * 
	 * @param resgistrationNumber
	 */
	public void setResgistrationNumber(int resgistrationNumber) {
		this.registrationNumber = resgistrationNumber;
	}   
	/**
	 * 
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}   
	
	/**
	 * 
	 * @return
	 */
	public List<EventType> getCertifiedFor() {
		
		return this.certifiedFor;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Event> getProduces() {
		return produces;
	}
}

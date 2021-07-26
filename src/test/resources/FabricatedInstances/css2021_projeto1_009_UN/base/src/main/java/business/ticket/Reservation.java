package business.ticket;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.*;

import business.event.LocalDateAttributeConverter;
import business.event.LocalTimeAttributeConverter;

/**
 * Entity implementation class for Entity: Reservation
 *
 */
@NamedQuery(name=Reservation.GET_ALL_REFERENCES, query= "SELECT r.reference FROM Reservation r")
@Entity
public class Reservation {

	
	protected static final String GET_ALL_REFERENCES = "Reservation.getAllReferences";
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column
	private String entity;
	
	@Column
	private String reference;
	
	@Convert(converter = LocalDateAttributeConverter.class)
	private LocalDate dateReservation;
	
	@Convert(converter = LocalTimeAttributeConverter.class)
	private LocalTime timeReservation;
	
	@Column
	private int value;
	
	@Column
	private String associatedEmail;
	
	@OneToMany(mappedBy="reservation")
	private List<Ticket> tickets;

	public Reservation() {
		super();
	}   
	public String getEntity() {
		return this.entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}   
	public String getReference() {
		return this.reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}   
	public int getValue() {
		return this.value;
	}

	public void setValue(int value) {
		this.value = value;
	}
   
	public void setEmail(String email) {
		this.associatedEmail = email;
	}
}

package business.entities;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Entity implementation class for Entity: Customer
 *
 */
@Entity
public class Customer {
   
	@Id @GeneratedValue private int id;
	
	private String eMail;
	
	@OneToMany(mappedBy = "customer")
	private Collection<Ticket> tickets;

	public Customer() {
		super();
		this.tickets = new ArrayList<>();
	}
	
	public Customer(String eMail) {
		super();
		this.tickets = new ArrayList<>();
		this.eMail = eMail;
	}
	
	public boolean hasValidEmail() {
		// TODO: implement regex validation
		return true;
	}

	public int getId() {
		return this.id;
	}

	public String getEMail() {
		return this.eMail;
	}

	public void setEMail(String eMail) {
		this.eMail = eMail;
	}
   
}

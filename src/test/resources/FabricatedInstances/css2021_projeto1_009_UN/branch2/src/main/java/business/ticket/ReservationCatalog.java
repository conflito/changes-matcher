package business.ticket;


import java.util.Collection;
import java.util.List;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import business.util.Triplet;


public class ReservationCatalog {

	
	private EntityManager em;
	private Random rd;
	
	public ReservationCatalog(EntityManager em) {
		this.em = em;
		rd = new Random();
	}
	
	
	public Triplet<String, String, Integer>addReservationSeated(Collection<Ticket> tickets, String eventName, String associatedEmail) {
		Reservation r = new Reservation();
		r.setEntity(eventName);
		r.setValue(sumTickets(tickets));
		r.setReference(creationReference());
		r.setEmail(associatedEmail);
		em.persist(r);
		return new Triplet<>(r.getEntity(), r.getReference(), r.getValue());
	}
	
	public Triplet<String, String, Integer>addReservationNotSeated(int numberOfTickets, String producerName, String associatedEmail, int value){
		Reservation r = new Reservation();
		r.setEntity(producerName);
		r.setValue(numberOfTickets * value);
		r.setReference(creationReference());
		em.persist(r);
		return new Triplet<>(r.getEntity(), r.getReference(), r.getValue());
		
	}

	
	private int sumTickets(Collection<Ticket> tickets) {
		
		int sum = 0;
		for(Ticket t : tickets) {
			sum += t.getValue();
		}
		return sum;
		
	}
	
	private String creationReference() {
		
		
		TypedQuery<String> query = em.createNamedQuery(Reservation.GET_ALL_REFERENCES, String.class);
		try {
			List<String> listReferences = query.getResultList();
			
			String reference = generatorReferenceNumber();
			
			while(listReferences.contains(reference)) {
				reference = generatorReferenceNumber();
			}
			
			return reference;
			
		} catch (Exception e) {
			return "";
		}
		
	}
	
	private String generatorReferenceNumber() {
		
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i <= 9; i++) {
			sb.append(rd.nextInt(10));
		}
		
		return sb.toString();
	}
}


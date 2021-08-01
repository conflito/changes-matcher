package business.handlers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import business.util.Triplet;

import business.event.EventCatalog;
import business.ticket.ReservationCatalog;
import business.ticket.Ticket;
import business.ticket.TicketState;
import facade.exceptions.ApplicationException;

/**
 *
 *
 */

public class BuyPassTicketsHandler {

	private EntityManagerFactory emf;
	private String eventName;
	private int numberPasses;

	public BuyPassTicketsHandler(EntityManagerFactory emf) {
		this.emf = emf;
	}

	/**
	 * Initiates the process of buying a Pass Ticket 
	 * and shows the number of passes available
	 * @param eventName the name of the event
	 * @return number of passes available
	 * @throws ApplicationException if any of the checks fails.
	 */
	public int buyPass(String eventName) throws ApplicationException {
		EntityManager em = emf.createEntityManager();
		int number = 0;
		try {
			checkEventName(eventName, em);
			number = checkPassAvailability(eventName, em);
		} catch (Exception e) {
			throw new ApplicationException("Error buying pass for the event: "+ eventName, e);
		}
		em.close();
		this.eventName = eventName;
		numberPasses = number;
		return number;
	}

	/**
	 * Finalizes the buying of the Pass Tickets
	 * and adds a new reservation.
	 * @param numberPasses number of ticket passes the user wants do buy
	 * @param email the email of the user
	 * @return entity reference value of the reservation
	 * @throws ApplicationException
	 */
	public Triplet<String, String, Integer> numberOfPassesAndInsertMail(int numberPasses, String email)
			throws ApplicationException {
		EntityManager em = emf.createEntityManager();

		if (numberPasses > this.numberPasses) {
			throw new ApplicationException("Error, number requested is higher than the existent number");
		}

		EventCatalog ec = new EventCatalog(em);
		try {
			em.getTransaction().begin();
			ReservationCatalog rc = new ReservationCatalog(em);
			// Caso 1, registar lugares atribuidos (seguidos) como reservados
			if (ec.getEvent(eventName).getType().isSeated()) {

				Collection<Ticket> tickets = ec.getEvent(eventName).getTickets();

				int numberPassesPicked = 0;

				Collection<Ticket> reserveTickets = new ArrayList<>();

				for (Ticket t : tickets) {
					if (numberPassesPicked == numberPasses) {
						break;
					}
					if (t.getState() == TicketState.AVAILABLE) {
						t.changeState(TicketState.RESERVED);
						reserveTickets.add(t);
						numberPassesPicked ++;
					}
				}
				Triplet<String,String,Integer> triple = rc.addReservationSeated(reserveTickets, eventName, email);
				em.getTransaction().commit();
				return triple;
			} else { // Caso 2, registar o numero de lugares reservados
				
				List<Ticket> reserveTickets = ec.getEvent(eventName).getTickets();
				
				int valueTicket = reserveTickets.get(0).getValue();
				
				Triplet<String,String,Integer> triple = rc.addReservationNotSeated(numberPasses, eventName, email,valueTicket);			
				em.getTransaction().commit();
				return triple;	
			}
		}catch (Exception e) {
			if (em.getTransaction().isActive()){
				em.getTransaction().rollback();
			}
			throw new ApplicationException("Error creating reservation", e);
		}finally {
			em.close();
		}
	}
	
	/**
	 * Checks if the Event exists
	 * @param eventName name of the Event.
	 * @param em EntityManager associated
	 * @throws ApplicationException if the Event is not found.
	 */
	private void checkEventName(String eventName, EntityManager em) throws ApplicationException {
		EventCatalog ec = new EventCatalog(em);

		try {
			em.getTransaction().begin();
			ec.checkExistentEvent(eventName);
			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw new ApplicationException("Error Event not found", e);
		}
	}
	
	/**
	 * Checks if there is any pass available.
	 * @param eventName the name of the Event.
	 * @param em the EntityManager associated.
	 * @return the number of passes available.
	 * @throws ApplicationException checking the passes.
	 */
	private int checkPassAvailability(String eventName, EntityManager em) throws ApplicationException {
		EventCatalog ec = new EventCatalog(em);
		int available = 0;
		try {
			em.getTransaction().begin();
			available = ec.checkNumberPassesAvailable(eventName);
			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw new ApplicationException("Error checking Pass availability", e);
		}
		return available;
	}
}

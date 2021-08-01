package business.handlers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import business.event.EventCatalog;
import business.event.EventVenueCatalog;
import business.event.Seat;
import business.event.exceptions.EventVenueNotAvailableException;
import business.event.exceptions.SeatedVenueLargerThanEventException;
import business.ticket.PassTicket;
import business.ticket.SingleTicket;
import business.ticket.Ticket;
import business.event.Event;
import business.event.EventDate;
import business.event.EventVenue;
import facade.exceptions.ApplicationException;

/**
 *
 *
 */

public class AssignEventVenueHandler {
	
	private EntityManagerFactory emf;

	public AssignEventVenueHandler(EntityManagerFactory emf) {
		this.emf = emf;
	}

	public List<String> assignEventVenue() throws ApplicationException {

		EntityManager em = emf.createEntityManager();
		EventVenueCatalog ec = new EventVenueCatalog(em);
		try {
			em.getTransaction().begin();
			List<String> eventVenues = ec.getEventVenues();
			em.getTransaction().commit();
			return eventVenues;
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw new ApplicationException("Error getting Event Venues", e);
		} finally {
			em.close();
		}
	}

	/**
	 * @param eventName        name of the event
	 * @param eventVenue       name of the event venue
	 * @param startDate        ticket selling starting date
	 * @param ticketPrice      individual ticket price
	 * @param passTicketPrice  pass ticket price
	 * @throws ApplicationException
	 */
	public void bookEventVenue(String eventName, String eventVenue, LocalDate startDate,
			int ticketPrice, int passTicketPrice) throws ApplicationException {

		EntityManager em = emf.createEntityManager();
		EventVenueCatalog evc = new EventVenueCatalog(em);
		EventCatalog ec = new EventCatalog(em);
		try {
			em.getTransaction().begin();
			ec.checkExistentEvent(eventName);
			Event event = ec.getEvent(eventName);
			EventVenue ev = evc.getEventVenue(eventVenue);

			if (ev.isSeated() && ev.getCapacity() > event.getType().getMaxAudience())
				throw new SeatedVenueLargerThanEventException("Seated event venue has larger capacity than event max audience!");
			
			if (event.getVenue() != null)
				throw new ApplicationException("Event has a venue!");
			
			evc.checkEventVenue(eventVenue);
			
			if (event.getType().isSeated() != ev.isSeated())
				throw new ApplicationException("Event venue is not appropriate for the event!");
			
			if (event.getDates().get(0).getDate().isBefore(startDate) || startDate.isBefore(LocalDate.now())) //Passar isto para um metodo e tratar as duas exepcoes
				throw new ApplicationException("Wrong event dates!");
			
			if (ticketPrice <= 0 || passTicketPrice < 0) //Passar isto para um metodo e tratar as duas exepcoes, é preciso mais alguma validacao?
				throw new ApplicationException("Ticket price is not valid!");
			
			if (!checkEventVenueAvailability(ev, event, em))
				throw new EventVenueNotAvailableException("Event venue is not available for all event dates");
			
			//assigns event venue
			event.setVenue(ev);
			
			event.setStartSellingDate(startDate);
			
			createTickets(event, ticketPrice, passTicketPrice);

			em.getTransaction().commit();
			em.close();

		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw new ApplicationException("Error creating new Event Venue", e);
		}
	}
	
	
	private void createTickets(Event event, int ticketPrice, int passTicketPrice) {
		List<Ticket> tickets = new ArrayList<>(); 
		
		if(event.getVenue().isSeated()) {
			ArrayList<Seat> seats = new ArrayList<>(event.getVenue().getSeats());
			// seated pass tickets
			if (passTicketPrice > 0) tickets.addAll(seats.stream().map(s -> new PassTicket(event, passTicketPrice, s)).collect(Collectors.toList()));
			
			// seated single tickets for each event date
			for (EventDate ed: event.getDates()) {
				tickets.addAll(seats.stream().map(s -> new SingleTicket(event, ticketPrice, ed, s)).collect(Collectors.toList()));
			}
			event.getTickets().addAll(tickets);
		}
		
		if(!event.getVenue().isSeated()) {
			int nTickets = Math.min(event.getVenue().getCapacity(), event.getType().getMaxAudience());
			// standing pass tickets
			if (passTicketPrice > 0) tickets.addAll(Stream.generate(PassTicket::new)
					.map(pt -> new PassTicket(event, passTicketPrice)).limit(nTickets).collect(Collectors.toList()));
			
			// standing single tickets for each event date
			for (EventDate ed: event.getDates()) {
				tickets.addAll(Stream.generate(SingleTicket::new)
						.map(pt -> new SingleTicket(event, ticketPrice, ed)).limit(nTickets).collect(Collectors.toList()));
			}
			event.getTickets().addAll(tickets);
		}
	}
	
	/**
	 * Checks if event venue is available.
	 * @param eventVenue the name of the Event Venue.
	 * @param eventDates dates when the event happens.
	 * @param em the EntityManager associated.
	 * @return true if available
	 * @throws ApplicationException checking the passes.
	 */
	private boolean checkEventVenueAvailability(EventVenue venue, Event event, EntityManager em) throws ApplicationException {
		EventVenueCatalog evc = new EventVenueCatalog(em);
		// no transaction here because this is used inside a transaction in bookEventVenue
		try {
			List<EventDate> eds = evc.checkEventVenueDatesAvailable(venue, event);
			if (!eds.isEmpty()) {
				for (EventDate ed : eds) {
					for (EventDate _ed : event.getDates()) {
						if (ed.getDate().equals(_ed.getDate()) && 
								ed.getStartsAt().isBefore(_ed.getFinishesAt()) && 
								_ed.getStartsAt().isAfter(ed.getFinishesAt())) {
							return false;							
						}
					}
				}	
			}
		} catch (Exception e) {
			throw new ApplicationException("Error checking event venue availability(", e);
		}
		return true;
	}

	
	
}

package business.handlers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import business.util.Triplet;

import business.event.EventCatalog;
import business.event.exceptions.DateInvalidException;
import business.event.exceptions.NameNotUniqueException;
import business.producer.Producer;
import business.producer.ProducerCatalog;
import business.producer.exceptions.NoProducerException;
import business.producer.exceptions.ProducerWithoutCertification;
import client.CurrentDate;
import facade.exceptions.ApplicationException;
/**
 * 
 *
 */
public class CreateEventHandler {

	private EntityManagerFactory emf;

	private List<String> eventTypes;

	public CreateEventHandler(EntityManagerFactory emf) {
		this.emf = emf;
	}

	/**
	 * The Client wants to create an Event
	 * 
	 * @return the list of types of Event of the System.
	 * @throws ApplicationException getting the types of Event
	 */
	public List<String> createEvent() throws ApplicationException {

		EntityManager em = emf.createEntityManager();
		EventCatalog ec = new EventCatalog(em);
		try {
			em.getTransaction().begin();
			eventTypes = ec.getEventsTypes();
			em.getTransaction().commit();
			return eventTypes;
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw new ApplicationException("Error getting Event Types.", e);
		} finally {
			em.close();

		}

	}

	/**
	 * Creates an Event with the information required.
	 * 
	 * @param eventType      the type of event
	 * @param eventName      name of the event
	 * @param dates          a list where each element is in the following format
	 *                       [x,y,z] x - date DD-MM-YYYY y - starting hour-minutes
	 *                       hh:mm z - ending hour-minute hh:mm
	 * @param producerNumber the producer responsible of creating the event
	 * @throws ApplicationException if any information checking fails.
	 */
	public void createEventWithInfo(String eventType, String eventName,
			List<Triplet<LocalDate, LocalTime, LocalTime>> dates, int producerNumber) throws ApplicationException {

		EntityManager em = emf.createEntityManager();
		EventCatalog ec = new EventCatalog(em);
		ProducerCatalog pc = new ProducerCatalog(em);
		try {
			em.getTransaction().begin();
			checkType(eventType); //checks if the type exists
			uniqueName(eventName); //checks if an event with the name exists
			longEnoughName(eventName);
			checkDates(dates, CurrentDate.currentDateMock()); //checks if the dates/times are valid
			checkProducerNumber(producerNumber, eventType); //checks if the producer got certification for that type of event

			Producer p = pc.getProducer(producerNumber);
			ec.addEvent(p, eventType, eventName, dates);

			em.getTransaction().commit();
			em.close();

		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw new ApplicationException("Error creating new Event", e);
		}
	}

	/**
	 * Checks if the type of Event exists
	 * @param eventType the type of Event
	 * @throws ApplicationException if the type does not exist.
	 */
	private void checkType(String eventType) throws ApplicationException {

		if (!eventTypes.contains(eventType)) {
			throw new ApplicationException("That type of event does not exist.");
		}

	}
	
	/**
	 * Checks if the Event name provided already exists on the Database
	 * @param name of the Event
	 * @throws ApplicationException if the name exists
	 */
	private void uniqueName(String name) throws ApplicationException {
		EntityManager em = emf.createEntityManager();
		EventCatalog ec = new EventCatalog(em);

		try {
			em.getTransaction().begin();
			ec.checkEvent(name); // checks if another event exists with that name
			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw new NameNotUniqueException("Error, already exists one event with the name: " + name, e);
		}
		em.close();

	}

	private void longEnoughName(String name) throws ApplicationException {
		if(name.length() < 10)
			throw new ApplicationException("The name for the event is too short");
	}

	/**
	 * Checks if the dates are valid and consistent
	 * @param dates the list of dates of the event
	 * @param mock the current date for testing purposes
	 * @throws DateInvalidException if any check fails
	 */
	private void checkDates(List<Triplet<LocalDate, LocalTime, LocalTime>> dates, LocalDate mock)
			throws DateInvalidException {


		List<LocalDate> localdates = new ArrayList<>();
		for (Triplet<LocalDate, LocalTime, LocalTime> triple : dates) {
			localdates.add(triple.getValue0());

		}

		int size = localdates.size();
		if (size > 1) {

			for (int i = 0; i < size-1; i++) {
				
				
				int getDay  = localdates.get(i).getDayOfMonth();
				int nextDay = localdates.get(i).plusDays(1).getDayOfMonth();
				Month month = localdates.get(i).getMonth();
				if(getDay == 31) {
					 month = localdates.get(i).plusMonths(1).getMonth();
				}
				
				boolean days = nextDay != localdates.get(i + 1).getDayOfMonth();
				boolean months = month != localdates.get(i + 1).getMonth();
				
				if (i != size - 1 && (days || months)) {
					throw new DateInvalidException("Error not consecutive days");
				}
			}
		}
	
		checkHours(dates, mock);
	}
		
		
	/**
	 * Checks if the hours are valid and consistent
	 * @param dates the list of dates of the event
	 * @param mock the current date for testing purposes
	 * @throws DateInvalidException if any check fails
	 */
	private void checkHours(List<Triplet<LocalDate, LocalTime, LocalTime>> dates, LocalDate mock) throws DateInvalidException {

		for (Triplet<LocalDate, LocalTime, LocalTime> triple : dates) {

			/**
			 * 0 if both the date-times represent the same time instance of the day.
			 * positive integer if given date/times is later than the otherDate. negative
			 * integer if given date/times is earlier than the otherDate.
			 */
			int comparationDate = triple.getValue0().compareTo(mock);
			if (comparationDate < 0) {
				throw new DateInvalidException("Error, the date given is less than today.");

			} else if (comparationDate == 0 && 
					triple.getValue1().isAfter(triple.getValue2()) && triple.getValue2().getHour() != 0) {
				throw new DateInvalidException("Error the start time for the event is later than the ending time.");
			}
		}

	}
	
	/**
	 * Checks if the producer exists and if the producer has the certification for that type of event.
	 * @param producerNumber the number of the producer
	 * @param eventType the type of event
	 * @throws ApplicationException if any of the checks fails.
	 */
	private void checkProducerNumber(int producerNumber, String eventType) throws ApplicationException {

		EntityManager em = emf.createEntityManager();
		ProducerCatalog pc = new ProducerCatalog(em);
		try {
			em.getTransaction().begin();
			pc.getProducer(producerNumber);
			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw new NoProducerException("Error, the producer with:" + producerNumber + " does not exist.", e);
		}

		try {
			em.getTransaction().begin();
			pc.checkcertification(producerNumber, eventType);
			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw new ProducerWithoutCertification("Error this producer does not have the certification for: " + eventType + " type", e);
		}

		em.close();
	}

}

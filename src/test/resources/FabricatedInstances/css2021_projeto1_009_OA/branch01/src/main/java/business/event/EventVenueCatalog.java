package business.event;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import business.event.exceptions.NoEventFoundException;
import facade.exceptions.ApplicationException;

public class EventVenueCatalog {
	
	private EntityManager em;
	
	/**
	 * Constructs a EventVenues's catalog given a entity manager factory
	 */
	public EventVenueCatalog(EntityManager em) {
		this.em = em;
	}
	
	public EventVenue getEventVenue (String name) throws ApplicationException {
		TypedQuery<EventVenue> query = em.createNamedQuery(EventVenue.FIND_BY_NAME, EventVenue.class);
		query.setParameter(EventVenue.STRING_VENUE_NAME, name);
		try {
			return query.getSingleResult();
		} catch (PersistenceException e) {
			throw new ApplicationException("EventVenue not found.", e);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public List<String> getEventVenues() {
		TypedQuery<String> query = em.createNamedQuery(EventVenue.FIND_ALL, String.class);
		try {

			List<String> finalResult = new ArrayList<>();
			List<String> result = query.getResultList();
			
			
			for (String s : result) {
				if (s.contains(",")) {
					String[] newS = s.split(",");
					for (String put : newS) {
						if (!finalResult.contains(put)) {
							finalResult.add(put);
						}
					}

				}else if (!finalResult.contains(s)) {
					finalResult.add(s);
				}
			}
			return finalResult;
		} catch (Exception e) {
			return new ArrayList<String>();
		}
	}
	
	/**
	 * Checks if the Event Venue with the name exists
	 * @param eventName the name of the event
	 * @throws ApplicationException if the Event does not exists
	 * @see checkEvent(), it does the opposite of this method.
	 */
	public void checkEventVenue(String eventVenueName) throws ApplicationException {
		TypedQuery<String> query = em.createNamedQuery(EventVenue.FIND_BY_NAME, String.class);
		query.setParameter(EventVenue.STRING_VENUE_NAME, eventVenueName);
		try {
			if (query.getResultList().isEmpty()) {
				throw new NoEventFoundException("Event Venue" + eventVenueName + " does not exist.");
			}
		} catch (PersistenceException e) {
			throw new NoEventFoundException("Error finding Event Venue.", e);
		}
	}
	
	public List<EventDate> checkEventVenueDatesAvailable(EventVenue venue, Event event) throws ApplicationException {		
		try {
//			List<EventDate> edList = new ArrayList<>();
//			for (EventDate edate : eventDates) {
//				TypedQuery<EventDate> query = em.createNamedQuery(EventVenue.EVENTVENUE_AVAILABLE, EventDate.class);
//				query.setParameter(EventVenue.STRING_NAME, eventVenue);
//				query.setParameter(EventVenue.DATE_DATE, edate.getDate());
//				edList.addAll(query.getResultList());
//			}
//			return edList;	
			TypedQuery<EventDate> query = em.createNamedQuery(EventVenue.EVENTVENUE_AVAILABLE, EventDate.class);
			query.setParameter(EventVenue.STRING_VENUE_NAME, venue.getName());
			query.setParameter(EventVenue.STRING_EVENT_NAME, event.getName());
			return query.getResultList();
		} catch (Exception e) {
			throw new ApplicationException("Error checking dates avaliable", e);
		}
	}


}

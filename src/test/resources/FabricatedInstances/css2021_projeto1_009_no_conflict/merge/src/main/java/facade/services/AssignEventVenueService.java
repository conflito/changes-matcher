package facade.services;

import java.time.LocalDate;
import java.util.List;

import business.handlers.AssignEventVenueHandler;
import facade.exceptions.ApplicationException;

public class AssignEventVenueService {

	private AssignEventVenueHandler assignEventVenueHandler;

	public AssignEventVenueService(AssignEventVenueHandler assignEventVenueHandler) {
		this.assignEventVenueHandler = assignEventVenueHandler;
	}
	
	public List<String> assignEventVenue() throws ApplicationException {
		try {
			return this.assignEventVenueHandler.assignEventVenue();	
		}
		catch (Exception e) {
			throw new ApplicationException("Error starting assignment of event venue.", e);
		}
	}
	
	public void bookEventVenue(String eventName, String eventVenue, LocalDate startDate,
			int ticketPrice, int passTicketPrice) throws ApplicationException {
		try {
			this.assignEventVenueHandler.bookEventVenue(eventName, eventVenue, startDate, ticketPrice, passTicketPrice);
		} catch (Exception e) {
			throw new ApplicationException("Error booking event venue.", e);
		}
	}
}	


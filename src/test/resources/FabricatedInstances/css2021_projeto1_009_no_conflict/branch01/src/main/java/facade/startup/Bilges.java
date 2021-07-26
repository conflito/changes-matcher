package facade.startup;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import business.handlers.AssignEventVenueHandler;
import business.handlers.BuyPassTicketsHandler;
import business.handlers.BuySeatedTicketHandler;
import business.handlers.CreateEventHandler;
import facade.exceptions.ApplicationException;
import facade.services.AssignEventVenueService;
import facade.services.BuyPassTicketService;
import facade.services.BuySeatedTicketService;
import facade.services.CreateEventService;

public class Bilges {

	private BuyPassTicketService buyPassService;
	private CreateEventService createEventService;
	private AssignEventVenueService assignEventVenueService;
	private BuySeatedTicketService buySeatedTicketService; 
	private EntityManagerFactory emf;
	
	
	public void run() throws ApplicationException{
		try {
			this.emf = Persistence.createEntityManagerFactory("bilges-domain-model-data-mappers-jpa");
			this.createEventService = new CreateEventService(new CreateEventHandler(emf));
			this.buyPassService = new BuyPassTicketService(new BuyPassTicketsHandler(emf));
			this.assignEventVenueService = new AssignEventVenueService(new AssignEventVenueHandler(emf));
			this.buySeatedTicketService = new BuySeatedTicketService(new BuySeatedTicketHandler(emf));
		} catch (Exception e) {
			throw new ApplicationException("Error connecting database",e);
		}
	}
	
	public void stopRun() {
		// Closes the database connection
		emf.close();
	}
	
	public CreateEventService getCreateService() {
		return createEventService;
	}
	
	public BuyPassTicketService getBuyPassService() {
		return buyPassService;
	}
	
	public AssignEventVenueService getAssignEventVenueService() {
		return this.assignEventVenueService;
	}
	
	public BuySeatedTicketService getBuySeatedTicketService() {
		return this.buySeatedTicketService;
	}
}

package client;


import java.util.logging.Level;
import java.util.logging.Logger;

import facade.exceptions.ApplicationException;
import facade.startup.Bilges;
/**
 * 
 *
 * A class that demonstrates the User Cases
 * For the project Bilges
 * 
 * Note: 
 * This class is divided in 4 sub-classes
 * To demonstrate the individual logic and work of 
 * each U.C 
 */
public class SimpleClient {

	private SimpleClient() {

	}

	public static void main(String[] args) {
		Bilges app = new Bilges();

		
		try {
			app.run();
			
			System.out.println("START UC1, CREATE EVENT(Criar Evento): \n");
			SimpleClientCreateEvent sce = new SimpleClientCreateEvent(app.getCreateService());
			sce.run();
			
			System.out.println("\nSTART UC2, ASSIGNVENUE (Atribuir Instalacao): \n");
			SimpleClientAssignEventVenue scaev = new SimpleClientAssignEventVenue(app.getAssignEventVenueService());
			scaev.run();
			
			System.out.println("\nSTART UC3, BUY SEATED TICKET(Comprar Bilhetes com Escolha de Lugares): \n");
			SimpleClientBuySeatedTicket scbst = new SimpleClientBuySeatedTicket(app.getBuySeatedTicketService());
			scbst.run();
			
			System.out.println("\nSTART UC4, BUY PASS TICKET(Comprar Bilhetes-Passe): \n");
			SimpleClientBuyPassTicket scbe = new SimpleClientBuyPassTicket(app.getBuyPassService());
			scbe.run();
			
			app.stopRun();
		} catch (ApplicationException e) {
			Logger logger = Logger.getLogger(SimpleClient.class.getName());
			logger.log(Level.WARNING,String.format("Error: %s", e.getMessage()));

			if (e.getCause() != null)
				logger.log(Level.INFO,"Cause: ");
			e.printStackTrace();
		}
	}
	
}

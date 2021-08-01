package client;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import business.event.exceptions.EventVenueNotAvailableException;
import business.event.exceptions.SeatedVenueLargerThanEventException;
import facade.exceptions.ApplicationException;
import facade.services.AssignEventVenueService;

public class SimpleClientAssignEventVenue {

	private AssignEventVenueService aevService;

	protected SimpleClientAssignEventVenue(AssignEventVenueService aevService) {
		this.aevService = aevService;
	}

	protected void run() {

		Logger logger = Logger.getLogger(SimpleClient.class.getName());

		/*
		 * 7. Atribuir instalação com os seguintes dados:
		 * nome: Bye Semestre X instalação: Mini Estadio
		 * data: 1/5/2021
		 * > a capacidade da instalação, de lugares sentados, é superior ao número máximo de
		 * participantes que o evento pode ter
		 */
		System.out.println("################################################");
		System.out.println("Test 7: assign venue Mini Estadio to event Bye Semestre X");
		System.out.println("Expected: error -> seated venue capacity is larger than event max audience.");
		try {
			List<String> venues = aevService.assignEventVenue();
			//System.out.println("Venues: " + String.join(",", venues));
			aevService.bookEventVenue("Bye Semestre X", "Mini Estadio", LocalDate.of(2021, 05, 01), 1, 0);
			System.out.println("Result: failed!");
			System.exit(1);
		} catch (ApplicationException e) {
			if (e.getCause()!=null && e.getCause().getCause()!=null &&
					e.getCause().getCause().getClass().equals(SeatedVenueLargerThanEventException.class)) {
				System.out.println("Result: passed!");	
			}
			else {
				logger.log(Level.WARNING, String.format("Error: %s", e.getMessage()));

				if (e.getCause() != null)
					logger.log(Level.INFO, "Cause: ");
				e.printStackTrace();
				System.exit(1);	
			}
		}
		
		/*
		 * 8. Atribuir instalação com os seguintes dados:
		 * nome: Bye Semestre X instalação: Micro Pavilhao
		 * data: 1/5/2021
		 * preço individual: 20 preço passe:
		 */
		System.out.println("################################################");
		System.out.println("Test 8: assign venue Micro Pavilhao to event Bye Semestre X");
		System.out.println("Expected: venue is assigned to event without error");
		try {
			List<String> venues = aevService.assignEventVenue();
			//System.out.println("Venues: " + String.join(",", venues));
			aevService.bookEventVenue("Bye Semestre X", "Micro Pavilhao", LocalDate.of(2021, 05, 01), 20, 0);
			System.out.println("Result: passed!");
		} catch (ApplicationException e) {
			logger.log(Level.WARNING, String.format("Error: %s", e.getMessage()));

			if (e.getCause() != null)
				logger.log(Level.INFO, "Cause: ");
			e.printStackTrace();
			System.out.println("Result: failed!");
			System.exit(1);
		}
		
		/*
		 *9. Atribuir instalação com os seguintes dados:
		 *nome: Bye Semestre Y instalação: Micro Pavilhao
		 *data: 1/5/2021
		 *preço individual: 20 preço passe:
		 *>a instalação já está ocupada 
		 */
		System.out.println("################################################");
		System.out.println("Test 9: assign venue Micro Pavilhao to event Bye Semestre Y");
		System.out.println("Expected: error -> venue is already booked");
		try {
			List<String> venues = aevService.assignEventVenue();
			//System.out.println("Venues: " + String.join(",", venues));
			aevService.bookEventVenue("Bye Semestre Y", "Micro Pavilhao", LocalDate.of(2021, 05, 01), 20, 0);
			System.out.println("Result: failed!");
		} catch (ApplicationException e) {
			if (e.getCause()!=null && e.getCause().getCause()!=null &&
					e.getCause().getCause().getClass().equals(EventVenueNotAvailableException.class)) {
				System.out.println("Result: passed!");	
			}
			else {
				logger.log(Level.WARNING, String.format("Error: %s", e.getMessage()));

				if (e.getCause() != null)
					logger.log(Level.INFO, "Cause: ");
				e.printStackTrace();
				System.exit(1);	
			}
		}
		
		/*
		 * 10. Atribuir instalação com os seguintes dados:
		 * nome: Open dos Exames instalação: Mini Estadio
		 * data: 1/5/2021
		 * preço individual: 20 preço passe: 30
		 */
		System.out.println("################################################");
		System.out.println("Test 10: assign venue Mini Estadio to event Open dos Exames");
		System.out.println("Expected: venue is assigned to event without error");
		try {
			List<String> venues = aevService.assignEventVenue();
			//System.out.println("Venues: " + String.join(",", venues));
			aevService.bookEventVenue("Open dos Exames", "Mini Estadio", LocalDate.of(2021, 05, 01), 20, 30);
			System.out.println("Result: passed!");
		} catch (ApplicationException e) {
			logger.log(Level.WARNING, String.format("Error: %s", e.getMessage()));

			if (e.getCause() != null)
				logger.log(Level.INFO, "Cause: ");
			e.printStackTrace();
			System.out.println("Result: failed!");
			System.exit(1);
		}
		
		/*
		 * 11. Atribuir instalação com os seguintes dados:
		 * nome: Festival Estou de Ferias instalação: Pequeno Relvado
		 * data: 1/5/2021
		 * preço individual: 15 preço passe:
		 */
		System.out.println("################################################");
		System.out.println("Test 11: assign venue Pequeno Relvado to event Festival Estou de Ferias");
		System.out.println("Expected: venue is assigned to event without error");
		try {
			List<String> venues = aevService.assignEventVenue();
			//System.out.println("Venues: " + String.join(",", venues));
			aevService.bookEventVenue("Festival Estou de Ferias", "Pequeno Relvado", LocalDate.of(2021, 05, 01), 15, 0);
			System.out.println("Result: passed!");
		} catch (ApplicationException e) {
			logger.log(Level.WARNING, String.format("Error: %s", e.getMessage()));

			if (e.getCause() != null)
				logger.log(Level.INFO, "Cause: ");
			e.printStackTrace();
			System.out.println("Result: failed!");
			System.exit(1);
		}
		
	}
}

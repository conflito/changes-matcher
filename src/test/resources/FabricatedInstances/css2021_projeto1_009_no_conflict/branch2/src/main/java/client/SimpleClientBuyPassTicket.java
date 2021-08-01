package client;

import java.util.logging.Level;
import java.util.logging.Logger;

import business.util.Triplet;

import business.ticket.exceptions.EventWithoutPassTicketException;
import facade.exceptions.ApplicationException;
import facade.services.BuyPassTicketService;

public class SimpleClientBuyPassTicket {

	private BuyPassTicketService bpt;

	protected SimpleClientBuyPassTicket(BuyPassTicketService bpt) {
		this.bpt = bpt;
	}

	private static final String SEPARATOR = "################################################";
	private static final String RESULT_PASSED = "Result: passed!";
	private static final String RESULT_ERROR = "Result: failed!";
	private static final String CAUSE = "Cause: ";

	protected void run() {

		Logger logger = Logger.getLogger(SimpleClient.class.getName());

		// Caso 17
		System.out.println(SEPARATOR);
		System.out.println("Test 17: Reservation created with 2 tickets with the email u4@gmail.com");
		System.out.println("Expected; Reservation created with 2 tickets reserved"
				+ " and return of Entity, Reference and Price ");
		try {
			System.out.println("Pass Tickets Available: " + bpt.buyPass("Open dos Exames"));

			Triplet<String, String, Integer> triple = bpt.numberOfPassesAndInsertMail(2, "u4@gmail.com");

			System.out.println(String.format("Entity: %s | Reference: %s | Price: %d", triple.getValue0(),
					triple.getValue1(), triple.getValue2()));
			System.out.println(RESULT_PASSED);
		} catch (ApplicationException e) {
			logger.log(Level.WARNING, String.format("Error: %s", e.getMessage()));

			if (e.getCause() != null) {
				logger.log(Level.INFO, CAUSE);
				e.printStackTrace();
				System.out.println(RESULT_ERROR);
				System.exit(1);
			}
		}

		// Caso 18
		System.out.println(SEPARATOR);
		System.out.println("Test 18: Reservation created with 3 tickets with the email u5@gmail.com");
		System.out.println("Expected; Reservation created with 3 tickets reserved"
				+ " and return of Entity, Reference and Price ");
		try {
			System.out.println("Pass Tickets Available: " + bpt.buyPass("Open dos Exames"));

			Triplet<String, String, Integer> triple = bpt.numberOfPassesAndInsertMail(3, "u5@gmail.com");

			System.out.println(String.format("Entity: %s | Reference: %s | Price: %d", triple.getValue0(),
					triple.getValue1(), triple.getValue2()));
			System.out.println(RESULT_PASSED);
		} catch (ApplicationException e) {
			logger.log(Level.WARNING, String.format("Error: %s", e.getMessage()));

			if (e.getCause() != null) {
				logger.log(Level.INFO, CAUSE);
				e.printStackTrace();
				System.out.println(RESULT_ERROR);
				System.exit(1);
			}
		}
		// Caso 19
		System.out.println(SEPARATOR);
		System.out.println("Test 19: Reservation created with 7 tickets with the email u4@gmail.com");
		System.out.println("Expected; Error, this Event hasn't got any passes.");
		try {
			System.out.println("Pass Tickets Available: " + bpt.buyPass("Festival Estou de Ferias"));

			Triplet<String, String, Integer> triple = bpt.numberOfPassesAndInsertMail(7, "u6@gmail.com");

			System.out.println(String.format("Entity: %s | Reference: %s | Price: %d", triple.getValue0(),
					triple.getValue1(), triple.getValue2()));
			System.out.println(RESULT_ERROR);
		} catch (ApplicationException e) {
			if (e.getCause() != null && e.getCause().getCause() != null && e.getCause().getCause().getCause() != null
					&& e.getCause().getCause().getCause().getClass().equals(EventWithoutPassTicketException.class)) {
				System.out.println(RESULT_PASSED);
				
			} else if (e.getCause() != null) {
				logger.log(Level.INFO, CAUSE);
				System.exit(1);
				e.printStackTrace();
			}
		}
		// Caso 20
		System.out.println(SEPARATOR);
		System.out.println("Test 20: Reservation created with 2 tickets with the email u4@gmail.com");
		System.out.println("Expected; Error, this Event hasn't got any passes.");
		try {
			System.out.println("Pass Tickets Available: " + bpt.buyPass("Festival Estou de Ferias"));

			Triplet<String, String, Integer> triple = bpt.numberOfPassesAndInsertMail(4, "u7@gmail.com");

			System.out.println(String.format("Entity: %s | Reference: %s | Price: %d", triple.getValue0(),
					triple.getValue1(), triple.getValue2()));
			System.out.println(RESULT_ERROR);
		} catch (ApplicationException e) {
			if (e.getCause() != null && e.getCause().getCause() != null && e.getCause().getCause().getCause() != null
					&& e.getCause().getCause().getCause().getClass().equals(EventWithoutPassTicketException.class)) {
				System.out.println(RESULT_PASSED);
				
			} else if (e.getCause() != null) {
				logger.log(Level.INFO, CAUSE);
				System.exit(1);
				e.printStackTrace();
			}
		}
		System.out.println(SEPARATOR);

	}

}

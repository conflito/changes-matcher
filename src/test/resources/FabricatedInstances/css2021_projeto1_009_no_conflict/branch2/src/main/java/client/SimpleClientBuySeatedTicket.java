package client;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import business.event.exceptions.EventIsNotSeatedException;
import business.ticket.exceptions.TicketNotAvailableException;
import facade.dtos.EventDateDto;
import facade.dtos.SeatDto;
import facade.exceptions.ApplicationException;
import facade.services.BuySeatedTicketService;

public class SimpleClientBuySeatedTicket {

	private BuySeatedTicketService bstService;

	protected SimpleClientBuySeatedTicket(BuySeatedTicketService bstService) {
		this.bstService = bstService;
	}

	protected void run() {

		String indent = "    ";
		Logger logger = Logger.getLogger(SimpleClient.class.getName());

		/*
		 * 12. Comprar bilhete com escolha de lugares para o evento Bye Semestre X. Escolhe os
		 * primeiros 3 lugares fornecidos (que devem ser A-1,A-2,B-1) e u1@gmail.com.
		 */
		System.out.println("################################################");
		System.out.println("Test 12: buy ticket, choosing a seat, for event Bye Semestre X");
		System.out.println("Expected: tickets are reserved without error");
		try {
			String eventName = "Bye Semestre X";
			
			List<EventDateDto> dates = bstService.getAvailableSeatedEventDates(eventName);
			System.out.println("");
			System.out.println("Event: " + eventName);
			System.out.println("Available dates: ");
			for (EventDateDto d: dates) {
				System.out.println(indent + "On: " + d.getDate() + " from: " + d.getStartsAt() + " to: " + d.getFinishesAt());
			}
			
			List<SeatDto> availableSeats = bstService.pickEventDate(eventName, dates.get(0).getDate());
			System.out.println("");
			System.out.println("Available seats for event: " + eventName + " on " + dates.get(0).getDate());
			for (SeatDto s: availableSeats) {
				System.out.println(String.format("%sRow: %2s Seat: %2d", indent, s.getRowLetters(), s.getSeatNumber()));
			}
			
			List<SeatDto> chosenSeats = availableSeats.stream().limit(3).collect(Collectors.toList());
			String result = bstService.reserveSingleTicketsForSeatedEventDate(eventName, dates.get(0).getDate(), chosenSeats, "u1@gmail.com");
			System.out.println("");
			System.out.println("Reservation created for:");
			System.out.println(indent + "Event: " +eventName);
			System.out.println(indent + "On:    " + dates.get(0).getDate());
			System.out.println(indent + "From:  " + dates.get(0).getStartsAt());
			System.out.println(indent + "To:    " + dates.get(0).getFinishesAt());
			System.out.println(indent + "With seats: ");
			for (SeatDto s: chosenSeats) {
				System.out.println(String.format("%s%sRow: %2s Seat: %2d", indent, indent, s.getRowLetters(), s.getSeatNumber()));
			}
			System.out.println(indent + result);
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
		 * 13. Comprar bilhete com escolha de lugares para o evento Bye Semestre X. Escolhe o B-
		 * 1 e u2@gmail.com.
		 * >o bilhete não está disponível (está reservado)
		 */
		System.out.println("################################################");
		System.out.println("Test 13: buy seated ticket for event Bye Semestre X with seat B-1");
		System.out.println("Expected: error -> ticket is not available");
		try {
			String eventName = "Bye Semestre X";
			
			List<EventDateDto> dates = bstService.getAvailableSeatedEventDates(eventName);
			System.out.println("");
			System.out.println("Event: " + eventName);
			System.out.println("Available dates: ");
			for (EventDateDto d: dates) {
				System.out.println(indent + "On: " + d.getDate() + " from: " + d.getStartsAt() + " to: " + d.getFinishesAt());
			}
			
			List<SeatDto> availableSeats = bstService.pickEventDate(eventName, dates.get(0).getDate());
			System.out.println("");
			System.out.println("Available seats for event: " + eventName + " on " + dates.get(0).getDate());
			for (SeatDto s: availableSeats) {
				System.out.println(String.format("%sRow: %2s Seat: %2d", indent, s.getRowLetters(), s.getSeatNumber()));
			}
			
			System.out.println("WARNING: as you can see, seat B-1 is not available!");
			
			List<SeatDto> chosenSeats = Arrays.asList(new SeatDto("B", 1));
			String result = bstService.reserveSingleTicketsForSeatedEventDate(eventName, dates.get(0).getDate(), chosenSeats, "u1@gmail.com");
			System.out.println("Result: failed!");
			System.exit(1);
		} catch (ApplicationException e) {
			if (e.getCause()!=null && e.getCause().getCause()!=null &&
					e.getCause().getCause().getClass().equals(TicketNotAvailableException.class)) {
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
		 * 14. Comprar bilhete com escolha de lugares para o evento Bye Semestre X. Escolhe o
		 * primeiro lugar fornecido (que deve ser B-2) e u2@gmail.com.
		 */
		System.out.println("################################################");
		System.out.println("Test 14: buy ticket, choosing a seat, for event Bye Semestre X with first seat available B-2");
		System.out.println("Expected: ticket is reserved without error");
		try {
			String eventName = "Bye Semestre X";
			
			List<EventDateDto> dates = bstService.getAvailableSeatedEventDates(eventName);
			System.out.println("");
			System.out.println("Event: " + eventName);
			System.out.println("Available dates: ");
			for (EventDateDto d: dates) {
				System.out.println(indent + "On: " + d.getDate() + " from: " + d.getStartsAt() + " to: " + d.getFinishesAt());
			}
			
			List<SeatDto> availableSeats = bstService.pickEventDate(eventName, dates.get(0).getDate());
			System.out.println("");
			System.out.println("Available seats for event: " + eventName + " on " + dates.get(0).getDate());
			for (SeatDto s: availableSeats) {
				System.out.println(String.format("%sRow: %2s Seat: %2d", indent, s.getRowLetters(), s.getSeatNumber()));
			}
			
			List<SeatDto> chosenSeats = availableSeats.stream().limit(1).collect(Collectors.toList());
			String result = bstService.reserveSingleTicketsForSeatedEventDate(eventName, dates.get(0).getDate(), chosenSeats, "u2@gmail.com");
			System.out.println("");
			System.out.println("Reservation created for:");
			System.out.println(indent + "Event: " +eventName);
			System.out.println(indent + "On:    " + dates.get(0).getDate());
			System.out.println(indent + "From:  " + dates.get(0).getStartsAt());
			System.out.println(indent + "To:    " + dates.get(0).getFinishesAt());
			System.out.println(indent + "With seats: ");
			for (SeatDto s: chosenSeats) {
				System.out.println(String.format("%s%sRow: %2s Seat: %2d", indent, indent, s.getRowLetters(), s.getSeatNumber()));
			}
			System.out.println(indent + result);
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
		 * 15. Comprar bilhete com escolha de lugares para o evento Festival Estou de Ferias.
		 * >o evento não tem lugares marcados
		 */
		System.out.println("################################################");
		System.out.println("Test 15: buy seated ticket for event Festival Estou de Ferias");
		System.out.println("Expected: error -> event is not seated");
		try {
			String eventName = "Festival Estou de Ferias";
			
			List<EventDateDto> dates = bstService.getAvailableSeatedEventDates(eventName);
			System.out.println("Result: failed!");
			System.exit(1);
		} catch (ApplicationException e) {
			if (e.getCause()!=null && e.getCause().getCause()!=null &&
					e.getCause().getCause().getClass().equals(EventIsNotSeatedException.class)) {
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
		 * 16. Comprar bilhete com escolha de lugares para o evento Open dos Exames. Escolhe a
		 * primeira data e os primeiros 2 lugares fornecidos (que devem ser A-1,A-2) e
		 * u3@gmail.com.
		 */
		System.out.println("################################################");
		System.out.println("Test 16: buy seated tickets for event Open dos Exames. For first date and fisrt two available seats.");
		System.out.println("Expected: tickets are reserved without error");
		try {
			String eventName = "Open dos Exames";
			
			List<EventDateDto> dates = bstService.getAvailableSeatedEventDates(eventName);
			System.out.println("");
			System.out.println("Event: " + eventName);
			System.out.println("Available dates: ");
			for (EventDateDto d: dates) {
				System.out.println(indent + "On: " + d.getDate() + " from: " + d.getStartsAt() + " to: " + d.getFinishesAt());
			}
			
			List<SeatDto> availableSeats = bstService.pickEventDate(eventName, dates.get(0).getDate());
			System.out.println("");
			System.out.println("Available seats for event: " + eventName + " on " + dates.get(0).getDate());
			for (SeatDto s: availableSeats) {
				System.out.println(String.format("%sRow: %2s Seat: %2d", indent, s.getRowLetters(), s.getSeatNumber()));
			}
			
			List<SeatDto> chosenSeats = availableSeats.stream().limit(2).collect(Collectors.toList());
			String result = bstService.reserveSingleTicketsForSeatedEventDate(eventName, dates.get(0).getDate(), chosenSeats, "u3@gmail.com");
			System.out.println("");
			System.out.println("Reservation created for:");
			System.out.println(indent + "Event: " +eventName);
			System.out.println(indent + "On:    " + dates.get(0).getDate());
			System.out.println(indent + "From:  " + dates.get(0).getStartsAt());
			System.out.println(indent + "To:    " + dates.get(0).getFinishesAt());
			System.out.println(indent + "With seats: ");
			for (SeatDto s: chosenSeats) {
				System.out.println(String.format("%s%sRow: %2s Seat: %2d", indent, indent, s.getRowLetters(), s.getSeatNumber()));
			}
			System.out.println(indent + result);
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

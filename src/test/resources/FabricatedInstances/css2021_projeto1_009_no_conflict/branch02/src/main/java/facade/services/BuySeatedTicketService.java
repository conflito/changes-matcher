package facade.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import business.util.Pair;

import business.handlers.BuySeatedTicketHandler;
import facade.dtos.EventDateDto;
import facade.dtos.SeatDto;
import facade.exceptions.ApplicationException;

public class BuySeatedTicketService {

	private BuySeatedTicketHandler buySeatedTicketHandler;

	public BuySeatedTicketService(BuySeatedTicketHandler buySeatedTicketHandler) {
		this.buySeatedTicketHandler = buySeatedTicketHandler;
	}
	
	public List<EventDateDto> getAvailableSeatedEventDates(String eventName) throws ApplicationException {
		try {
			return this.buySeatedTicketHandler.getAvailableSeatedEventDates(eventName).stream()
					.map(EventDateDto::fromEventDate).collect(Collectors.toList());
		}
		catch (Exception e) {
			throw new ApplicationException("Error getting available dates for event.", e);
		}
	}
	
	public List<SeatDto> pickEventDate(String eventName, LocalDate date) throws ApplicationException {
		try {
			return this.buySeatedTicketHandler.pickEventDate(eventName, date).stream()
					.map(SeatDto::fromSeat).collect(Collectors.toList());
		} catch (Exception e) {
			throw new ApplicationException("Error picking event date.", e);
		}
	}
	
	public String reserveSingleTicketsForSeatedEventDate(String eventName, LocalDate date, List<SeatDto> seats, String customerEMail) throws ApplicationException {
		try {
			List<Pair<String, Integer>> seatLabels = seats.stream()
					.map(s -> new Pair<String, Integer>(s.getRowLetters(), s.getSeatNumber()))
					.collect(Collectors.toList()); 
			return this.buySeatedTicketHandler.reserveSingleTicketsForSeatedEventDate(eventName, date, seatLabels, customerEMail);
		} catch (Exception e) {
			throw new ApplicationException("Error reserving tickets for seated event", e);
		}		
	}
}	


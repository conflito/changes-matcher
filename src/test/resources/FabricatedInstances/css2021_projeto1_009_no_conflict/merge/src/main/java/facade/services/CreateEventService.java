package facade.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import business.util.Triplet;

import business.handlers.CreateEventHandler;
import facade.exceptions.ApplicationException;

public class CreateEventService {

	private CreateEventHandler createEventHandler;

	public CreateEventService(CreateEventHandler createEventHandler) {
		this.createEventHandler = createEventHandler;
	}
	
	
	
	public List<String> createEvent() throws ApplicationException{
		return createEventHandler.createEvent();
	}
	
	
	public void createEventWithInfo(String eventType, String name, List<Triplet<LocalDate, LocalTime, LocalTime>> dates,
			int producerNumber) throws ApplicationException {
		createEventHandler.createEventWithInfo(eventType, name, dates, producerNumber);
	}
}	

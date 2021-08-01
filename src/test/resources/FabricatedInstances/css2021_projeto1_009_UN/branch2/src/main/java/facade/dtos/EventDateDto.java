/**
 * 
 */
package facade.dtos;

import java.time.LocalDate;
import java.time.LocalTime;

import business.event.EventDate;

/**
 *
 */
public class EventDateDto {

	private LocalDate date;
	private LocalTime startsAt;
	private LocalTime finishesAt;

	public EventDateDto() {
	}
	
	public EventDateDto(LocalDate date, LocalTime startsAt, LocalTime finishesAt) {
		this.date = date;
		this.startsAt = startsAt;
		this.finishesAt = finishesAt;
	}
	
	public static EventDateDto fromEventDate(EventDate eventDate) {
		return new EventDateDto(eventDate.getDate(), eventDate.getStartsAt(), eventDate.getFinishesAt());
	}

	public void mapFromEventDate(EventDate eventDate) {
		this.date = eventDate.getDate();
		this.startsAt = eventDate.getStartsAt();
		this.finishesAt = eventDate.getFinishesAt();
	}
	
	public void mapToEventDate(EventDate eventDate) {
		eventDate.setDate(this.date);
		eventDate.setStartsAt(this.startsAt);
		eventDate.setFinishesAt(this.finishesAt);
	}
	
	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getStartsAt() {
		return startsAt;
	}

	public void setStartsAt(LocalTime startsAt) {
		this.startsAt = startsAt;
	}

	public LocalTime getFinishesAt() {
		return finishesAt;
	}

	public void setFinishesAt(LocalTime finishesAt) {
		this.finishesAt = finishesAt;
	}
}


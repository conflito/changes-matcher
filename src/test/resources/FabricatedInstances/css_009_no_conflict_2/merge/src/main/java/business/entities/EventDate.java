/**
 * 
 */
package business.entities;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author rui.fartaria
 *
 */
@Entity
public class EventDate {
	
	/**
	 * Primary key for JPA
	 */
	@Id @GeneratedValue private int id;
	
	/**
	 * 
	 */
	@Temporal(TemporalType.DATE)
	private Date date;
	
	/**
	 * 
	 */
	@Temporal(TemporalType.TIME)
	private Date startsAt;
	
	/**
	 * 
	 */
	@Temporal(TemporalType.TIME)
	private Date finishesAt;
	
	/**
	 * 
	 */
	@ManyToOne @JoinColumn(nullable = false)
	private Event event;
	
	
	// constructor required by JPA
	public EventDate() {
	}
	
	public EventDate(Date eventDate, Date startsAt, Date finishesAt) {
		this.date = eventDate;
		this.startsAt = startsAt;
		this.finishesAt = finishesAt;
	}
	
	public Date getDate() {
		return date;
	}

	public Date getStartsAt() {
		return startsAt;
	}

	public Date getFinishesAt() {
		return finishesAt;
	}

	public Event getEvent() {
		return event;
	}

	public boolean hasValidDates() {
		Calendar cEvent = Calendar.getInstance();
		Calendar cBegin = Calendar.getInstance();
		Calendar cEnd = Calendar.getInstance();
		
		cEvent.setTime(this.date);
		cBegin.setTime(this.startsAt);
		cEnd.setTime(this.finishesAt);
		
		if (cEvent.get(Calendar.YEAR) != cBegin.get(Calendar.YEAR) ||
			cEvent.get(Calendar.MONTH) != cBegin.get(Calendar.MONTH) ||
			cEvent.get(Calendar.DAY_OF_MONTH) != cBegin.get(Calendar.DAY_OF_MONTH) ||
			cEnd.before(cBegin) ||
			TimeUnit.HOURS.convert(this.finishesAt.getTime()-this.startsAt.getTime(), TimeUnit.MILLISECONDS) > 24) {
			return false;
		}
		
		return true;
	}
}


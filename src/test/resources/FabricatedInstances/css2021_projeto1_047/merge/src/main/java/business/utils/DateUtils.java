package business.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * An utility class with date utilities.
 *
 */
public class DateUtils {
	
	private static SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private static Calendar calendar = new GregorianCalendar();
	
	/**
	 * An utility class should not have public constructors
	 */
	private DateUtils() {
	}

	/**
	 * Converts a string with the previously specified format to the respective date
	 * @param date A string representing the date
	 * @return A string with the previously specified format to the respective date
	 * @throws InvalidDateFormatException When the input is not in the expected format
	 */
	public static Date parse(String date) throws InvalidDateFormatException {
		try {
			return parser.parse(date);
		} catch (ParseException e) {
			throw new InvalidDateFormatException("The input was not in the expected format.");
		}
	}
	
	/**
	 * For test purposes only. 
	 * 
	 * Converts a string with the previously specified format to the respective date
	 * @param date A string representing the date
	 * @return A string with the previously specified format to the respective date
	 */
	public static Date fakeParse(String date) {
		try {
			return parser.parse(date);
		} catch (ParseException e) {
			// swallow exception and returns today
			e.printStackTrace();
			return new Date();
		}
	}

	
	/**
	 * Converts the date component of a Date to LocalDate
	 * @param date The Date to convert
	 * @return The corresponding LocalDate
	 */
	public static LocalDate toLocalDate(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
	/**
	 * Converts the LocalDate to a Date
	 * WARNING: Note that the time component of the resulting Date is random.
	 * @param date The LocalDate to convert
	 * @return The corresponding Date
	 */
	public static Date toDate(LocalDate date) {
		return Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}
	
	/**
	 * Converts the time component of a Date to LocalTime
	 * @param date The Date to convert
	 * @return The corresponding LocalTime
	 */
	public static LocalTime toLocalTime(Date date) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault()).toLocalTime();
	}
	
	/**
	 * Converts the LocalTime to a Date
	 * WARNING: Note that the date component of the resulting Date is random.
	 * @param date The LocalTime to convert
	 * @return The corresponding Date of the given date
	 */
	public static Date toDate(LocalTime time) {
		return Date.from(time.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * Calculates number of minutes between two dates
	 * @param start The start date
	 * @param end The end date
	 * @return The number of minutes between these dates
	 */
	public static long minutesBetween(Date start, Date end) {
		long interval = end.getTime() - start.getTime();
		return interval / 60000;
	}
	
	/**
	 * Calculate number of hours between two dates
	 * @param start The start date
	 * @param end The end date
	 * @return The number of hours between these dates
	 */
	public static long hoursBetween(Date start, Date end) {
		return minutesBetween(start, end) / 60;
	}

	/**
	 * Checks if two dates (with hours) have the same date component
	 * @param start The start date
	 * @param end The end date
	 * @return True if both dates are in the same day
	 */
	public static boolean isSameDay(Date start, Date end) {
		return toLocalDate(start).equals(toLocalDate(end));
	}
	
	/**
	 * ATTENTION! This method is a stub method
	 * Checks if the given date is a holiday
	 * @param date The given date
	 * @return True if date is a holiday, false otherwise
	 */
	public static boolean isHoliday(LocalDate date) {
		return false;
	}
	
	/**
	 * Checks if given date is a Saturday or Sunday
	 * @param date The given date
	 * @return True if is a weekend day, false otherwise
	 */
	public static boolean isWeekend(LocalDate date) {
		return getDayOfWeek(date) == Calendar.SUNDAY || getDayOfWeek(date) == Calendar.SATURDAY;
	}

	/**
	 * Returns a number corresponding to the given date's week day
	 * @param date The given date
	 * @return A number corresponding to the given date's week day
	 */
	private static int getDayOfWeek(LocalDate date) {
		calendar.clear();
		calendar.set(date.getYear(), date.getMonthValue() - 1 , date.getDayOfMonth());
		return calendar.get(Calendar.DAY_OF_WEEK);
	}
	
	
}


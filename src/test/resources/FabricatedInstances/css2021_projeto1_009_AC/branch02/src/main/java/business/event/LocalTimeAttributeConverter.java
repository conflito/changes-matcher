package business.event;

import java.sql.Time;
import java.time.LocalTime;

import javax.persistence.AttributeConverter;

import javax.persistence.Converter;

/**
 *
 * Converter Class to enable conversion between LocalTime to Time
 */
@Converter(autoApply = true)
public class LocalTimeAttributeConverter implements AttributeConverter<LocalTime,Time> {

	@Override
	public Time convertToDatabaseColumn(LocalTime locTime) {
		return locTime == null ? null : Time.valueOf(locTime);
	}

	@Override
	public LocalTime convertToEntityAttribute(Time sqlTime) {
		return sqlTime == null ? null : sqlTime.toLocalTime();
	}

}

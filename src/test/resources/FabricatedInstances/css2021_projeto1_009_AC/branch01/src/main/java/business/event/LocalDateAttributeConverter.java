package business.event;


import java.sql.Date;
import java.time.LocalDate;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *
 * Converter Class to enable conversion between LocalDate to Date
 */
@Converter(autoApply = true)
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, Date> {

	@Override
	public Date convertToDatabaseColumn(LocalDate locDate) {
	return locDate == null ? null : Date.valueOf(locDate);
	}

	@Override
	public LocalDate convertToEntityAttribute(Date sqlDate) {
		return sqlDate == null ? null : sqlDate.toLocalDate();

	}

}

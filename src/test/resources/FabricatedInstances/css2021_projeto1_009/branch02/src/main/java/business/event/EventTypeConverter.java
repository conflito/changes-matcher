package business.event;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.*;
@Converter(autoApply = true)
public class EventTypeConverter implements AttributeConverter<EventType, String> {

	@Override
	public String convertToDatabaseColumn(EventType arg0) {
		if(arg0 == null) {
			return null;
		}
		return arg0.getName();
	}

	@Override
	public EventType convertToEntityAttribute(String arg0) {
	       if (arg0 == null) {
	            return null;
	        }

	        return Stream.of(EventType.values())
	          .filter(c -> c.getName().equals(arg0))
	          .findFirst()
	          .orElseThrow(IllegalArgumentException::new);
	    }
	}


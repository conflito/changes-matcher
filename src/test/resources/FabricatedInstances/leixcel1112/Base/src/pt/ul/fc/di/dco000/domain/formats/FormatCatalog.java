package pt.ul.fc.di.dco000.domain.formats;

import java.util.HashMap;
import java.util.Map;

public class FormatCatalog {

	private Map<String,Format> formats;
	
	public FormatCatalog(){
		formats = new HashMap<String,Format>();
	}

	public Format getFormat(String name) {
			return formats.get(name);
	}

	public void put(Format f) {
		formats.put(f.getName(),f);
	}
	
	@Override
	public String toString() {
		return formats.keySet().toString();
	}

}

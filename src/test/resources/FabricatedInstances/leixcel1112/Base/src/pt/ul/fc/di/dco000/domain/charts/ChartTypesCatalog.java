package pt.ul.fc.di.dco000.domain.charts;

import java.util.HashMap;
import java.util.Map;

public class ChartTypesCatalog {

private Map<String,Class<? extends IChart>> types;
	
	public ChartTypesCatalog(){
		types = new HashMap<String,Class<? extends IChart>>();
	}

	public Class<? extends IChart> get(String name) {
		return types.get(name);
	}

	public void put(String string, Class<? extends IChart> class1) {
		types.put(string,class1);
	}
	
}

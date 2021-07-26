package pt.ul.fc.di.dco000.domain;

import java.util.HashMap;
import java.util.Map;

import pt.ul.fc.di.dco000.domain.expressions.Addition;
import pt.ul.fc.di.dco000.domain.expressions.FunctionApplication;



public enum FunctionCatalog {

	INSTANCE;

	private Map<String,Function> functions;
	
	private FunctionCatalog(){
		functions = new HashMap<String,Function>();
		loadDefaultFunctions();
	}

	private void loadDefaultFunctions() {
		Function f = new Function("SUM", Addition.class);
		functions.put("SUM",f);
	}

	public Class<? extends FunctionApplication> getFunctionApplication(String name) {
		return functions.get(name).getFunctionApplication();
	}

	public boolean contains(String name) {
		return functions.containsKey(name);
	}

	@SuppressWarnings("unchecked")
	public boolean insert(String nameFunction, String nameClass) {
		try {
			Class<? extends FunctionApplication> c = (Class<? extends FunctionApplication>) Class.forName(nameClass);
			nameFunction = nameFunction.toUpperCase();
			Function f = new Function(nameFunction,c);
			functions.put(nameFunction,f);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
}

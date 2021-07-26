package pt.ul.fc.di.dco000.services.parser;

import java.util.Locale;

/**
 * Represents a function invoked in a formula parsed for the user's input for a
 * cell's content.
 * 
 * @author jcraveiro
 * 
 * @version $Revision: 1.0 $
 */
public class Function {

	/**
	 * The string of characters admissible in a function's name
	 */
	static final String ADMISSIBLE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ_";

	/**
	 * This function's name
	 */
	private String name;


	/**
	 * Creates a new object representing an invoked function
	 * @param name The function's name
	 */
	private Function(String name) {
		this.name = name;
	}

	/**
	 * Tries to parse a function name from the provided string. Of course the
	 * parses does not know which functions exist, it just checks if it's a
	 * syntactically valid name.
	 * @param name The putative function name
	
	
	 * @return The parsed function
	 * @throws CellParseException If the provided function name is not valid 
	 */
	public static Function parseFunction(String name) throws CellParseException {
		name = name.toUpperCase(Locale.ENGLISH);
		for (int i = 0; i < name.length(); i++) {
			if (ADMISSIBLE.indexOf(name.charAt(i)) < 0)
				throw new CellParseException();
		}
		return new Function(name);
	}

	/**
	 * Gets this function's name
	
	 * @return This function's name */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns a string representation of the object.	
	 * @return a string representation of the object.
	 */
	public String toString() {
		return super.toString();
	}

}

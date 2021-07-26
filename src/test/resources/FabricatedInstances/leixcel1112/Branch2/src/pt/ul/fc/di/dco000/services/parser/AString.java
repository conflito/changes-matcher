package pt.ul.fc.di.dco000.services.parser;

/**
 * Represents a string (appearing as a function argument) parsed from the user's
 * input for a cell's content.
 * 
 * @author jcraveiro
 * 
 * @version $Revision: 1.0 $
 */
public class AString extends Value {

	/**
	 * The string to which this objects corresponds
	 */
	private String string;

	/**
	 * Creates a new object representing a quote-delimited string.
	 * The quotes are not stored, though.
	 * @param s
	 */
	private AString(String s) {
		string = s;
	}

	/**
	 * Tries to parse a quote-delimited string from the provided string. A
	 * string is a sequence of characters delimited by quotation marks at both
	 * ends, and containing any character but quotation marks in between.
	 * 
	 * @param s
	 *            The string to process
	 * @return The corresponding string object, if the parse was successful
	 * @throws CellParseException
	 *             If the parse was unsuccessful (i.e. the string does not
	 *             contain a valid quote-delimited string)
	 */
	protected static AString parseAString(String s) throws CellParseException {

		s = CellParser.removeSurroundingSpaces(s);

		if (s.charAt(0) == '\"' && s.charAt(s.length() - 1) == '\"') {
			String s2 = s.substring(1, s.length() - 1);
			if (s2.indexOf('\"') != -1)
				throw new CellParseException();
			return new AString(s2);
		} else {
			throw new CellParseException();
		}
	}

	/**
	 * Gets this object's string (without the delimiting quotation marks)
	
	 * @return This object's string (without the delimiting quotation marks) */
	public String getString() {
		return string;
	}

	/**
	 * Method accept.
	 * @param visitor IParserVisitor
	 * @return Object
	 */
    public <T> T  accept(IParserVisitor<T> visitor) {
        return visitor.visit(this);
    }
	
	/**
	 * Returns a string representation of the object.
	 * @return a string representation of the object. 
	 */
	public String toString() {
		return super.toString();
	}
}

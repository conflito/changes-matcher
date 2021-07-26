package pt.ul.fc.di.dco000.services.parser;

/**
 * Represents a number parsed from the user's input for a cell's content
 * 
 * @author jcraveiro
 * @version $Revision: 1.0 $
 */
public class ANumber extends Value {

	/**
	 * The numeric value to which this object corresponds
	 */
	private Number value;

	/**
	 * Constructs a new object representing a given numeric value
	 * @param value Numeric value
	 */
	private ANumber(Number value) {
		this.value = value;
	}

	/**
	 * Gets this object's numeric value
	
	 * @return This object's numeric value  */
	public Number getValue() {
		return value;
	}

	/**
	 * Tries to parse a number from the provided string.
	 * @param s
	 *            The string to process
	 * @return The corresponding number object, if the parse was successful 
	 * @throws CellParseException
	 *             If the parse was unsuccessful (i.e. the string does not
	 *             contain a valid number)
	 */
	protected static ANumber parseNumber(String s) throws CellParseException {
		s = CellParser.removeSurroundingSpaces(s);
		try {
			return new ANumber(new Integer(Integer.parseInt(s)));
		} catch (NumberFormatException e1) {
			try {
				return new ANumber(new Double(Double.parseDouble(s)));
			} catch (NumberFormatException e2) {
				throw new CellParseException();
			}
		}
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

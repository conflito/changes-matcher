package pt.ul.fc.di.dco000.services.parser;

/**
 * Represents literal text parsed from the user's input for a cell's content.
 * This only occurs as root content.
 * @author jcraveiro
 *
 * @version $Revision: 1.0 $
 */
public class Literal extends Value {
	
	/**
	 * The string of text to which this object corresponds
	 */
	private String value;
	
	/**
	 * Creates a new object representing literal text
	 * @param s The literal text
	 */
	private Literal (String s) {
		this.value = s;
	}

	/**
	 * Parses a literal text object from the provided string. It always succeeds.
	 * @param s The string to parse 
	
	 * @return The corresponding literal text object */
	protected static Value parseLiteral(String s) {
		return new Literal(s);
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
	 * Gets the string of text to which this object corresponds.
	
	 * @return The string of text to which this object corresponds. */
	public String getString() {
		return value;
	}
	
	/**
	 * Returns a string representation of the object.	
	 * @return a string representation of the object.
	 */
	public String toString() {
		return super.toString();
	}

}

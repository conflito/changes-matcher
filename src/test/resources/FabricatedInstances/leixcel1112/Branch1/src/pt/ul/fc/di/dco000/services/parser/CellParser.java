package pt.ul.fc.di.dco000.services.parser;

/**
 * The main parser class. 
 * 
 * @author jcraveiro
 * @version $Revision: 1.0 $
 */
public class CellParser {

	/**
	 * Parses the user's input. The return value is of a Value subtype,
	 * depending on the user input. Here are some examples:
	 * <dl>
	 * <dt>=SUBSTRING("Abc";1;A2)</dt>
	 * <dd>A Formula instance, containing a Function instance and a list of
	 * arguments containing 1) an AString instance 2) an ANumber instance, and
	 * 3) a CellReference instance</dd>
	 * <dt>=AVERAGE(SUM(A1;A2);SUM(B1;B2))</dt>
	 * <dd>A Formula instance, containing a Function instance and a list of
	 * arguments containing two distinct Formula instances (each one containing
	 * a Function instance and a list of arguments containing two CellReference
	 * instances)</dd>
	 * <dt>=A2</dt>
	 * <dd>A CellReference instance</dd>
	 * <dt>1234</dt>
	 * <dd>An ANumber instance</dd>
	 * <dt>Xpto123</dt>
	 * <dd>A Literal instance</dd>
	 * </dl>
	 * 
	 * @param s
	 *            The string to process
	 * @return A value object meaningfully representing the parsed value 
	 * @throws CellParseException
	 *             If nothing could be parsed from the input (either number,
	 *             string, cell reference or formula)
	 */
	//@requires s != null && s.length() > 0
	public static Value parseContent(String s) throws CellParseException {
		if (s.charAt(0) == '=') {
			return Value.parseValue(s.substring(1), true);
		} else {
			try {
				// Try to parse a number from it
				return ANumber.parseNumber(s);
			} catch (CellParseException e1) {
				// If that didn't work, then treat as a cell with text
				return Literal.parseLiteral(s);
			}
		}
	}

	/**
	 * Removes spaces at the beginning and end of the provided string
	 * 
	 * @param s
	 *            The string to clean
	
	 * @return The cleaned string */
	protected static String removeSurroundingSpaces(String s) {

		StringBuilder sb = new StringBuilder(s);

		while (sb.length() > 0 && sb.charAt(0) == ' ') {
			sb.deleteCharAt(0);
		}

		while (sb.length() > 0 && sb.charAt(s.length() - 1) == ' ') {
			sb.deleteCharAt(s.length() - 1);
		}

		return sb.toString();
	}
	
	/**
	 * Returns a string representation of the object.
	 * @return a string representation of the object.
	 */
	public String toString() {
		return super.toString();
	}

}

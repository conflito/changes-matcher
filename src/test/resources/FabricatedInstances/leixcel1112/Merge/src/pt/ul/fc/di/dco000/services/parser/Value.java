package pt.ul.fc.di.dco000.services.parser;

/**
 * Represents a typed value parsed from user's input for a cell's content.
 * 
 * @author jcraveiro
 * @version $Revision: 1.0 $
 */
public abstract class Value {

	/**
	 * If this value is root cell content (true), or
	 * an argument of a formula (false)
	 */
	protected boolean isRoot;

	/**
	 * Tries to parse a value from user's input for a cell content. The parses
	 * only gets to here is the user's input starts with an equals sign. This is
	 * also what is called when trying to parse arguments in formulae.
	 * 
	 * @param s
	 *            The string to be parsed
	 * @param isRoot
	 *            True is this formula is to be the root cell's content, false
	 *            if it occurs as an argument to another formula.
	
	
	 * @return The corresponding value (or subtype thereof) object, if the parse
	 *         was successful
	 *         @throws CellParseException
	 *             If nothing could be parsed from the input (either number,
	 *             string, cell reference or formula) 
	 */
	public static Value parseValue(String s, boolean isRoot)
			throws CellParseException {

		try {
			// Try to parse a number from it
			return ANumber.parseNumber(s);
		} catch (CellParseException e1) {
			try {
				// If it fails, try to parse a quote-delimited string from it
				return AString.parseAString(s);
			} catch (CellParseException e2) {
				try {
					// If it fails, try to parse a cell reference from it
					return CellReference.parseCellReference(s);
				} catch (CellParseException e3) {
					//  If it fails, try to parse a formula from it
					return Formula.parseFormula(s, isRoot);
					// If it fails, it fails - no default fallback
				}
			}
		}
	}
	
	/**
	 * Method accept.
	 * @param <T> Parametrized type
	 * @param visitor IParserVisitor<T>
	 * @return T
	 */
	public abstract <T> T accept(IParserVisitor<T> visitor);

}

package pt.ul.fc.di.dco000.services.parser;

import java.util.Locale;
import java.util.StringTokenizer;

/**
 * Represents a cell reference parsed from the user's input for a cell's
 * content. The reference is not verified to be effectively valid, only
 * syntactically. Supports fixed column and/or line (with dollar sign).
 * 
 * @author jcraveiro
 * @version $Revision: 1.0 $
 */
public class CellReference extends Value {

	/**
	 * Whether the column is fixed
	 */
	private boolean fixedCol;

	/**
	 * The column reference (a string of capital letters)
	 */
	private String col;

	/**
	 * Whether the line is fixed
	 */
	private boolean fixedLine;

	/**
	 * The line reference
	 */
	private int line;

	/**
	 * Creates a new cell reference
	 * @param col The column reference
	 * @param line The line reference
	 * @param fixedCol Whether the column is fixed
	 * @param fixedLine Whether the line is fixed
	 */
	private CellReference(String col, int line, boolean fixedCol,
			boolean fixedLine) {
		this.fixedCol = fixedCol;
		this.col = col;

		this.fixedLine = fixedLine;
		this.line = line;
	}

	/**
	 * Queries whether this reference has fixed column
	 * 
	
	 * @return Whether the column is fixed */
	public boolean isFixedCol() {
		return fixedCol;
	}

	/**
	 * Gets this reference's column
	 * 
	
	 * @return The column (letters) */
	public String getCol() {
		return col;
	}

	/**
	 * Queries whether this reference has fixed line
	 * 
	
	 * @return Whether the line is fixed */
	public boolean isFixedLine() {
		return fixedLine;
	}

	/**
	 * Gets this reference's line
	 * 
	
	 * @return The line (number) */
	public int getLine() {
		return line;
	}

	/**
	 * Tries to parse a cell reference from the provided string
	 * 
	 * @param s The string to process
	 * @return The corresponding reference object, if the parse was successful
	 * @throws CellParseException
	 *             If the parse was unsuccessful (i.e. the string does not
	 *             contain a cell reference) 
	 */
	protected static CellReference parseCellReference(String s)
			throws CellParseException {
		s = CellParser.removeSurroundingSpaces(s).toUpperCase(Locale.ENGLISH);

		boolean fixedLine = false;
		boolean fixedCol = false;
		String putativeLine = null;
		String putativeCol = null;

		StringTokenizer st = new StringTokenizer(s, "$");

		
		switch (st.countTokens()) {
		case 1: // will fall here if it's either A1 or $A1
			fixedLine = false;
			fixedCol = (s.charAt(0) == '$');
			StringBuilder sb = new StringBuilder(1);
			int idx = fixedCol ? 1 : 0;
			while (idx < s.length() && Character.isLetter(s.charAt(idx))) {
				sb.append(s.charAt(idx));
				idx++;
			}
			putativeCol = sb.toString();
			putativeLine = s.substring(idx + (fixedCol ? 1 : 0));

			break;
		case 2: // will fall here if it's either A$1 or $A$1
			fixedLine = true;
			fixedCol = (s.charAt(0) == '$');
			putativeCol = st.nextToken();
			putativeLine = st.nextToken();

			break;
		default: // no chance here
			throw new CellParseException();
		}

		int line = -1;

		try {
			line = Integer.parseInt(putativeLine);
			if (line <= 0)
				throw new CellParseException();
		} catch (NumberFormatException exc) {
			throw new CellParseException();
		}

		return new CellReference(putativeCol.toUpperCase(Locale.ENGLISH), line,
				fixedCol, fixedLine);

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
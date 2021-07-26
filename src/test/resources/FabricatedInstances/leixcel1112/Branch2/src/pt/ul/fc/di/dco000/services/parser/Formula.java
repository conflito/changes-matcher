package pt.ul.fc.di.dco000.services.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Represents a formula parsed from the user's input for a cell's content. The
 * value is not calculated, and nothing about the function itself is verified
 * except that the name is syntactically valid (i.e. is a string of letters).
 * More specifically, the function's existence, arity and parameter type is not
 * considered.
 * 
 * @author jcraveiro
 * @version $Revision: 1.0 $
 */
public class Formula extends Value {

	/**
	 * The function invoked
	 */
	Function function;

	/**
	 * The arCollectionguments provided
	 */
	List<Value> args;

	/**
	 * The character used to open the list of arguments
	 */
	private static final char ARGS_OPEN = '(';

	/**
	 * The character used to close the list of arguments
	 */
	private static final char ARGS_CLOSE = ')';

	/**
	 * The character used to separate the arguments in the list. We adopted the
	 * semicolon to simplify supporting OpenDocument export through ODFDOM
	 */
	private static final char ARGS_SEPARATOR = ';';

	/**
	 * Creates a new formula object
	 * 
	 * @param f
	 *            The function invoked by the formula
	 * @param args
	 *            The arguments passed to the function
	 * @param isRoot
	 *            If this formula is root cell content (true), or an argument to
	 *            another formula (false)
	 */
	private Formula(Function f, List<Value> args, boolean isRoot) {
		this.function = f;
		this.args = args;
		this.isRoot = isRoot;
	}

	/**
	 * Gets this formula's function's name.
	
	 * @return This formula's function's name */
	public String getFunctionName() {
		return function.getName();
	}

	/**
	 * Gets the arguments provided to this formuala's function
	
	 * @return A list with the arguments */
	public List<Value> getArgs() {
		return args;
	}

	/**
	 * Tries to parse a formula from the provided string. For the parse to be
	 * successful, each argument must also be parseable as some value (another
	 * formula, a cell reference, a number, or a quote-delimited string.
	 * 
	 * @param s
	 *            The string to process
	 * @param isRoot
	 *            True is this formula is to be the root cell's content, false
	 *            if it occurs as an argument to another formula.
	
	
	 * @return The corresponding formula object, if the parse was successful
	 * @throws CellParseException
	 *             If the parse was unsuccessful (i.e. the string does not
	 *             contain a valid formula)
	 */
	protected static Formula parseFormula(String s, boolean isRoot)
			throws CellParseException {
		s = CellParser.removeSurroundingSpaces(s);
		
		int openParen = -1;

		/*
		 * s.indexOf(ARGS_OPEN) meanings:
		 * 
		 * -1: there is no open paren.
		 * 
		 * 0: open paren is the first character (i.e. null function name)
		 */
		if (s.charAt(s.length() - 1) != ARGS_CLOSE ||
				(openParen = s.indexOf(ARGS_OPEN)) < 1)
			throw new CellParseException();

		Function f = Function.parseFunction(s.substring(0, openParen));

		String argsString = s.substring(openParen + 1, s.length() - 1);

		StringTokenizer st = new StringTokenizer(argsString, ""
				+ ARGS_SEPARATOR);

		List<Value> args = new ArrayList<Value>();

		while (st.hasMoreTokens()) {
			Value v = null;
			StringBuilder token = new StringBuilder(st.nextToken());

			/**
			 * This whole mumbo jumbo is to support argument separators
			 * occurring inside strings.
			 */
			while (null == v) {
				try {
					v = Value.parseValue(token.toString(), false);
				} catch (CellParseException e1) {
					if (st.hasMoreTokens()) {
						token.append(ARGS_SEPARATOR);
						token.append(st.nextToken());
					} else {
						throw e1;
					}
				}
			}

			args.add(v);
		}

		return new Formula(f, args, isRoot);
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

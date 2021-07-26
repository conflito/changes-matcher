/**
 * Implements a Move in a word search game Third deliverable of the project of
 * Introducao a Programacao 2020 @ DI-FCUL (Programming Fundamentals @ DI-FCUL )
 *
 * @author amordido
 */

public class Move {

	private int row1;
	private int col1;
	private int row2;
	private int col2;
	private int rows;
	private int columns;
	private Direction direction;

	/**
	 * Verifies if indexes define a well-formed move
	 *
	 * @param row1    given integer representing the row of the start position
	 * @param col1    given integer representing the column of the start position
	 * @param row2    given integer representing the row of the final position
	 * @param col2    given integer representing the column of the final position
	 * @param rows    given integer representing the number of rows in a matrix
	 * @param columns given integer representing the number of columns in a matrix
	 * @return true if the positions define a well-formed move, false otherwise.
	 **/
	public static boolean definesMove(int row1, int col1, int row2, int col2, int rows, int columns) {
		return validIndexes(row1, col1, rows, columns) && validIndexes(row2, col2, rows, columns)
				&& validContiguousIndexes(row1, col1, row2, col2);
	}

	/**
	 * Verifies if indexes are valid in a matrix
	 *
	 * @param row     given integer representing the row of a position
	 * @param col     given integer representing the column of a position
	 * @param rows    given integer representing the number of rows in a matrix
	 * @param columns given integer representing the number of columns in a matrix
	 * @return true if the position is valid, false otherwise.
	 */
	private static boolean validIndexes(int row, int col, int rows, int columns) {
		return row <= rows && col <= columns;
	}

	/**
	 * Verifies if indexes constitute contiguous positions in a matrix
	 *
	 * @param row1    given integer representing the row of the start position
	 * @param col1    given integer representing the column of the start position
	 * @param row2    given integer representing the row of the final position
	 * @param col2    given integer representing the column of the final position
	 * @param rows    given integer representing the number of rows in a matrix
	 * @param columns given integer representing the number of columns in a matrix
	 * @requires validIndexes (row1, col1, rows, columns) && validIndexes (row2,
	 *           col2, rows, columns)
	 * @return true if the positions are contiguous, false otherwise.
	 */
	private static boolean validContiguousIndexes(int row1, int col1, int row2, int col2) {
		boolean validRowIndexes = row1 == row2 && col1 <= col2;
		boolean validColumnIndexes = col1 == col2 && row1 <= row2;
		boolean validPositiveDiagonalIndexes = row2 >= row1 && row2 - row1 == col2 - col1;
		boolean validNegativeDiagonalIndexes = row2 >= row1 && row2 - row1 == col1 - col2;
		return validRowIndexes || validColumnIndexes || validPositiveDiagonalIndexes || validNegativeDiagonalIndexes;
	}

	/**
	 * Constructs a move
	 *
	 * @param row1    given integer representing the row of the start position
	 * @param col1    given integer representing the column of the start position
	 * @param row2    given integer representing the row of the final position
	 * @param col2    given integer representing the column of the final position
	 * @param rows    given integer representing the number of rows in a matrix
	 * @param columns given integer representing the number of columns in a matrix
	 * @requires definesMove (row1, col1, row2, col2, rows, columns)
	 */
	public Move(int row1, int col1, int row2, int col2, int rows, int columns) {
		this.row1 = row1;
		this.col1 = col1;
		this.row2 = row2;
		this.col2 = col2;
		this.rows = rows;
		this.columns = columns;
		if (row1 == row2)
			this.direction = Direction.HORIZONTAL;
		else if (col1 == col2)
			this.direction = Direction.VERTICAL;
		else if (col1 <= col2)
			this.direction = Direction.DIAGONAL_RIGHT;
		else
			this.direction = Direction.DIAGONAL_LEFT;
	}

	/**
	 * Gets the row of the starting position of this move
	 *
	 * @return the start row of this move
	 */
	public int startRow() {
		return this.row1;
	}

	/**
	 * Gets the column of the starting position of this move
	 *
	 * @return the start column of this move
	 */
	public int startColumn() {
		return this.col1;
	}

	/**
	 * Gets the row of the final position of this move
	 *
	 * @return the final row of this move
	 */
	public int endRow() {
		return this.row2;
	}

	/**
	 * Gets the column of the final position of this move
	 *
	 * @return the final column of this move
	 */
	public int endColumn() {
		return this.col2;
	}

	/**
	 * Gets the direction of this move
	 *
	 * @return the direction of this move
	 */
	public Direction direction() {
		return this.direction;
	}

	/**
	 * Gets the total number of rows in the move
	 *
	 * @return the number of rows
	 */
	public int rows() {
		return this.rows;
	}

	/**
	 * Gets the total number of columns in the move
	 *
	 * @return the number of columns
	 */
	public int columns() {
		return this.columns;
	}

	@Override
	public String toString() {
		return "(" + row1 + "," + col1 + ")(" + row2 + "," + col2+")";
	}
}

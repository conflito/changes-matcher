/**
 * Implements the Puzzle of a word search game Third deliverable of the project
 * of Introducao a Programacao 2020
 *
 * @author amordido
 */

public class Puzzle {

	private final char[][] board;
	private final String[] hiddenWords;

	/**
	 * Verifies if the board and hiddenWords define a Puzzle
	 *
	 * @param board       the given board
	 * @param hiddenWords given hidden words
	 * @return true if the board and hidden words define a puzzle, false otherwise
	 * @requires {@code isMatrix(board) && isHiddenWords != null}
	 */
	public static boolean definesPuzzle(char[][] board, String[] hiddenWords) {
		boolean valid = hiddenWords.length > 0;
		for (int i = 0; i < hiddenWords.length; i++) {
			if (!isHidden(board, hiddenWords[i]))
				valid = false;
		}
		return valid;
	}

	/**
	 * Verifies if the board is a matrix
	 *
	 * @param board the given board
	 * @return true if the board is a matrix, false otherwise
	 */
	public static boolean isMatrix(char[][] board) {
		if (board == null)
			return false;
		else if (board.length == 0)
			return false;
		else {
			for (int i = 0; i < board.length - 1; i++) {
				if (board[i].length != board[i + 1].length)
					return false;
			}
		}
		return true;
	}

	/**
	 * Verifies if a word is hidden in the board
	 *
	 * @param board the given board
	 * @param word  the given word
	 * @return true if the word is hidden in the board, false otherwise
	 */
	private static boolean isHidden(char[][] board, String word) {
		int maxDim = Math.max(board.length, board[0].length);
		for (int i = 0; i < maxDim; i++) {
			if ((i < board.length &&
					(containsWord(rowToString(board[i]), word) ||
							containsWord(diagonalRightBelowToString(board,i), word))) ||
					(i < board[0].length &&
							(containsWord(columnToString(board,i), word) ||
									containsWord(diagonalRightAboveToString(board,i), word) ||
									containsWord(diagonalLeftToString(board,i), word))))
				return true;
		}
		return false;
	}

	/**
	 * Converts a row into a string
	 *
	 * @param row given row
	 * @return the string with the characters in the row
	 */
	private static String rowToString (char[] row){
		StringBuilder sb = new StringBuilder();
		return sb.append(row).toString();
	}

	/**
	 * Converts a column into a string
	 *
	 * @param board given matrix
	 * @param i given integer
	 * @return the string with the characters in column j
	 */
	private static String columnToString (char[][] board, int j){
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < board[0].length; i++)
			sb.append(board[i][j]);
		return sb.toString();
	}

	/**
	 * Converts a diagonal right below the principal diagonal into a string
	 *
	 * @param board given matrix
	 * @param i given integer
	 * @return the string with the characters in the right diagonal starting at (i,0)
	 */
	private static String diagonalRightBelowToString (char[][] board, int i){
		StringBuilder sb = new StringBuilder();
		for (int j = 0; j < board.length - i && j < board[0].length; j++)
			sb.append(board[i + j][j]);
		return sb.toString();
	}

	/**
	 * Converts a diagonal right above the principal diagonal into a string
	 *
	 * @param board given matrix
	 * @param j given integer
	 * @return the string with the characters in the right diagonal starting at (0,j)
	 */
	private static String diagonalRightAboveToString (char[][] board, int j){
		StringBuilder sb = new StringBuilder();
		for (int k = 0; k < board[0].length - j && k < board.length; k++)
			sb.append(board[k][j + k]);
		return sb.toString();
	}

	/**
	 * Converts a diagonal left into a string
	 *
	 * @param board given matrix
	 * @param j given integer
	 * @return the string with the characters in the left diagonal starting at (0,j)
	 */
	private static String diagonalLeftToString (char[][] board, int j){
		StringBuilder sb = new StringBuilder();
		for (int k = 0; k < board.length && j - k >= 0; k++)
			sb.append(board[k][j - k]);
		return sb.toString();
	}

	/**
	 * Verifies if a string contains another in normal or reversed order
	 *
	 * @param boardSegment given string
	 * @param word         given word to be found in boardSegment
	 * @return true if word is contained in boardSegment in normal or reversed
	 * order, false otherwise
	 */
	private static boolean containsWord(String boardSegment, String word) {
		String reverseWord = new StringBuilder(word).reverse().toString();
		return boardSegment.contains(word) || boardSegment.contains(reverseWord);
	}

	/**
	 * Creates a puzzle with the given board and hidden words
	 *
	 * @param board       given board
	 * @param hiddenWords given hidden words
	 *
	 */
	public Puzzle(char[][] board, String[] hiddenWords) {
		this.board = new char[board.length][board[0].length];
		this.hiddenWords = new String[hiddenWords.length];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				this.board[i][j] = board[i][j];
			}
		}
		for (int i = 0; i < hiddenWords.length; i++) {
			this.hiddenWords[i] = hiddenWords[i];
		}
	}

	/**
	 * Gets the number of rows in the board of this puzzle
	 *
	 * @return number of rows of the board
	 */
	public int rows() {
		return this.board.length;
	}

	/**
	 * Gets the number of columns in the board of this puzzle
	 *
	 * @return number of columns of the board
	 */
	public int columns() {
		return this.board[0].length;
	}

	/**
	 * Gets the number of hidden words in this puzzle
	 *
	 * @return number of hidden words
	 */
	public int numberHiddenWords() {
		return this.hiddenWords.length;
	}

	/**
	 * Gets the board of this puzzle
	 *
	 * @return the board of this puzzle
	 */
	public char[][] board() {
		char[][] newBoard = new char[this.board.length][this.board[0].length];
		for (int i = 0; i < this.board.length; i++) {
			for (int j = 0; j < this.board[0].length; j++) {
				newBoard[i][j] = this.board[i][j];
			}
		}
		return newBoard;
	}

	/**
	 * Determines a string from a given move
	 *
	 * @param move given move
	 * @return the word represented in this move
	 */
	private String getStringFromPositions(Move move) {
		StringBuilder sb = new StringBuilder();
		int row1 = move.startRow();
		int col1 = move.startColumn();
		int row2 = move.endRow();
		int col2 = move.endColumn();
		switch (move.direction()) {
		case HORIZONTAL:
			for (int j = col1 - 1; j <= col2 - 1; j++)
				sb.append(this.board[row1 - 1][j]);
			break;
		case VERTICAL:
			for (int i = row1 - 1; i <= row2 - 1; i++)
				sb.append(this.board[i][col1 - 1]);
			break;
		case DIAGONAL_RIGHT:
			for (int i = 0; i <= col2 - col1; i++) {
				sb.append(this.board[row1 - 1 + i][col1 - 1 + i]);
			}
			break;
		case DIAGONAL_LEFT:
			for (int i = 0; i <= col1 - col2; i++) {
				sb.append(this.board[row1 - 1 + i][col1 - 1 - i]);
			}
			break;
		}
		return sb.toString();
	}

	/**
	 * Determines a string from the given move
	 *
	 * @param move given move
	 * @return the word represented in this move if it represents a hidden words,
	 * null otherwise
	 * @requires {@code move != null && move.rows() == rows() && move.columns() == columns()}
	 */
	public String getWord(Move move) {
		String word = getStringFromPositions(move);
		String result = null;
		for (int i = 0; i < numberHiddenWords(); i++) {
			if (this.hiddenWords[i].equals(word) ||
					this.hiddenWords[i].equals(new StringBuilder(word).reverse().toString())) {
				return  this.hiddenWords[i];
			}
		}
		return result;
	}
}

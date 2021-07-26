import java.util.Arrays;

/**
 * This class tests the methods of Move,Puzzle and WordSearch,
 * on some simple situations.
 *
 * @author Graca Gaspar
 * @date December 2020
 */

public class WordSearchTest{

	private static final char[][] BOARD =  {
			{'R', 'O', 'M', 'A', 'E'},
			{'G', 'A', 'I', 'J', 'K'},
			{'M', 'T', 'M', 'P', 'Q'},
			{'S', 'A', 'U', 'A', 'X'},
			{'B', 'P', 'D', 'E', 'F'},
			{'H', 'I', 'J', 'K', 'L'},
			{'N', 'O', 'P', 'Q', 'R'},
			{'T', 'U', 'V', 'X', 'Y'},
			{'C', 'D', 'E', 'F', 'G'},
			{'I', 'J', 'K', 'L', 'M'}
	};
	
	private static final int ROWS = BOARD.length;
	private static final int COLUMNS = BOARD[0].length;

	private static final  int[] VALID_POSITIONS = new int[]{1, 1, 4, 4};
	private static final  int[] INVALID_POSITIONS = new int[]{10, 2, 5, 2};

	private static final  String[] VALID_HIDDEN = new String [] {"ROMA", "PATA", "RAMA"};
	private static final  String[] INVALID_HIDDEN = new String[]{"CBA", "XX"}; // be brave and add also "" :-)
	private static final  int DURATION_IN_SECONDS = 50;


	/**
	 * Executes tests on classes Move, Puzzle and WordSearch
	 * @param args
	 */
	/*public static void main (String[] args){

		//System.out.println(">>>>>> Starting tests <<<<<<");
		//System.out.println();
		//testMove();
		//System.out.println(testPuzzle());
		//testWordSearch();

		//System.out.println(">>>>>> Tests finished <<<<<< \n");
		//System.out.println("Do not forget: these are just a couple of simple tests.");
		//System.out.println("Test your code with additional tests!!!");
	}*/

	/**
	 * Tests functions and methods of class Move:
	 * - static boolean  definesMove(int row1, int col1,
	 *                     int row2, int col2, int rows, int columns)
	 * - int startRow()/startColumn()
	 * - int endRow()/endColumn()
	 * - Direction direction()
	 * - int rows()/columns()
	 */
	private static String testMove(){
		StringBuilder result = new StringBuilder();
		// initialize the valid Move
		Move validMove = new Move(VALID_POSITIONS[0], VALID_POSITIONS[1], VALID_POSITIONS[2], VALID_POSITIONS[3],
				ROWS, COLUMNS);

		result.append("\n");//System.out.println();
		result.append(">>> Testing class Move:<<<\n");
		//System.out.println(">>> Testing class Move:<<<");
		result.append("\n");//System.out.println();
		boolean obtained = Move.definesMove(VALID_POSITIONS[0], VALID_POSITIONS[1],
				VALID_POSITIONS[2], VALID_POSITIONS[3], ROWS, COLUMNS);
		result.append(printTest("definesMove", "valid", "move", obtained));
		obtained = Move.definesMove(INVALID_POSITIONS[0], INVALID_POSITIONS[1],
				INVALID_POSITIONS[2], INVALID_POSITIONS[3], ROWS, COLUMNS);
		result.append(printTest("definesMove", "invalid", "move", obtained));

		int position = validMove.startRow();
		result.append(printIntTest("startRow", position, VALID_POSITIONS[0]));
		position = validMove.startColumn();
		result.append(printIntTest("startColumn", position, VALID_POSITIONS[1]));
		position = validMove.endRow();
		result.append(printIntTest("endRow", position, VALID_POSITIONS[2]));
		position = validMove.endColumn();
		result.append(printIntTest("endColumn", position, VALID_POSITIONS[3]));
		int bound = validMove.rows();
		result.append(printIntTest("rows", bound, ROWS));
		bound = validMove.columns();
		result.append(printIntTest("columns", bound, COLUMNS));

		Direction dir = validMove.direction();
		Direction expDir = Direction.DIAGONAL_RIGHT;
		result.append(printDirTest("direction", dir, expDir));

		return result.toString();
	}

	/**
	 * Tests the methods of class Puzzle:
	 * - static boolean isHidden(char[][] board, String word)
	 * - static boolean definesPuzzle(char[][] board, String[] hiddenWords)
	 * - int rows()/columns()
	 * - int numberHiddenWords()
	 * - char[][] board()
	 * - String getWord(Move move)
	 */
	public static String testPuzzle(){

		StringBuilder result = new StringBuilder();
		Move [] m = new Move[3];
		// initializes the moves that find hiddenWords
		m[0] = new Move(1,1,1,1+VALID_HIDDEN[0].length() - 1, ROWS, COLUMNS);
		m[1] = new Move(2, 2, 2 + VALID_HIDDEN[1].length() - 1, 2, ROWS, COLUMNS);
		m[2] = new Move(1,1,1+VALID_HIDDEN[2].length()-1,1+VALID_HIDDEN[2].length() - 1, ROWS, COLUMNS);

		// create the valid puzzle
		Puzzle validPuzzle = new Puzzle(BOARD, VALID_HIDDEN);

		result.append("\n >>> Testing class Puzzle: <<<\n");
		//System.out.println("\n >>> Testing class Puzzle: <<<");
		result.append("\n**** BOARD ****\n");//System.out.println("\n**** BOARD ****");
		for(int i = 0; i < ROWS; i++){
			for(int j = 0; j < COLUMNS; j++)
				result.append(BOARD[i][j] + "  ");//System.out.print(BOARD[i][j] + "  ");
			result.append("\n");//System.out.println();
		}

		result.append("\n **** Invalid HiddenWords ****\n");
		//System.out.println("\n **** Invalid HiddenWords ****");
		result.append(Arrays.toString(INVALID_HIDDEN) + "\n");
		//System.out.println(Arrays.toString(INVALID_HIDDEN));

		boolean obtained = Puzzle.definesPuzzle(BOARD, INVALID_HIDDEN);
		result.append(printTest("definesPuzzle", "invalid", "puzzle", obtained));

		result.append("\n**** Valid HiddenWords ****\n");
		//System.out.println("\n**** Valid HiddenWords ****");
		result.append(Arrays.toString(VALID_HIDDEN) + "\n");
		//System.out.println(Arrays.toString(VALID_HIDDEN));


		obtained = Puzzle.definesPuzzle(BOARD, VALID_HIDDEN);
		result.append(printTest("definesPuzzle", "valid", "puzzle", obtained));

		for (int i = 0; i < VALID_HIDDEN.length; i++){
			String w = validPuzzle.getWord(m[i]);
			result.append(printStrTest("getWord with move "+ m[i].toString(), w, VALID_HIDDEN[i]));
		}

		Move moveThatDoesnotFindHiddenWord = new Move(1,1,1,2,ROWS,COLUMNS);
		String w = validPuzzle.getWord(moveThatDoesnotFindHiddenWord);
		result.append(printStrTest("getWord with move " + moveThatDoesnotFindHiddenWord.toString(), w , null));

		char[][]boardCopy = validPuzzle.board();
		result.append(printTest("board", "well copied", " board", BOARD != boardCopy ));
		boolean same = true;
		for (int i = 0; i < ROWS && same ; i++) {
			for (int j = 0; j < COLUMNS && same; j++) {
				if (BOARD[i][j] != boardCopy[i][j]){
					result.append(printTest("board", "well copied", " board", false  ));
					same = false;
				}
			}
		}

		return result.toString();
	}

	/**
	 * Tests the methods of class WordSearch:
	 * - Puzzle puzzle()
	 * - int duration()
	 * - int howManyFoundWords()
	 * - public int score()
	 * - String[] foundWords()
	 * - boolean isFinished()
	 * - boolean play(Move move)
	 * - String toString()
	 */
	public static String testWordSearch(){
		
		StringBuilder result = new StringBuilder();
		Move [] m = new Move[5];

		//  the moves to be executed  
		m[0] = new Move(1, 1, 1, 4, ROWS, COLUMNS);
		// a move already played
		m[1] = new Move(1, 1, 1, 4, ROWS, COLUMNS); 
		m[2] = new Move(2, 2, 5, 2, ROWS, COLUMNS);
		// a move that does not find a hidden word
		m[3] = new Move(4, 1, 4, 4, ROWS, COLUMNS);
		m[4] = new Move(1, 1, 4, 4, ROWS, COLUMNS);

		// the valid puzzle to use in the game
		Puzzle validPuzzle = new Puzzle(BOARD, VALID_HIDDEN);

		//initialize the WordSearch game
		WordSearch game = new WordSearch(validPuzzle, DURATION_IN_SECONDS);
		result.append("\n");//System.out.println();
		result.append(">>> Testing class WordSearch: <<<\n");
		//System.out.println(">>> Testing class WordSearch: <<<");
		result.append("\n");
		//System.out.println();
		result.append("The starting state:\n");
		//System.out.println("The starting state:");
		result.append(game.toString() + "\n\n");
		//System.out.println(game.toString() + "\n");

		int gameDuration = game.duration();
		result.append(printIntTest("duration", gameDuration, DURATION_IN_SECONDS));

		double meanTime = DURATION_IN_SECONDS / validPuzzle.numberHiddenWords();
		result.append("\n> Word Points in this game: "+ (game.puzzle().rows() * game.puzzle().columns() / 10) + "\n");
		//System.out.println("\n> Word Points in this game: "+ game.puzzle().rows() * game.puzzle().columns() / 10);
		result.append("> Expected time for finding a word: "+ (int)meanTime + "seconds\n");
		//System.out.println("> Expected time for finding a word: "+ (int)meanTime + "seconds");

		int nMoves = 0;
		int words;

		do {
			result.append("\n> Executing move: " + m[nMoves] + "\n");
			//System.out.println("\n> Executing move: " + m[nMoves]);
			result.append("\n> Executing move: " +
				m[nMoves].startRow() + ", " +  m[nMoves].startColumn() + ", " +
				m[nMoves].endRow() + ", "  +  m[nMoves].endColumn() + "\n");
			//System.out.println("\n> Executing move: " +
			//		m[nMoves].startRow() + ", " +  m[nMoves].startColumn() + ", " +
			//		m[nMoves].endRow() + ", "  +  m[nMoves].endColumn());
			boolean wordIsFound = game.play(m[nMoves]);

			result.append("> move found word?: " + wordIsFound + "\n");
			//System.out.println("> move found word?: " + wordIsFound);
			result.append("> move finds word: " + game.puzzle().getWord(m[nMoves]) + "\n");
			//System.out.println("> move finds word: " + game.puzzle().getWord(m[nMoves]));
			result.append("> found words: \n");
			//System.out.println("> found words: ");
			words = game.howManyFoundWords();
			for (int k = 0; k < words; k++)
				result.append(game.foundWords()[k] + "  \n");
				//System.out.print(game.foundWords()[k] + "  " );
			result.append("\n");//System.out.println();
			result.append(printIntTest("howManyFoundWords", words, nMoves / 2  + 1));
			result.append("> current score: " + game.score() + "\n");
			//System.out.println("> current score: " + game.score());
			result.append("  (be aware this value can vary from run to run, since it depends on elapsed time)");
			//System.out.println("  (be aware this value can vary from run to run, since it depends on elapsed time)");

			nMoves++;

			if (nMoves ==  m.length - 1) {
				result.append("\n> Making a big pause before the last move....\n");
				//System.out.println("\n> Making a big pause before the last move....");
				try {
					Thread.sleep((long) (meanTime*1000 + 1000));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		} while (nMoves < m.length && !game.isFinished());

		result.append(printTest("isFinished", "finished", "game",  game.isFinished() ));
		result.append("\n");//System.out.println();

		result.append("The ending state:");//System.out.println("The ending state:");
		result.append(game.toString() + "\n\n");//System.out.println(game.toString() + "\n");

		return result.toString();
	}

	public static String printTest(String methodName, String msg,
			String className, boolean obtained){
		StringBuilder result = new StringBuilder();
		
		result.append(">>Testing " + methodName + " for " + msg + " " + className + "\n");
		//System.out.println(">>Testing " + methodName + " for " + msg + " " + className);
		boolean expected =  (msg.equals("valid") || msg.equals("well copied") || msg.equals("finished") );
		if (obtained == expected )
			result.append("  Ok\n");//System.out.println("  Ok");
		else 
			result.append("  ERROR: obtained " +  obtained + " but should be " +  expected + "\n");
			//System.out.println("  ERROR: obtained " +  obtained + " but should be " +  expected);

		return result.toString();
	}

	public static String printIntTest(String methodName, int obtained, int expected){
		StringBuilder result = new StringBuilder();
		result.append(">>Testing " + methodName + ": \n");
		//System.out.println(">>Testing " + methodName + ": ");
		if (obtained == expected)
			result.append("  Ok "+ obtained + "\n");
			//System.out.println("  Ok "+ obtained);
		else
			result.append("  ERROR: obtained " + obtained + "but should be " + expected + "\n");
			//System.out.println("  ERROR: obtained " + obtained + "but should be " + expected);

		return result.toString();
	}

	public static String printStrTest(String methodName, String obtained, String expected){
		StringBuilder result = new StringBuilder();
		
		result.append(">>Testing " + methodName + ": \n");
		//System.out.println(">>Testing " + methodName + ": ");
		if (obtained == null && expected == null || obtained != null && obtained.equals(expected) )
			result.append("  Ok " + obtained + "\n");
			//System.out.println("  Ok " + obtained);
		else
			result.append("  ERROR: obtained " + obtained + " but should be " + expected + "\n");
			//System.out.println("  ERROR: obtained " + obtained + " but should be " + expected);

		return result.toString();
	}

	public static String printDirTest(String methodName, Direction obtained, Direction expected){
		
		StringBuilder result = new StringBuilder();
		result.append(">>Testing " + methodName + ": \n");
		//System.out.println(">>Testing " + methodName + ": ");
		if (obtained.equals(expected))
			result.append("  Ok  " + obtained.name() + "\n");
			//System.out.println("  Ok  " + obtained.name());
		else
			result.append("  ERROR: obtained " + obtained.name() + "but should be " +  expected.name() + "\n");
			//System.out.println("  ERROR: obtained " + obtained.name() + "but should be " +  expected.name());

		return result.toString();
	}


}

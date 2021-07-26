import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Reads and imports a WordSearch puzzle from a file.
 *
 * The file must start with a line containing the hidden words separated by
 * spaces.
 *
 * The remaining lines each contain a line of the puzzle grid, each with a
 * certain amount of characters separated by a TAB (\t byte).
 *
 * A simple example is
 *
 * <pre>
 * {@code
 * TEA CUP
 * A	E	T	P
 * X	Y	Z	U
 * Q	W	E	C
 * }
 * </pre>
 *
 * All lines that define the grid must contain the same number of columns.
 *
 *
 * @author fcasal
 * @author alopes
 */
public class PuzzleReader {

	private char[][] puzzle;
	private String[] hiddenWords;

	private boolean available;
	private List<String> errorMsgs;

	/**
	 * Reads and imports a WordSearch puzzle from a file.
	 *
	 * @param filename path to the file containing the puzzle to be read
	 */
	public PuzzleReader(String filename) {
		boolean problemFound = false;
		errorMsgs = new ArrayList<>();

		try {
			File f = new File(filename);
			Scanner sc = new Scanner(f);
			// first line has the words
			hiddenWords = sc.nextLine().split(" ");

			ArrayList<char[]> letterList = new ArrayList<>();

			int lineSize = -1;
			while (sc.hasNextLine() && !problemFound) {
				String line = sc.nextLine();
				String[] letters = line.split("\t");
				if (lineSize == -1) {
					lineSize = letters.length;
				} else if (lineSize != letters.length) {
					problemFound = true;
					errorMsgs.add("All letter lines should have the same size.");
				}

				if (!problemFound) {
					char[] lettersChar = new char[letters.length];
					for (int i = 0; i < letters.length && !problemFound; i++) {
						if (letters[i].length() != 1) {
							problemFound = true;
							errorMsgs.add("Invalid letter: " + letters[i]);
						}
						lettersChar[i] = letters[i].charAt(0);
					}

					letterList.add(lettersChar);
				}
			}
			// close scanner
			sc.close();

			int numLines = letterList.size();
			puzzle = new char[numLines][lineSize];
			for (int i = 0; i < numLines; i++) {
				for (int j = 0; j < lineSize; j++) {
					puzzle[i][j] = letterList.get(i)[j];
				}
			}

		} catch (FileNotFoundException e) {
			problemFound = true;
			errorMsgs.add("Could not find the file " + filename + "\n" + "Current path: "
					+ System.getProperty("user.dir"));

		} catch (Exception e) {
			problemFound = true;
			errorMsgs.add("Unhandled exception: " + e);
		}
		finally {
			available = !problemFound;
		}
	}

	/**
	 * Gets the error messages, if any.
	 *
	 * @return the error messages
	 * @requires !isPuzzleAvailable()
	 * @ensures \return != null
	 */
	public List<String> getErrorMsgs() {
		return new ArrayList<String>(errorMsgs);
	}

	/**
	 * Checks if a puzzle is available, i.e., the reading of a puzzle from the
	 * provided file was successful
	 *
	 * @return whether there is a puzzle available
	 */
	public boolean isPuzzleAvailable() {
		return available;
	}

	/**
	 * Gets the puzzle
	 *
	 * @return the puzzle read
	 * @requires isPuzzleAvailable()
	 * @ensures {@code \return is a matrix}
	 */
	public char[][] getPuzzle() {
		return puzzle;
	}

	/**
	 * Gets the hidden words
	 *
	 * @return the hidden words
	 * @requires isPuzzleAvailable()
	 * @ensures {@code \return != null}
	 */
	public String[] getHiddenWords() {
		return hiddenWords;
	}

}

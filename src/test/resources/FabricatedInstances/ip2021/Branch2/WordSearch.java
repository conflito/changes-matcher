/**
 * Implements a WordSearch game.
 * Third deliverable of the project of Introducao a Programacao 20/21
 *
 * @author amordido
 */

public class WordSearch {

	private final Puzzle puzzle;
	private String[] foundWords;
	private int nrFoundWords;
	private final int maxDuration;
	private int score;
	private boolean isRunning;
	private final double startSeconds;
	private double secondsLastFound;
	private final double meanTime;
	private final int wordPoints;

	/**
	 * Creates a word search game.
	 *
	 * @param puzzle            given puzzle
	 * @param durationInSeconds given maximum duration
	 * @requires {@code puzzle != null}
	 * @requires {@code durationInSeconds > 0 && durationInSeconds/puzzle.numberHiddenWords() > 5}
	 */
	public WordSearch(Puzzle puzzle, int durationInSeconds) {
		this.puzzle = puzzle;
		this.foundWords = new String[this.puzzle.numberHiddenWords()];
		this.nrFoundWords = 0;
		this.maxDuration = durationInSeconds;
		this.score = 0;
		this.isRunning = true;
		this.startSeconds = Math.round(System.currentTimeMillis()/1000.0);
		this.secondsLastFound = this.startSeconds;
		this.meanTime = (double)this.maxDuration / this.puzzle.numberHiddenWords();
		this.wordPoints = this.puzzle.rows() * this.puzzle.columns() / 10;
	}

	/**
	 * Gets the puzzle of this word search game.
	 *
	 * @return the puzzle of this game
	 * @ensures {@code \result != null}
	 */
	public Puzzle puzzle() {
		return this.puzzle;
	}

	/**
	 * Gets the current number of found words in this word search game.
	 *
	 * @return the number of found words in this game
	 * @ensures {@code \result >= 0}
	 */
	public int howManyFoundWords() {
		return this.nrFoundWords;
	}

	/**
	 * Gets the maximum duration this word search game.
	 *
	 * @return the maximum duration this word search game in seconds
	 * @ensures {@code \result > 0}
	 */
	public int duration() {
		return this.maxDuration;
	}

	/**
	 * Gets the current number of found words in this word search game.
	 *
	 * @return the found words
	 * @ensures {@code \result != null && \result.length == howManyFoundWords()}
	 * @ensures {@code \result[i] != null, for all i in 0 .. \result.length}
	 */
	public String[] foundWords() {
		String[] result = new String[this.nrFoundWords];
		for (int i = 0; i < this.nrFoundWords; i++) {
			result[i] = this.foundWords[i];
		}
		return result;
	}

	/**
	 * Gets the current score in this word search game.
	 *
	 * @return the current score
	 * @ensures {@code \result >= 0}
	 */
	public int score() {
		return this.score;
	}

	/**
	 * Gets the current status of this word search game.
	 *
	 * @return true if the game is finished, false otherwise
	 */
	public boolean isFinished() {
		return !isRunning;
	}

	/**
	 * Plays a move in this word search game.
	 *
	 * @param move the requested move
	 * @return true if the play is done while the game is running and a word is
	 * found, false otherwise
	 */
	public boolean play(Move move) {
		double now = System.currentTimeMillis()/1000.0;
		if (now - this.startSeconds > maxDuration) {
			this.isRunning = false;
			return false;
		}
		
		String optionalWord = this.puzzle.getWord(move);
		if (optionalWord == null)
			return false;
		if (contains(this.foundWords, optionalWord)){
			return true;
		}
		
		double elapsedTime = now - this.secondsLastFound;
		if (elapsedTime < meanTime) {
			this.score += (1 + meanTime - elapsedTime) * this.wordPoints;
		} else {
			this.score += this.wordPoints;
		}
		
		this.foundWords[this.nrFoundWords] = optionalWord;
		this.nrFoundWords++;
		if (nrFoundWords == puzzle().numberHiddenWords()) {
			this.isRunning = false;
		}
		this.secondsLastFound = now;
		return true;
	}

	/**
	 * Determines if a string is in an array of strings.
	 *
	 * @param words given array of words
	 * @param w given word
	 * @return true if the words contains w, false otherwise
	 */
	private boolean contains (String[] words, String w){
		for (String s : words){
			if (s == null)
				return false;
			if (s.equals(w))
				return true;
		}
		return false;
	}

	/**
	 * Gets a textual representation of this word search game.
	 *
	 * @return a textual representation of this word search game
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		char[][] board = this.puzzle.board();
		int wordsToBeFound = this.foundWords.length - this.nrFoundWords;
		sb.append("***************\n");
		sb.append("*    Board    *\n");
		sb.append("***************\n");
		for (char[] chars : board) {
			for (int j = 0; j < board[0].length; j++) {
				sb.append(chars[j]);
				if (j != board[0].length - 1) {
					sb.append("\t");
				}
			}
			sb.append("\n");
		}
		sb.append("***************\n");
		sb.append("* Found words *\n");
		sb.append("***************\n");
		for (int i = 0; i < this.nrFoundWords; i++) {
			sb.append("- " + this.foundWords[i] + "\n");
		}
		sb.append("\n");
		sb.append("*****************************\n");
		sb.append("  Hidden words: " + wordsToBeFound + " \n");
		sb.append("  Current score: " + this.score + " \n");
		sb.append("*****************************\n");
		sb.append("\n");
		return sb.toString();
	}
}

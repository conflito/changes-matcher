package pt.ul.fc.di.dco000.ui;

import java.io.File;

import pt.ul.fc.di.dco000.domain.IWorkbook;
import pt.ul.fc.di.dco000.ui.swing.SheetUI;

/**
 * Defines an abstract sheet UI delegate
 * 
 * @author fmartins
 * @author jcraveiro
 */
public abstract class SheetUIDelegate {

	/**
	 * Reference to the UI's delegate
	 */
	protected SheetUI sheetUI;

	/**
	 * 
	 * @return The current workbook
	 */
	public abstract IWorkbook getCurrentWorkbook();

	/**
	 * Links the delegate back to its UI
	 * 
	 * @param sheetUI
	 *            The sheet UI
	 */
	public void setSheetUI(SheetUI sheetUI) {
		this.sheetUI = sheetUI;
	}

	/**
	 * Notify the UI that the overall structure has changed
	 */
	protected void refreshSheetStructure() {
		sheetUI.refreshSheetStructure();
	}

	/**
	 * Notify the UI that a new line was inserted
	 * 
	 * @param arg
	 *            The index at which the line was inserted
	 */
	protected void refreshSheetNewline(int arg) {
		sheetUI.refreshSheetNewline(arg);
	}

	/**
	 * Notify the UI that a new column was inserted
	 * 
	 * @param arg
	 *            The index at which the column was inserted
	 */
	protected void refreshSheetNewcol(int arg) {
		sheetUI.refreshSheetNewcol(arg);

	}

	/**
	 * Get the textual representation content from the cell at the given
	 * coordinates. The coordinates are domain-independent.
	 * 
	 * @param row
	 *            The row coordinate
	 * @param col
	 *            The column coordinate
	 * @return The textual representation of content from the cell
	 */
	// @requires row >= 0 && col >= 0
	public abstract String getContentAt(int row, int col);

	/**
	 * Get the textual representation of the evaluated value of the cell at the
	 * given coordinates. The coordinates are domain-independent.
	 * 
	 * @param row
	 *            The row coordinate
	 * @param col
	 *            The column coordinate
	 * @return The textual representation of value of the cell
	 * @throws Exception
	 *             If the cell cannot be evaluated
	 */
	// @requires row >= 0 && col >= 0
	public abstract String getValueAt(int row, int col) throws Exception;

	/**
	 * Sets the content of the cell at the given coordinates. The coordinates
	 * are domain-independent.
	 * 
	 * @param row
	 *            The row coordinate
	 * @param col
	 *            The column coordinate
	 * @param value
	 *            The new content (will be parsed)
	 * @throws Exception
	 *             If the content cannot be validly parsed
	 */
	// @requires row >= 0 && col >= 0
	public abstract void setContentAt(int row, int col, String value)
			throws Exception;

	/**
	 * Export the current workbook to HTML.
	 * 
	 * @param selectedFile
	 *            The destination file
	 */
	public abstract void sheetExportAsHTML(File selectedFile);

	/**
	 * Export the current workbook to OpenDocument Format (OpenOffice.org).
	 * 
	 * @param selectedFile
	 *            The destination file
	 * @throws Exception
	 *             If the export fails.
	 */
	public abstract void sheetExportAsOpenOffice(File selectedFile)
			throws Exception;

	/**
	 * Delete the row of the selected cell.
	 */
	public abstract void deleteRow();

	/**
	 * Delete the column of the selected cell.
	 */
	public abstract void deleteColumn();

	/**
	 * Insert a row above that of the selected cell.
	 */
	public abstract void insertRowBefore();

	/**
	 * Insert a row below that of the selected cell.
	 */
	public abstract void insertRowAfter();

	/**
	 * Insert a column to the left of that of the selected cell.
	 */
	public abstract void insertColumnBefore();

	/**
	 * Insert a column to the right of that of the selected cell.
	 */
	public abstract void insertColumnAfter();

	/**
	 * Set the cell at the given coordinates to be the currently selected cell.
	 * The coordinates are domain independent.
	 * 
	 * @param selectedRow
	 *            The row coordinate
	 * @param selectedColumn
	 *            The column coordinate
	 */
	//@requires selectedRow >= 0
	//@requires selectedColumn >= 0
	public abstract void selectCell(int selectedRow, int selectedColumn);

	/**
	 * Insert a sheet at the end of the current workbook.
	 * 
	 * @param name
	 *            The name for the new sheet.
	 */
	public abstract void insertSheetAtTheEnd(String name);

	/**
	 * Create a new workbook and switch to it. The previous workbook will go
	 * into a sort of limbo, as in it exists but the UI does not have a way to
	 * access it.
	 * 
	 * @param name
	 *            the name for the new workbook
	 */
	public abstract void newWorkbook(String name);

	/**
	 * Get the current sheet's name. This is for UI beautification (the
	 * "Rename sheet..." option shows the current name when promptin the user
	 * for a new one.
	 * 
	 * @return The current sheet's name
	 */
	public abstract String getCurrentSheetName();

	/**
	 * Set the current sheet's name to the given one.
	 * 
	 * @param name
	 *            The new name for the current sheet.
	 */
	public abstract void editSheetName(String name);
	
	
	/**
	 * Refreshes title
	 */
	public abstract void refreshTitle();
}

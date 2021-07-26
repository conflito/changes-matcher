package pt.ul.fc.di.dco000.services.exporters;

import java.io.File;
import java.util.Locale;

import org.odftoolkit.odfdom.doc.OdfSpreadsheetDocument;
import org.odftoolkit.odfdom.doc.table.OdfTable;
import org.odftoolkit.odfdom.doc.table.OdfTableCell;

/**
 * Class providing the service of exporting to ODF (Openffice) format.
 * Does not export charts.
 * @author jcraveiro
 */
public class ExportToOdf {

	/**
	 * The document 
	 */
	private final OdfSpreadsheetDocument document;
	
	/**
	 * The file to which the document will be written
	 */
	private final File file;
	
	/**
	 * The current table
	 */
	private OdfTable table;

	/**
	 * Creates a new ODF exporter
	 * @param file The destination file
	 * @throws ExportException
	 */
	public ExportToOdf (File file) throws ExportException {
		try {
			this.file = file;
			this.document = OdfSpreadsheetDocument.newSpreadsheetDocument();
			this.document.setLocale(Locale.ENGLISH);
			
			this.document.getTableList().get(0).remove();

		} catch (Exception e) {
			throw new ExportException(e);
		}
	}

	/**
	 * Makes the export effective, saving the document to file
	 * @throws ExportException
	 */
	public void close() throws ExportException {
		try {
			this.document.save(file);
		} catch (Exception e) {
			throw new ExportException(e);
		}
	}

	/**
	 * Saves a formula to a cell
	 * @param ref The textual reference to the cell
	 * @param cont The textual representation of the formula
	 * @see org.odftoolkit.odfdom.doc.table.OdfTableCell
	 */
	public void saveFormula(String ref, String cont) {
		final OdfTableCell cell = table.getCellByPosition(ref);
		cell.setFormula(cont);		
	}

	/**
	 * Saves text to a cell
	 * @param ref The textual reference to the cell
	 * @param cont The text
	 * @see org.odftoolkit.odfdom.doc.table.OdfTableCell
	 */
	public void saveText(String ref, String cont) {
		final OdfTableCell cell = table.getCellByPosition(ref);
		cell.setStringValue(cont);		
		
	}

	/**
	 * Saves a number to a cell
	 * @param ref The textual reference to the cell
	 * @param cont The textual representation of the number
	 */
	public void saveNumber(String ref, String cont) {
		final OdfTableCell cell = table.getCellByPosition(ref);
		cell.setDoubleValue(Double.parseDouble(cont));
	}

	/**
	 * Adds a sheet to the document being exported,
	 * and makes it the current sheet for export purposes.
	 * @param name The new sheet's name
	 */
	public void newSheet(String name) {
		table = OdfTable.newTable(document);
		table.setTableName(name);		
	}
	
	/**
	 * Returns a string representation of the object.	
	 * @return a string representation of the object. 
	 */
	public String toString() {
		return super.toString();
	}


}

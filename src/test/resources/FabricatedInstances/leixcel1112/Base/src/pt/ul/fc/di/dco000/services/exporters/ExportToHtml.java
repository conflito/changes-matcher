/**
 * 
 */
package pt.ul.fc.di.dco000.services.exporters;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;


/**
 * @author jcraveiro
 *
 */
public class ExportToHtml {
	
	/**
	 * The print writer which writes to the file
	 */
	private PrintWriter fw;
	
	/**
	 * The charts' exporter
	 */
	private final ExporterChartsToHtml exp;

	/**
	 * Creates a new HTML exporter, given the file to save
	 * to and the exporter responsible for any charts.
	 * @param file The file to save to
	 * @param exp The exporter responsible for any charts
	 * @throws ExportException If the export fails
	 */
	public ExportToHtml(File file, ExporterChartsToHtml exp) throws ExportException {
		try {
			fw = new PrintWriter(file);
			fw.println("<html>");
			this.exp = exp;
		} catch (Exception exc) {
			if (this.fw != null) {
				this.fw.close();
			}
			throw new ExportException(exc);
		}
	}
	
	/**
	 * Pretty-prints the workbook name to HTML
	 * @param name
	 */
	public void writeWorkbookName(String name) {
		fw.print("<head><title>");
		fw.print(name);
		fw.println("</title></head>");
		fw.println("<body>");
		fw.print("<h1>");
		fw.print(name);
		fw.println("</h1>");
	}
	
	/**
	 * Places the HTML code needed to begin a table (sheet)
	 * @param sheetName The sheet's name
	 */
	public void startTable(String sheetName){
		fw.print("<h2>");
		fw.print(sheetName);
		fw.println("</h2>");
		fw.println("<table width=\"100%\" border=\"1\">");
	}
	
	/**
	 * Places the HTML code needed to begin a line
	 */
	public void startLine() {
		fw.println("<tr>");
	}
	
	/**
	 * Pretty-prints a cell with its evaluated value.
	 * @param s The textual representation of the value
	 * @param pcWidth The desired relative (percentual) width for the cell
	 */
	//@requires pcWidth <= 100
	public void writeEvaluatedCell(String s, int pcWidth) {
		fw.print("\t<td width=\"");
		fw.print(pcWidth);
		fw.println("%\">");
		fw.print(s);
		fw.println("</td>");
	}
	

	/**
	 * Places the HTML code needed to end a line
	 */
	public void endLine() {
		fw.println("</tr>");
	}
	
	/**
	 * Places the HTML code needed to end a table (sheet)
	 */
	public void closeTable() {
			fw.println("</table>");
	}
	
	/**
	 * Makes the export effective, saving the document to file
	 * @throws ExportException
	 */
	public void close() throws ExportException {
		try {
			fw.println("</body></html>");
			
		} catch (Exception e) {
			throw new ExportException(e);
		} finally {
			fw.close();
		}
	}

	/**
	 * Pretty-prints a pie chart to HTML
	 * @param name The title for the chart
	 * @param axisX List of labels
	 * @param axisY List of values
	 */
	public void writePieChart(String name, List<String> axisX, List<Integer> axisY) {
		fw.println("<br /><br /><img border=\"0\" style=\"border: 0\" src=\"");
		fw.println(exp.htmlForPieChart(name, axisX,axisY));
		fw.println(" \"/>");
	}

	/**
	 * Pretty-prints a column chart to HTML
	 * @param name The title for the chart
	 * @param axisX List of labels
	 * @param axisY List of values
	 */
	public void writeColumnChart(String name, List<String> axisX,
			List<Object> axisY) {
		fw.println("<br /><br /><img border=\"0\" style=\"border: 0\" src=\"");
		fw.println(exp.htmlForColumnChart(name, axisX,axisY));
		fw.println(" \"/>");
	}
	
	/**
	 * Returns a string representation of the object.	
	 * @return a string representation of the object.
	 */
	public String toString() {
		return super.toString();
	}

}

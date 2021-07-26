package pt.ul.fc.di.dco000.leixcel;

import java.io.File;

import pt.ul.fc.di.dco000.domain.CellAddress;
import pt.ul.fc.di.dco000.domain.CellContent;
import pt.ul.fc.di.dco000.domain.Column;
import pt.ul.fc.di.dco000.domain.IWorkbook;
import pt.ul.fc.di.dco000.domain.Leixcel;
import pt.ul.fc.di.dco000.domain.Line;
import pt.ul.fc.di.dco000.domain.formats.ODFFormat;
import pt.ul.fc.di.dco000.ui.SheetUIDelegate;

/**
 * The Leixcel delegate. Establishes the "glue" between the UI and the domain.
 * @author fmartins
 * @author jcraveiro
 */
public class LeixcelSheetUIDelegate extends SheetUIDelegate {

	/**
	 * The domain instance
	 */
	private Leixcel leixcel;
	
	/**
	 * The current workbook
	 */
	private IWorkbook wb;

	/**
	 * The row of the last selected cell
	 */
	private int selectedRow;

	/**
	 * The column of the last selected cell
	 */
	private int selectedColumn;
	

	public LeixcelSheetUIDelegate(IWorkbook wb) {
		this.wb = wb;
	}

	public IWorkbook getCurrentWorkbook() {
		return wb;
	}

	private CellAddress computeRef(int rown, int coln) {
		Column col = computeColRef(coln);
		Line lin = computeRowRef(rown);
		return new CellAddress(col, lin);
	}

	/**
	 * Get the domain "Line" corresponding to the given UI row
	 * @param row
	 * @return
	 */
	//@requires row >= 0
	private Line computeRowRef(int row) {		
		return new Line(row+1);
	}
	
	/**
	 * Get the domain "Column" corresponding to the given UI column
	 * @param coln The column number
	 * @return
	 */
	//@requires coln >= 0
	private Column computeColRef(int coln) {		
		return Column.newColumn(coln+1);
	}

	@Override
	public String getValueAt(int row, int col) {
		return wb.getCurrent().getValue(computeRef(row, col)).toString();
	}

	@Override
	public String getContentAt(int row, int col) {
		CellContent cc = wb.getCurrent().getContent(computeRef(row, col));
		if (cc != null)
			return wb.getCurrent().getContent(computeRef(row, col)).toString();
		else
			return null;
	}

	@Override
	public void setContentAt(int row, int col, String value) throws Exception {
		CellAddress ca = computeRef(row, col);
		wb.getCurrent().editCell(ca.getColumn(), ca.getLine(), value);
		refreshSheetStructure();
	}


	@Override
	public void sheetExportAsOpenOffice(File selectedFile) throws Exception {
		ODFFormat.INSTANCE.exportWorkbook(wb, selectedFile);
	}

	@Override
	public void deleteRow() {
		wb.getCurrent().removeLine(computeRowRef(selectedRow));
		refreshSheetStructure();
	}

	@Override
	public void deleteColumn() {
		wb.getCurrent().removeColumn(computeColRef(selectedColumn));
		refreshSheetStructure();
	}

	@Override
	public void insertRowBefore() {
		wb.getCurrent().insertLine(computeRowRef(selectedRow));
		refreshSheetNewline(selectedRow);
	}

	@Override
	public void insertRowAfter() {
		wb.getCurrent().insertLine(computeRowRef(selectedRow).suc());
		refreshSheetNewline(selectedRow+1);
	}

	@Override
	public void insertColumnBefore() {
		wb.getCurrent().insertColumn(computeColRef(selectedColumn));
		refreshSheetNewcol(selectedColumn);
	}

	@Override
	public void insertColumnAfter() {
		wb.getCurrent().insertColumn(computeColRef(selectedColumn).suc());
		refreshSheetNewcol(selectedColumn+1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ui.SheetUIDelegate#selectCell(int, int)
	 */
	@Override
	public void selectCell(int selectedRow, int selectedColumn) {
		this.selectedRow = selectedRow;
		this.selectedColumn = selectedColumn;
	}

	@Override
	public void insertSheetAtTheEnd(String name) {
		wb.insertSpreadsheet(name);
		refreshTitle();
		refreshSheetStructure();
		
	}
	
	@Override
	public void newWorkbook(String name) {
		
		wb = leixcel.getController().getNewWorkbook();
		wb.setName(name);
		refreshTitle();
		refreshSheetStructure();
	}

	@Override
	public String getCurrentSheetName() {
		return wb.getCurrent().getName();
	}

	@Override
	public void editSheetName(String name) {
		wb.getCurrent().setName(name);		
	}
	
	@Override
	public void refreshTitle() {
		sheetUI.setTitle(
				"LEIxcel » " +
						wb.getName() +
						" » "+
						getCurrentSheetName()				
				);
		
	}

}

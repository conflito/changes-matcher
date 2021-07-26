package pt.ul.fc.di.dco000.ui.swing;

import javax.swing.table.AbstractTableModel;

import pt.ul.fc.di.dco000.domain.IWorkbook;
import pt.ul.fc.di.dco000.ui.SheetUIDelegate;


public class SheetTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2639669988262792352L;

	private final SheetUIDelegate tableModelDelegate;

	public SheetTableModel(SheetUIDelegate tableModelDelegate) {
		this.tableModelDelegate = tableModelDelegate;
	}

	@Override
	public int getColumnCount() {
		final IWorkbook wb = tableModelDelegate.getCurrentWorkbook();
		return wb.getCurrent().getColumns().size();
	}

	@Override
	public int getRowCount() {
		final IWorkbook wb = tableModelDelegate.getCurrentWorkbook();
		return wb.getCurrent().getLines().size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		try {
			return tableModelDelegate.getValueAt(row, col);
		} catch (Exception e) {
			return "ERROR";
		}
		//return "a";
	}

	@Override
	public boolean isCellEditable(int row, int col)
	{ 
		return true;
	}

	public void setValueAt(Object value, int row, int col) {
		try {
			tableModelDelegate.setContentAt(row, col, (String) value);
			// passar info para o modelo
			fireTableCellUpdated(row, col);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

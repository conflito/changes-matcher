/**
 * 
 */
package pt.ul.fc.di.dco000.ui.swing;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;

import pt.ul.fc.di.dco000.ui.SheetUIDelegate;


/**
 * @author fmartins
 *
 */
public class CellEditorUI extends DefaultCellEditor  {
    
	private static final long serialVersionUID = -2636145695014600684L;
	private final SheetUIDelegate sheetUIDelegate;

    /**
	 * 
	 */
	public CellEditorUI(SheetUIDelegate sheetUIDelegate) {
		super (new JTextField());
		this.sheetUIDelegate = sheetUIDelegate;
	}
    
	@Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int col) {
		super.getTableCellEditorComponent(table, value, isSelected, row, col);
		delegate.setValue(sheetUIDelegate.getContentAt(row, col));
        return editorComponent;
    }
}
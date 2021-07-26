package pt.ul.fc.di.dco000.ui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import pt.ul.fc.di.dco000.ui.SheetUIDelegate;


public class EditController {

	private final SheetUIDelegate sheetUIDelegate;
	private final SheetUI sheetUI;

	public EditController(SheetUIDelegate sheetUIDelegate, SheetUI sheetUI) {
		this.sheetUIDelegate = sheetUIDelegate;
		this.sheetUI = sheetUI;
	}

	
	public ActionListener deleteRowController() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sheetUIDelegate.deleteRow();
			}
		};
	}

	public ActionListener deleteColumnController() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sheetUIDelegate.deleteColumn();
			}
		};
	}

	public ActionListener editSheetNameController() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				final String currentName = sheetUIDelegate.getCurrentSheetName();
				final String name = JOptionPane.showInputDialog(sheetUI, "Insert new name for current sheet:",currentName);
				sheetUIDelegate.editSheetName(name);
			}
		};
	}
	
}



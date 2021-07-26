package pt.ul.fc.di.dco000.ui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import pt.ul.fc.di.dco000.ui.SheetUIDelegate;


public class InsertController {

	private final SheetUIDelegate sheetUIDelegate;
	private final SheetUI sheetUI;

	public InsertController(SheetUIDelegate sheetUIDelegate, SheetUI sheetUI) {
		this.sheetUIDelegate = sheetUIDelegate;
		this.sheetUI = sheetUI;
	}
	
	public ActionListener insertRowBeforeController() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				sheetUIDelegate.insertRowBefore();
			}
		};
	}

	public ActionListener insertRowAfterController() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				sheetUIDelegate.insertRowAfter();
			}
		};
	}

	
	public ActionListener insertColumnBeforeController() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				sheetUIDelegate.insertColumnBefore();
			}
		};
	}

	public ActionListener insertColumnAfterController() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				sheetUIDelegate.insertColumnAfter();
			}
		};
	}

	public ActionListener insertSheetAtTheEndController() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				final String name = JOptionPane.showInputDialog(sheetUI, "Insert name for new sheet:");
				sheetUIDelegate.insertSheetAtTheEnd(name);
			}
		};
	}	
}



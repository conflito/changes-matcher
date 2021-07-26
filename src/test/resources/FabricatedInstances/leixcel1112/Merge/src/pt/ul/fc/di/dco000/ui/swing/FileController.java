package pt.ul.fc.di.dco000.ui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import pt.ul.fc.di.dco000.ui.SheetUIDelegate;


public class FileController {

	private final SheetUIDelegate sheetUIDelegate;
	private final SheetUI sheetUI;

	public FileController(SheetUIDelegate sheetUIDelegate, SheetUI sheetUI) {
		this.sheetUIDelegate = sheetUIDelegate;
		this.sheetUI = sheetUI;
	}

	public ActionListener exportAsOpenOfficeController() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				final JFileChooser fc = new JFileChooser();
				final FileFilter filter = new FileNameExtensionFilter("OpenDocument spreadsheet (*.ods)", "ods");
				fc.setFileFilter(filter);
				fc.setAcceptAllFileFilterUsed(false);

				final int returnVal = fc.showSaveDialog(sheetUI);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					try {
						sheetUIDelegate.sheetExportAsOpenOffice(fc.getSelectedFile());
					} catch (Exception e) {
						e.printStackTrace();
						/*
						 * Yep, this just prints the exception's stack trace.
						 */
					}
				}
			}
		};
	}

	public ActionListener exportAsHTMLController() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				final JFileChooser fc = new JFileChooser();
				final FileFilter filter = new FileNameExtensionFilter("HTML document (*.htm, *.html)", "htm", "html");
				fc.setFileFilter(filter);
				fc.setAcceptAllFileFilterUsed(false);

				final int returnVal = fc.showSaveDialog(sheetUI);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					sheetUIDelegate.sheetExportAsHTML(fc.getSelectedFile());
				}
			}
		};
	}

	public ActionListener newWorkbookController() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				final String name = JOptionPane.showInputDialog(sheetUI,
						"Insert name for new workbook:");
				sheetUIDelegate.newWorkbook(name);
			}
		};
	}

	public ActionListener quitController() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		};
	}

}

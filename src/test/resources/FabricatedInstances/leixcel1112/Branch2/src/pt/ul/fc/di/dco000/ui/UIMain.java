/**
 * The main UI classe
 * 
 * @author fmartins
 * @author jcraveiro
 */

package pt.ul.fc.di.dco000.ui;

import java.awt.EventQueue;

import pt.ul.fc.di.dco000.ui.swing.SheetUI;


public class UIMain {

	/**
	 * Launch user interface.
	 * @param tableModelDelegate 
	 */
//	public static void run(final BookshelfUIDelegate bookshelfDelegate, 
//			final DocumentUIDelegate documentViewerDelegate, 
//			final DocumentMetadataUIDelegate documentMetadataUIDelegate) {
	public static void run(final SheetUIDelegate tableModelDelegate) {		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//BookshelfUI frame = new BookshelfUI(bookshelfDelegate, 
					//		documentViewerDelegate, documentMetadataUIDelegate);
					final SheetUI frame = new SheetUI(tableModelDelegate);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}

package pt.ul.fc.di.dco000.leixcel;

import pt.ul.fc.di.dco000.domain.IController;
import pt.ul.fc.di.dco000.domain.IWorkbook;
import pt.ul.fc.di.dco000.domain.Leixcel;
import pt.ul.fc.di.dco000.ui.UIMain;

/**
 * The main project class
 * 
 * @author fmartins
 * @author jcraveiro
 * 
 */
public class LeixcelMain {

	public static void main(String[] args) {
		// Creates the initial object
		Leixcel leixcel = new Leixcel();
		
		// Get the controllers
		IController controller = leixcel.getController();
		IWorkbook wb = controller.getNewWorkbook();
		
		//Creates the delegates
		LeixcelSheetUIDelegate sheetDelegate = new LeixcelSheetUIDelegate(wb);
		
		// Executes the start up case and starts the UI
		UIMain.run(sheetDelegate);
	}
}

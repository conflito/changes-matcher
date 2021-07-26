/**
 * 
 */
package pt.ul.fc.di.dco000.domain.formats;

import java.io.File;

import pt.ul.fc.di.dco000.domain.IWorkbook;
import pt.ul.fc.di.dco000.services.exporters.ExportException;

/**
 * @author jcraveiro
 *
 */
public interface Format {
	public String getName();
	void exportWorkbook(IWorkbook l, File file) 
			throws  java.lang.UnsupportedOperationException, ExportException; 
	IWorkbook importWorkbook(File file) 
			throws java.lang.UnsupportedOperationException; 
}

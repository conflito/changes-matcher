/**
 * 
 */
package pt.ul.fc.di.dco000.domain.formats;

import java.io.File;

import pt.ul.fc.di.dco000.domain.CellAddress;
import pt.ul.fc.di.dco000.domain.CellContent;
import pt.ul.fc.di.dco000.domain.Column;
import pt.ul.fc.di.dco000.domain.Formula;
import pt.ul.fc.di.dco000.domain.IWorkbook;
import pt.ul.fc.di.dco000.domain.Line;
import pt.ul.fc.di.dco000.domain.Spreadsheet;
import pt.ul.fc.di.dco000.domain.XNumber;
import pt.ul.fc.di.dco000.services.exporters.ExportException;
import pt.ul.fc.di.dco000.services.exporters.ExportToOdf;

/**
 * @author jcraveiro
 * 
 * The instance of this class exports the workbook
 * to the ODF format. Charts are not covered.
 * 
 * @version $Revision: 1.0 $
 */

public enum ODFFormat implements Format{

    INSTANCE;
    /* (non-Javadoc)
     * @see domain.FormatoEscrita#write(domain.Livro)
     */
    @Override
    public void exportWorkbook(IWorkbook wb, File file) throws ExportException {
        ExportToOdf exporter = new ExportToOdf(file);

        for (Spreadsheet f : wb) {
            System.err.println("Exporting "+f.getName());
            exporter.newSheet(f.getName());
            for (Line l: f.getLines()) {
                for (Column c: f.getColumns() ){
                    CellAddress ref = new CellAddress(c,l);
                    CellContent val = f.getContent(ref);

                    if (val != null) {
                        if (val instanceof Formula) {
                            exporter.saveFormula(ref.toString(), val.toString());
                        } else {
                            if (val instanceof XNumber) {
                                exporter.saveNumber(ref.toString(),
                                        val.toString());
                            } else {
                                exporter.saveText(ref.toString(),
                                        val.toString());
                            }
                        }
                    }
                }
            }
        }

        exporter.close();

    }

    @Override
    public String getName() {
        return "ODF";
    }
    
	@Override
	public String toString() {
		return "ODF";
	}

    @Override
    public IWorkbook importWorkbook(File file)
    throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
}

/**
 * 
 */
package pt.ul.fc.di.dco000.domain.formats;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pt.ul.fc.di.dco000.domain.CellAddress;
import pt.ul.fc.di.dco000.domain.Column;
import pt.ul.fc.di.dco000.domain.IWorkbook;
import pt.ul.fc.di.dco000.domain.Line;
import pt.ul.fc.di.dco000.domain.PrimitiveValue;
import pt.ul.fc.di.dco000.domain.SpecialValue;
import pt.ul.fc.di.dco000.domain.Spreadsheet;
import pt.ul.fc.di.dco000.domain.charts.IChart;
import pt.ul.fc.di.dco000.domain.charts.PieChart;
import pt.ul.fc.di.dco000.services.exporters.ExportToHtml;
import pt.ul.fc.di.dco000.services.exporters.ExporterChartsToHtml;
import pt.ul.fc.di.dco000.services.exporters.ExportersFactory;


/**
 * @author jcraveiro
 * @author mal
 * 
 * The instance of this class exports the workbook
 * to the HTML format.
 * 
 * @version $Revision: 1.0 $
 * 
 */
public enum HTMLFormat implements Format{
	INSTANCE;

	@Override
	public void exportWorkbook(IWorkbook wb, File file) {
		try {
			ExporterChartsToHtml expCharts = 
				ExportersFactory.INSTANCE.getChartsToHtmlExporter();
			
			ExportToHtml exporter = new ExportToHtml(file,expCharts);
			exporter.writeWorkbookName(wb.getName());
			for (Spreadsheet f : wb) {
				exporter.startTable(f.getName());
				int pcWidth = 100 / f.getColumns().size();
				for (Line l: f.getLines()) {
					exporter.startLine();
					for (Column c: f.getColumns() ){
						PrimitiveValue<?> val = f.getValue(new CellAddress(c,l));
						if (val.equals(SpecialValue.DEFAULT))
							exporter.writeEvaluatedCell("&nbsp;",pcWidth);
						else
							exporter.writeEvaluatedCell(val.toString(),pcWidth);
					}
					exporter.endLine();
				}
				exporter.closeTable();
			}
			
			for (IChart c: wb.getCharts()){
				String name = c.getName();

				List<String> xAxis = new ArrayList<String>();
				for(PrimitiveValue<?> val: c.getXAxis().eval()){
					xAxis.add(val.toString());
				}

				if (c instanceof PieChart){
					List<Integer> yAxis = new ArrayList<Integer>();
					for(PrimitiveValue<?> val: c.getYAxis().eval()){
						yAxis.add((Integer)val.nativeValue());
					}
					exporter.writePieChart(name,xAxis,yAxis);
				}	
				else
					//TODO  not yet supported
					;
			}
			
			exporter.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String getName() {
		return "HTML";
	}
	
	@Override
	public String toString() {
		return "HTML";
	}


	@Override
	public IWorkbook importWorkbook(File file)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
}

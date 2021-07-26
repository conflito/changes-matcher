package pt.ul.fc.di.dco000.domain;

import pt.ul.fc.di.dco000.domain.charts.ChartTypesCatalog;
import pt.ul.fc.di.dco000.domain.charts.PieChart;
import pt.ul.fc.di.dco000.domain.formats.Format;
import pt.ul.fc.di.dco000.domain.formats.FormatCatalog;
import pt.ul.fc.di.dco000.domain.formats.HTMLFormat;
import pt.ul.fc.di.dco000.domain.formats.ODFFormat;

public class Leixcel {

	private ChartTypesCatalog chartCatalog; 
	private IController controller;
	private FormatCatalog formatCatalog;
	

	public Leixcel() {
		chartCatalog = loadChartTypes();
		formatCatalog = loadFormats();
		controller = new Controller(chartCatalog,formatCatalog);
	}

	private ChartTypesCatalog loadChartTypes() {
		ChartTypesCatalog cCatalog = new ChartTypesCatalog();
		cCatalog.put("PIE", PieChart.class);
		return cCatalog;
	}
	
	private FormatCatalog loadFormats() {
		FormatCatalog fCatalog = new FormatCatalog();
		
		fCatalog.put(HTMLFormat.INSTANCE);
		
		fCatalog.put(ODFFormat.INSTANCE);
		return fCatalog;
	}

	public IController getController() {
		return controller;
	}

	public String toString() {
		//return "[charts: "+ chartCatalog + ;
		return "[formats: "+ formatCatalog + "]";
	}


}

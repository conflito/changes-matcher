package pt.ul.fc.di.dco000.domain;

import pt.ul.fc.di.dco000.domain.charts.IChart;
import pt.ul.fc.di.dco000.domain.formats.Format;

public interface IController {
	public IWorkbook getNewWorkbook();
	public void quit();
	public boolean loadFunction(String string, String string2);
	public Class<? extends IChart> getChartType(String string);
	public Format getFormat(String string);
}

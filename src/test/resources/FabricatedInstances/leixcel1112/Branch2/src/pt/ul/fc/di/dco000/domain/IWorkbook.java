package pt.ul.fc.di.dco000.domain;

import java.io.File;

import pt.ul.fc.di.dco000.domain.charts.IChart;
import pt.ul.fc.di.dco000.domain.formats.Format;

public interface IWorkbook extends Iterable<Spreadsheet>{

	public void insertSpreadsheet(String name);

	public Spreadsheet getCurrent();

	public void setName(String name);

	public int numberSheets();

	public boolean editCell(Column c, Line l, String string);

	public void insertLine(Line line);

	public void removeLine(Line line);

	public void removeColumn(Column column);

	public void insertColumn(Column column);

	public String getName();

	public void export(Format f, File file);
	
	public Iterable<IChart> getCharts();

	public boolean insertChart(Class<? extends IChart> cl, String string, Column column, Line line,
			Line line2, Column column2, Line line3, Line line4);
}

package pt.ul.fc.di.dco000.domain;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.ul.fc.di.dco000.domain.charts.IChart;
import pt.ul.fc.di.dco000.domain.formats.Format;


public class Workbook implements IWorkbook{

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("Workbbok: "+name+"\n");
		for (Spreadsheet sp: sheets)
			result.append(sp.toString()+"\n \n");
		return result.toString();
	}

	private List<Spreadsheet> sheets;
	private String name;
	private Spreadsheet current;

	public Workbook(String string) {
		sheets = new ArrayList<Spreadsheet>();
		name = string;	
	}

	@Override
	public Iterator<Spreadsheet> iterator() {
		return sheets.iterator();
	}

	public void insertSpreadsheet(String name) {
		Spreadsheet s = new Spreadsheet(name);
		sheets.add(s);
		current = s;
	}

	public Spreadsheet getCurrent(){
		return current;
	}

	public Spreadsheet next(){
		int next = sheets.indexOf(current);
		return sheets.get(next+1);
	}

	@Override
	public void setName(String name) {
		this.name = name;	
	}

	@Override
	public int numberSheets() {
		return sheets.size();
	}

	@Override
	public boolean editCell(Column c, Line l, String content) {
		return current.editCell(c, l, content);
	}

	@Override
	public void insertLine(Line line) {
		current.insertLine(line);
	}

	@Override
	public void insertColumn(Column column) {
		current.insertColumn(column);

	}

	@Override
	public void removeColumn(Column column) {
		current.removeColumn(column);
	}

	@Override
	public void removeLine(Line line) {
		current.removeLine(line);		
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void export(Format f, File file) {
		try {
			f.exportWorkbook(this, file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean insertChart(Class<? extends IChart> cl, String string, Column column, Line line,
			Line line2, Column column2, Line line3, Line line4) {
		
		return current.insertChart(cl,string, column, line, line2, column2, line3, line4);
	}

	@Override
	public Iterable<IChart> getCharts() {
		return current.getCharts();
	}

}

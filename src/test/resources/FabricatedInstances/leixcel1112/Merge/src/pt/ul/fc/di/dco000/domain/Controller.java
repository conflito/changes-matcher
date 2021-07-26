package pt.ul.fc.di.dco000.domain;

import java.util.ArrayList;
import java.util.List;

import pt.ul.fc.di.dco000.domain.charts.ChartTypesCatalog;
import pt.ul.fc.di.dco000.domain.charts.IChart;
import pt.ul.fc.di.dco000.domain.formats.Format;
import pt.ul.fc.di.dco000.domain.formats.FormatCatalog;


public class Controller implements IController{
	private IWorkbook currentWb; 
	private List<IWorkbook> wbs;
	private int numWb;
	private ChartTypesCatalog chartCatalog;
	private FormatCatalog formatCatalog;


	public Controller(ChartTypesCatalog ccatalog, FormatCatalog fcatalog) {
		this.chartCatalog = ccatalog;
		this.formatCatalog = fcatalog;
		wbs = new ArrayList<IWorkbook>();
		numWb = 1;
	}

	public IWorkbook getNewWorkbook() {
		currentWb = new Workbook("Workbook"+numWb);
		numWb++;
		wbs.add(currentWb);
		insertNewSpreadSheet();
		return currentWb;
	}

	public void insertNewSpreadSheet() {
		currentWb.insertSpreadsheet("Sheet"+(currentWb.numberSheets()+1));
	}


	public void setNameCurrentWorkbook(String name){
		currentWb.setName(name);
	}

	@Override
	public void quit() {
		// TODO Auto-generated method stub
		// save all workbooks
	}

	@Override
	public boolean loadFunction(String name, String className) {
		return  FunctionCatalog.INSTANCE.insert(name,className);
	}

	@Override
	public Class<? extends IChart> getChartType(String string) {
		return chartCatalog.get(string);
	}

	@Override
	public Format getFormat(String string) {
		return formatCatalog.getFormat(string);
	}
}

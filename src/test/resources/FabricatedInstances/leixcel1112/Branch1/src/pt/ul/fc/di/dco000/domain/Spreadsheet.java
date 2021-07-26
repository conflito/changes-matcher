package pt.ul.fc.di.dco000.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import pt.ul.fc.di.dco000.domain.charts.IChart;
import pt.ul.fc.di.dco000.domain.charts.VerticalRange;
import pt.ul.fc.di.dco000.domain.expressions.ContentParser;

public class Spreadsheet{

	private static final int LINES_DEFAULT = 5;
	private static final int COLUMNS_DEFAULT = 5;

	private String name;
	private final CellCatalog cells;
	private final SortedSet<Line> lines;
	private final SortedSet<Column> columns;

	private final List<IChart> charts;


	public Spreadsheet(String name){
		this.name = name;
		this.cells = new CellCatalog();

		this.lines = new TreeSet<Line>();
		Line l = Line.FIRST;
		for (int i = 1; i <= LINES_DEFAULT; i++){
			lines.add(l);
			l = l.suc();
		}

		this.columns = new TreeSet<Column>();	
		Column c = Column.FIRST;
		for (int i = 1; i <= COLUMNS_DEFAULT; i++){
			columns.add(c);
			c = c.suc();
		}
		this.charts = new ArrayList<IChart>();
	}

	public boolean editCell(Column c, Line l, String content){
		ContentParser parser = new ContentParser(this);
		CellAddress a = new CellAddress(c,l);
		CellContent cc = parser.parse(content,a);
		//System.out.println("Parsed content " + cc);
		if (cc == null || cc.equals(SpecialValue.ERROR))
			return false;
		Cell cell = cells.getCell(a);		
		if (cell == null){
			cell = new Cell(a,cc);	
			cells.add(cell);
		}
		else{
			cell.setContent(cc);	
		}
//		System.out.println(cells);
		return true;
	}
	
	public void insertLine(Line line){
		lines.add(lines.last().suc());
		cells.moveDown(line);
	}	

	public void removeLine(Line line){
		lines.remove(lines.last());
		cells.removeLine(line);
		cells.moveUp(line);
	}	
	
	public void insertColumn(Column column){
		columns.add(columns.last().suc());
		cells.moveRight(column);
	}
	
	public void removeColumn(Column column){
		columns.remove(columns.last());
		cells.removeColumn(column);
		cells.moveLeft(column);
	}

	public String toString(){
		StringBuilder result = new StringBuilder();
		
		result.append("   Spreadsheet: " + name + "\n \n \t");
		result.append("\t");
		for (Column c: this.columns ){
			result.append(c.toString()+"\t");
		}
		
		result.append("\n");
		for (Line l: this.lines ){
			result.append("\t"+l.toString()+"\t");
			for (Column c: this.columns ){
				Cell cell = cells.getCell(new CellAddress(c,l));
				if (cell != null){
					result.append(cell.getValue()+ "\t");
				}
				else{
					result.append(" \t");
				}
			}
			result.append("\n");
		}
		result.append("\n");
		result.append("\t Cell's content \n"+cells.toString()+"\n");
		for(IChart ch: charts)
			result.append(ch+"\n");
		
		return result.toString();
	}

	public SortedSet<Line> getLines() {
		return lines;
	}

	public SortedSet<Column> getColumns() {
		return columns;
	}

	public PrimitiveValue<?> getValue(CellAddress address) {
		Cell cell = cells.getCell(address);
		if (cell != null){
			return cell.getValue();
		}
		else{
			return SpecialValue.DEFAULT;
		}
	}

	public CellContent getContent(CellAddress address) {
		Cell cell = cells.getCell(address);
		if (cell != null){
			return cell.getContent();
		}
		else{
			return null;
		}
	}

	private void insertLinesUpTo(Line line) {
		Line l = lines.last().suc();
		while ( l.compareTo(line) <=0 ){
			lines.add(l);
			//cells.moveDown(l); // nao sera preciso
			l = l.suc();
		}		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private void insertColumnsUpTo(Column column) {
		Column c = columns.last().suc();
		while (c.compareTo(column) <=0 ){
			columns.add(c);
			// cells.moveRight(c); // nao sera preciso
			c = c.suc();
		}
	}

	public CellAddress getCellAddress(Column c, Line l) {
		CellAddress address = new CellAddress(c,l);
		Cell cell = cells.getCell(address);
		if (cell != null)
			address = cell.getAddress();
		else{
			makeUsedCellAddress(address);
			//System.out.println(cells);
		}
		return address;
	}
	
	private void makeUsedCellAddress(CellAddress address) {
		Cell cell = cells.getCell(address);
		if (cell == null){
			cells.add(new Cell(address));
			if (!columns.contains(address.getColumn())){
				insertColumnsUpTo(address.getColumn());
			}
				
			if (!lines.contains(address.getLine()))
				insertLinesUpTo(address.getLine());
		}
	}

	 public Cell getCell(CellAddress address) {
		return cells.getCell(address);
	}


	public boolean insertChart(Class<? extends IChart> cl, String name, Column column, Line line,
			Line line2, Column column2, Line line3, Line line4) {
		VerticalRange x = new VerticalRange(this,column,line,line2);
		VerticalRange y = new VerticalRange(this,column2,line3,line4);
		try {
			IChart ch = cl.newInstance();
			ch.setName(name);
			ch.setXAxis(x);
			ch.setYAxis(y);
			if (ch.valid()){
				charts.add(ch);
				return true;
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public Iterable<IChart> getCharts() {
		return charts;
	}

}

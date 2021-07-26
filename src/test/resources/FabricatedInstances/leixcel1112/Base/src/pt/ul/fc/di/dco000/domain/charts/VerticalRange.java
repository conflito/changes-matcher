package pt.ul.fc.di.dco000.domain.charts;

import java.util.ArrayList;
import java.util.List;

import pt.ul.fc.di.dco000.adts.Interval;
import pt.ul.fc.di.dco000.domain.CellAddress;
import pt.ul.fc.di.dco000.domain.Column;
import pt.ul.fc.di.dco000.domain.Line;
import pt.ul.fc.di.dco000.domain.PrimitiveValue;
import pt.ul.fc.di.dco000.domain.Spreadsheet;


public class VerticalRange{

	private final Spreadsheet sp;
	private final CellAddress aMin;
	private final CellAddress aMax;
	private final int dim;
	
	public VerticalRange(Spreadsheet sp, Column c, Line min, Line max) {
		this.sp = sp;
		this.aMin = sp.getCellAddress(c, min);
		this.aMax = sp.getCellAddress(c, max);
		this.dim = eval().size();
	}

	public List<CellAddress> elements() {
		List<CellAddress> result = new ArrayList<CellAddress> ();
		for(Line line: new Interval<Line>(aMin.getLine(),aMax.getLine()))
			result.add(sp.getCellAddress(aMin.getColumn(), line));
		return result;
	}
	
	public List<PrimitiveValue<?>> eval() {
		List<PrimitiveValue<?>> result = new ArrayList<PrimitiveValue<?>> ();
		for(CellAddress a: elements())
			result.add(sp.getValue(a));
		return result;
	}

	public int getDimension(){
		return dim;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return eval().toString() + "<->" + elements().toString();
	}

}

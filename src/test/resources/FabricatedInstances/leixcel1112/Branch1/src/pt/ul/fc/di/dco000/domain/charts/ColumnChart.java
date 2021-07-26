package pt.ul.fc.di.dco000.domain.charts;

public class ColumnChart extends AbsChart{

	protected ColumnChart(String name, VerticalRange x, VerticalRange y) {
		super(name, x, y);
	}

	@Override
	public String toString() {
		return "ColumnChart: "+super.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		return super.equals(obj);
	}

	@Override
	public boolean valid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}


}

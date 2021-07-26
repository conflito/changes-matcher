package pt.ul.fc.di.dco000.domain.charts;

import pt.ul.fc.di.dco000.domain.PrimitiveValue;

public class PieChart extends AbsChart{

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
	public String toString() {
		return "PieChart "+ getName() +"\n"+ super.toString();
	}

	public PieChart(String name, VerticalRange x, VerticalRange y) {
		super(name, x, y);
	}
	
	public PieChart() {
		super();
	}

	@Override
	//se y so tem inteiros
	public boolean valid() {
		if (this.getYAxis()==null || this.getXAxis()==null)
			return false;
		for(PrimitiveValue<?> val: this.getYAxis().eval()){
			if (! (val.nativeValue() instanceof Integer))
				return false;
		}
		return true; 
	}

}

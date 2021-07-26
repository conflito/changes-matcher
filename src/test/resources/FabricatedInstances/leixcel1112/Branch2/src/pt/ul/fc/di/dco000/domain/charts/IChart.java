package pt.ul.fc.di.dco000.domain.charts;

public interface IChart {

	public abstract String getName();

	public abstract VerticalRange getYAxis();

	public abstract void setYAxis(VerticalRange axis);

	public abstract VerticalRange getXAxis();

	public abstract void setXAxis(VerticalRange axis);

	public abstract int dimension();
	
	public abstract boolean valid();

	public abstract void setName(String name);

}
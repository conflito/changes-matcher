package pt.ul.fc.di.dco000.domain.charts;

public abstract class AbsChart implements IChart{

		private String name;
		private VerticalRange yAxis;
		private VerticalRange xAxis;
		private int dimension;
		
		//@requires x.dim() == y.dim() 
		protected AbsChart(String name, VerticalRange x, VerticalRange y) {
			super();
			this.name = name;
			xAxis = x;
			yAxis = y;
			this.dimension = x.getDimension();
		}

		protected AbsChart() {
			super();
		}

		/* (non-Javadoc)
		 * @see domain.charts.IChart2#getName()
		 */
		public String getName() {
			return name;
		}

		public int dimension(){
			return dimension;
		}
		
		public VerticalRange getYAxis() {
			return yAxis;
		}
		
		//@requires axis != null
		// && getXAxis()!=null ==> getXAxis().dimension()==axis.dimension()
		public void setYAxis(VerticalRange axis) {
			dimension = axis.getDimension();
			yAxis = axis;
		}

		/* (non-Javadoc)
		 * @see domain.charts.IChart2#getXAxis()
		 */
		public VerticalRange getXAxis() {
	
			return xAxis;
		}

		@Override
		public String toString() {
			return "x: "+xAxis+" y: "+yAxis;
		}

		/* (non-Javadoc)
		 * @see domain.charts.IChart2#setXAxis(domain.charts.VerticalRange)
		 */
		public void setXAxis(VerticalRange axis) {
			dimension = axis.getDimension();
			xAxis = axis;
		}
		public void setName(String name){
			this.name = name;
		}
}

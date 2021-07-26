package pt.ul.fc.di.dco000.services.exporters;

import java.util.Iterator;
import java.util.List;

import com.googlecode.charts4j.Color;
import static com.googlecode.charts4j.Color.BLACK;
import com.googlecode.charts4j.GCharts;
import com.googlecode.charts4j.PieChart;
import com.googlecode.charts4j.Slice;

/**
 * 
 * @author mal
 * @version $Revision: 1.0 $
 */
public class Charts4JAdapter implements ExporterChartsToHtml{

	/**
	 * Chart title font size (in points)
	 */
	private static final int TITLE_FONT_SIZE = 16;
	
	/**
	 * Chart width (in pixels)
	 */
	private static final int WIDTH = 500;
	
	/**
	 * Chart height (in pixels)
	 */
	private static final int HEIGHT = 200;


	/**
	 * The set of colors used for the charts
	 */
	private static final Color[] COLORS = 
	{Color.RED,Color.AZURE,Color.BURLYWOOD,Color.BLUE,Color.BROWN, 
		Color.CYAN, Color.YELLOW,Color.GOLD,Color.GREEN};
	
	
	/**
	 * The index for the next color to be used
	 */
	private int indexNextColor = 0;
	

	/**
	 * Generates the HTML for a pie chart.
	 * @param name The title for the chart
	 * @param axisX List of labels
	 * @param axisY List of values
	 * @return HTML code for a pie chart.
	 * @see pt.ul.fc.di.dco000.services.exporters.ExporterChartsToHtml#htmlForPieChart(String, List<String>, List<Integer>)
	 */
	@Override
	public String htmlForPieChart(String name, List<String> axisX, List<Integer> axisY) {
		final Slice[] slices = new Slice[axisX.size()];
		final Iterator<Integer> it = axisY.iterator();
		
		int i = 0;
		for(String s : axisX){
			slices[i] = Slice.newSlice(it.next(),COLORS[indexNextColor],s,s);
			indexNextColor = (indexNextColor + 1)% COLORS.length;
			i++;
		}
		
		final PieChart chart = GCharts.newPieChart(slices);
		chart.setTitle(name, BLACK, TITLE_FONT_SIZE);
		chart.setSize(WIDTH, HEIGHT);
		chart.setThreeD(true);
		return chart.toURLString();
	}
	
	/**
	 * Generates the HTML for a column chart.
	 * @param name The title for the chart
	 * @param axisX List of labels
	 * @param axisY List of values
	 * @return HTML code for a column chart.
	 * @see pt.ul.fc.di.dco000.services.exporters.ExporterChartsToHtml#htmlForColumnChart(String, List<String>, List<Object>)
	 */
	@Override
	public String htmlForColumnChart(String name, List<String> axisX,
			List<Object> axisY) {
		throw new UnsupportedOperationException("htmlForColumnChart");
		// TODO Auto-generated method stub

	}
	
	/**
	 * Returns a string representation of the object.
	 * @return a string representation of the object.  
	 */
	public String toString() {
		return super.toString();
	}

}

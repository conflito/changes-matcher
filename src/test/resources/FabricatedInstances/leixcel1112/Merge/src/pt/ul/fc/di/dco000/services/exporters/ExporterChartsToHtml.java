package pt.ul.fc.di.dco000.services.exporters;

import java.util.List;

/**
 * 
 * @author mal
 */
public interface ExporterChartsToHtml {

	/**
	 * Generates the HTML for a pie chart.
	 * @param name The title for the chart
	 * @param axisX List of labels
	 * @param axisY List of values
	 * @return HTML code for a pie chart.
	 */
	String htmlForPieChart(String name, List<String> axisX, List<Integer> axisY);
	
	/**
	 * Generates the HTML for a column chart.
	 * @param name The title for the chart
	 * @param axisX List of labels
	 * @param axisY List of values
	 * @return HTML code for a column chart.
	 */
	String htmlForColumnChart(String name, List<String> axisX, List<Object> axisY);
	
}

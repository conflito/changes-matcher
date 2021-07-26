package pt.ul.fc.di.dco000.services.exporters;

/**
 * 
 * @author mal
 */
public enum ExportersFactory {
	INSTANCE;

	/**
	 * Get a new charts exporter.
	 * @return a new charts exporter.
	 */
	public ExporterChartsToHtml getChartsToHtmlExporter() {
		return new Charts4JAdapter();
	}

}

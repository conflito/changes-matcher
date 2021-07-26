	package business.event;


/**
 * Enum of the types of Events
 */

public enum EventType {

	TeteATete("TeteATete",6,true), 
	BandoSentado("BandoSentado",1000, true), 
	MultidaoEmPe("MultidaoEmPe",500000, false);
	
	// the audience for the type
	private final int maxAudience;
	
	// if the type is seated or not
	private final boolean seated;
	private final String name;

	private EventType(String name,int maxAudience, boolean seated) {
		this.name = name;
		this.maxAudience = maxAudience;
		this.seated = seated;
	}
	
	/**
	 * Gets the max audience of the type
	 * @return the max audience
	 */
	public int getMaxAudience() {
		return maxAudience;
	}

	public String getName() {
		return this.name;
	}

	/**
	 * Checks if the type is seated or not
	 * @return if the type is seated or not
	 */
	public boolean isSeated() {
		return seated;
	}

}

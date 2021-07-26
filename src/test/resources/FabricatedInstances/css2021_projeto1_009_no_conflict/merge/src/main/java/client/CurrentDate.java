package client;

import java.time.LocalDate;



public class CurrentDate {

	
	private CurrentDate() {
		
	}
	
	
	public static LocalDate currentDateMock() {
		return LocalDate.of(2021, 4, 1);
		
	}
}

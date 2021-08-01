/**
 * 
 */
package facade.dtos;

import business.event.Seat;

/**
 *
 */
public class SeatDto {
	
	@SuppressWarnings("unused")
	private String rowLetters;
	@SuppressWarnings("unused")
	private int seatNumber;

	public SeatDto() {
	}
	
	public SeatDto(String rowLetters, int seatNumber) {
		this.rowLetters = rowLetters;
		this.seatNumber = seatNumber;
	}
	
	public static SeatDto fromSeat(Seat seat) {
		return new SeatDto(seat.getRowLetters(), seat.getSeatNumber());
	}

	public String getRowLetters() {
		return rowLetters;
	}

	public void setRowLetters(String rowLetters) {
		this.rowLetters = rowLetters;
	}

	public int getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(int seatNumber) {
		this.seatNumber = seatNumber;
	}
}


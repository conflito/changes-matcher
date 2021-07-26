package pt.ul.fc.di.dco000.domain;

public class CellContentChangedEvent {

	private Cell cell;
	
	public CellContentChangedEvent(Cell cell) {
		this.cell = cell;
	}

	public Cell getCell(){
		return this.cell;
	}
}

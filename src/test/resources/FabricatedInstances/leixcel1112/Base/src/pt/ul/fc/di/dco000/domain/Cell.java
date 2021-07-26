package pt.ul.fc.di.dco000.domain;

import java.util.Observable;

public class Cell extends Observable{
	private CellAddress address;
	private CellContent content;
	
	public Cell(CellAddress a, CellContent cc) {
		this.address = a;
		this.content = cc;
	}

	public Cell(CellAddress a) {
		this.address = a;
		this.content = SpecialValue.DEFAULT;
	}

	public void setContent(CellContent cc) {
		this.content = cc;	
		this.setChanged();
		this.notifyObservers(new CellContentChangedEvent(this));
	}

	public boolean in(Line line) {
		return this.address.in(line);
	}

	public boolean in(Column column) {
		return this.address.in(column);
	}
	
	public boolean hasAddress(CellAddress a) {
		return address.equals(a); 
	}

	public boolean after(Line line) {
		return address.after(line);
	}

	public boolean after(Column column) {
		return address.after(column);
	}

	public boolean before(Column column) {
		return address.before(column);
	}
	
	public boolean before(Line line) {
		return address.before(line);
	}

	public void moveRight() {
		address.moveRight();
	}
	
	public void moveLeft() {
		address.moveLeft();
	}

	public void moveUp() {
	    if (!address.in(Line.FIRST))
	        address.moveUp();
	}

	public void moveDown() {
		address.moveDown();
	}
	
	public String toString(){
		return address+":"+content.getValue();
	}

	public PrimitiveValue<?> getValue() {
		return content.getValue();
	}

	public CellContent getContent() {
		return content;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cell other = (Cell) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		return true;
	}

	public CellAddress getAddress() {
		return address;
	}

    public void removed() {
        this.setChanged();
        this.notifyObservers(new CellRemovedEvent(this));
    }
}

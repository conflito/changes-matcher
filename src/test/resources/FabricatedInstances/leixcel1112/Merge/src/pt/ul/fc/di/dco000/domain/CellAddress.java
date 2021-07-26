package pt.ul.fc.di.dco000.domain;

public class CellAddress{
	
	private Line line;
	private Column column;
	
	public CellAddress(Column c, Line l){
		super();
		this.line = l;
		this.column = c;
	}
	public Line getLine() {
		return line;
	}

	public Column getColumn() {
		return column;
	}
	
	public boolean after(Column c) {
		return this.column.compareTo(c)>0;
	}
	public boolean before(Column c) {
		return this.column.compareTo(c)<0;
	}
	
	public boolean after(Line l) {
		return this.line.compareTo(l)>0;
	}
	
	public boolean before(Line l) {
		return this.line.compareTo(l)<0;
	}
	
	public boolean in(Line l) {
		return line.equals(l);
	}
	
	public boolean in(Column c) {
		return column.equals(c);
	}
	public void moveRight() {
		this.column = this.column.suc();
	}

	public void moveLeft() {
		this.column = this.column.pred();
	}

	public void moveUp() {
		this.line = this.line.pred();	
	}
	@Override
	public String toString() {
		return column+""+line;
	}
	public void moveDown() {
		this.line = this.line.suc();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((column == null) ? 0 : column.hashCode());
		result = prime * result + ((line == null) ? 0 : line.hashCode());
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
		CellAddress other = (CellAddress) obj;
		if (column == null) {
			if (other.column != null)
				return false;
		} else if (!column.equals(other.column))
			return false;
		if (line == null) {
			if (other.line != null)
				return false;
		} else if (!line.equals(other.line))
			return false;
		return true;
	}

}

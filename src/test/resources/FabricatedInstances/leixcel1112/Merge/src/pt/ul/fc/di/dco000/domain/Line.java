package pt.ul.fc.di.dco000.domain;

import pt.ul.fc.di.dco000.adts.SucAndPredessorable;

public class Line implements SucAndPredessorable<Line>{
	
	public static final Line FIRST = new Line(1);
	
	private final int id;

	/**
	 * 
	 * @param num
	 * @requires validNum(num)
	 */
	public Line(int num) {
		this.id = num;
	}
	
	@Override
	public boolean isFirst() {
		return this.equals(FIRST);
	}

	public int getLineId(){
		return this.id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Line other = (Line) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public Line suc() {
		return new Line(this.id + 1 );
	}

	@Override
	public Line pred() {
			return new Line(this.id - 1 );
	}

	@Override
	public int compareTo(Line other) {
		return (this.id - other.id);
	}
	
	public String toString(){
		return id +"" ;
	}

	public static boolean validNumber(int num) {
		return num >= 1;
	}
}

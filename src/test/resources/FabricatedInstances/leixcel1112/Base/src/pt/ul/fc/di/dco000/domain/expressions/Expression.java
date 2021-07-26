package pt.ul.fc.di.dco000.domain.expressions;

import pt.ul.fc.di.dco000.domain.Spreadsheet;

public abstract class Expression implements IExpression {

	private Spreadsheet sp;
	
	public Expression(Spreadsheet sp) {
		this.sp = sp;
	}
	
	public Spreadsheet getSpreadsheet(){
		return sp;
	}

	public Object clone() throws CloneNotSupportedException{
		return super.clone();
	}

}

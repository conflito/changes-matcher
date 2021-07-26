package pt.ul.fc.di.dco000.domain.expressions;

import pt.ul.fc.di.dco000.domain.CellAddress;
import pt.ul.fc.di.dco000.domain.PrimitiveValue;
import pt.ul.fc.di.dco000.domain.Spreadsheet;

public class VarExpression extends Expression{

	private CellAddress address;

	public VarExpression(Spreadsheet sp, CellAddress address) {
		super(sp);
		this.address = address;
	}

	@Override
	public <E> E accept(IExpressionVisitor<E> visitor) {
		return visitor.visit(this);
	}

	@Override
	public PrimitiveValue<?> eval() {
		return getSpreadsheet().getValue(address);
	}

	@Override
	public String toString() {
		return address.toString();
	}

	public CellAddress getCellAddress() {
		return address;
	}
	
	public VarExpression clone(){
		try {
			return (VarExpression) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
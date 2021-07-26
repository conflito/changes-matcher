package pt.ul.fc.di.dco000.domain.expressions;

import pt.ul.fc.di.dco000.domain.PrimitiveValue;
import pt.ul.fc.di.dco000.domain.Spreadsheet;

public class ValueExpression extends Expression{

	private PrimitiveValue<?> value;

	public ValueExpression(Spreadsheet sp, PrimitiveValue<?> value) {
		super(sp);
		this.value = value;
	}

	@Override
	public <T> T accept(IExpressionVisitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public PrimitiveValue<?> eval() {
		return value;
	}
	
	@Override
	public String toString() {
		return value.toString();
	}
	
	public ValueExpression clone(){
		try {
			return (ValueExpression) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}

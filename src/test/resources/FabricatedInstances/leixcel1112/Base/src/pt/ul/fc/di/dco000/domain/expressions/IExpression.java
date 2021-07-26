package pt.ul.fc.di.dco000.domain.expressions;

import pt.ul.fc.di.dco000.domain.PrimitiveValue;

public interface IExpression extends Cloneable{

	PrimitiveValue<?> eval();
	
	<T> T accept(IExpressionVisitor<T> visitor);

	Object clone() throws CloneNotSupportedException; 	

}

package pt.ul.fc.di.dco000.domain.expressions;

import java.util.ArrayList;
import java.util.List;

import pt.ul.fc.di.dco000.domain.PrimitiveValue;
import pt.ul.fc.di.dco000.domain.SpecialValue;
import pt.ul.fc.di.dco000.domain.Spreadsheet;
import pt.ul.fc.di.dco000.domain.XNumber;


public class Addition extends FunctionApplication{

	public Addition(Spreadsheet sp, List<IExpression> args){
		super(sp, args);
	}

	@Override
	public PrimitiveValue<?> eval() {

		List<XNumber> res = new ArrayList<XNumber>();
		for (IExpression e: getArgs()){
			PrimitiveValue<?> v = e.eval();
			if (v.equals(SpecialValue.DEFAULT))
				v = new XNumber(0);
			if (!(v instanceof XNumber))
				return SpecialValue.ERROR;
			else
				res.add((XNumber)v);
		}

		XNumber result = new XNumber(0.0);
		for (XNumber n: res)
			result = result.add(n);

		return result;
	}

	@Override
	public <T> T accept(IExpressionVisitor<T> visitor) {
		return visitor.visit(this);
	}
	@Override
	public String toString() {
		return "SUM"+ getArgs();
	}
}


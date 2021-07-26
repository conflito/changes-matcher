

import java.util.ArrayList;
import java.util.List;

import pt.ul.fc.di.dco000.domain.*;
import pt.ul.fc.di.dco000.domain.expressions.FunctionApplication;
import pt.ul.fc.di.dco000.domain.expressions.IExpression;
import pt.ul.fc.di.dco000.domain.expressions.IExpressionVisitor;


public class Average extends FunctionApplication{

	public Average(Spreadsheet sp, List<IExpression> args){
		super(sp, args);
	}

	@Override
	public PrimitiveValue<?> eval() {

		List<XNumber> res = new ArrayList<XNumber>();
		for (IExpression e: getArgs()){
			PrimitiveValue<?> v = e.eval();
			if (v.equals(SpecialValue.DEFAULT))
				v = new XNumber(0.0);
			if (!(v instanceof XNumber))
				return SpecialValue.ERROR;
			else
				res.add((XNumber)v);
		}

		XNumber result = new XNumber(0.0);
		for (XNumber n: res)
			result = result.add(n);

		return result.div(new XNumber((double)getArgs().size()));
	}

	@Override
	public <T> T accept(IExpressionVisitor<T> visitor) {
		return visitor.visit(this);
	}
	@Override
	public String toString() {
		return "AVERAGE"+ getArgs();
	}
}


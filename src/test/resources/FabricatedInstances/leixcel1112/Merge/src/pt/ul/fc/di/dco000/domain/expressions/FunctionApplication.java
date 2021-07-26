package pt.ul.fc.di.dco000.domain.expressions;

import java.util.ArrayList;
import java.util.List;

import pt.ul.fc.di.dco000.domain.Spreadsheet;


public abstract class FunctionApplication extends Expression{

	private List<IExpression> args;
	
	protected FunctionApplication(Spreadsheet sp, List<IExpression> args){
		super(sp);
		this.args = new ArrayList<IExpression>(args);
	}
	
	public List<IExpression> getArgs(){
		return args;
	}

	public void setArgs(List<IExpression> args2) {
		args = args2;
	}
	
	public FunctionApplication clone(){
		try {
			FunctionApplication cl = (FunctionApplication) super.clone();
			cl.args = new ArrayList<IExpression>();
			for (IExpression e: args)
				cl.args.add((IExpression) e.clone());
			return cl;
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}

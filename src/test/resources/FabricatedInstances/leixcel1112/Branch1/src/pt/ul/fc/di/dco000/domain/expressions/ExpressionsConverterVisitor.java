package pt.ul.fc.di.dco000.domain.expressions;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import pt.ul.fc.di.dco000.domain.CellAddress;
import pt.ul.fc.di.dco000.domain.Column;
import pt.ul.fc.di.dco000.domain.FunctionCatalog;
import pt.ul.fc.di.dco000.domain.Line;
import pt.ul.fc.di.dco000.domain.SpecialValue;
import pt.ul.fc.di.dco000.domain.Spreadsheet;
import pt.ul.fc.di.dco000.domain.Text;
import pt.ul.fc.di.dco000.domain.XNumber;
import pt.ul.fc.di.dco000.services.parser.ANumber;
import pt.ul.fc.di.dco000.services.parser.AString;
import pt.ul.fc.di.dco000.services.parser.CellReference;
import pt.ul.fc.di.dco000.services.parser.Formula;
import pt.ul.fc.di.dco000.services.parser.IParserVisitor;
import pt.ul.fc.di.dco000.services.parser.Literal;
import pt.ul.fc.di.dco000.services.parser.Value;



/**
 * @author jcraveiro
 *
 * The objects of this class are visitors that are able to
 * convert the abstract syntax tree used by the parser
 * provided by services (not domain specific)  into an 
 * IExpression (domain specific)
 * 
 * @version $Revision: 1.0 $
 */
public class ExpressionsConverterVisitor implements IParserVisitor<IExpression>{

	/**
	 * Field sp.
	 */
	private Spreadsheet sp;

	/**
	 * Constructs a converter for a given spreadsheet
	 * @param spreadsheet Spreadsheet
	 */
	public ExpressionsConverterVisitor(Spreadsheet spreadsheet) {
		this.sp = spreadsheet;
	}

	/**
	 * Method visit.
	 * @param x ANumber
	 * @return IExpression
	 * @see services.parser.IParserVisitor#visit(ANumber)
	 */
	public IExpression visit(ANumber x) {
		return new ValueExpression(this.sp,new XNumber(x.getValue().doubleValue()));
	}

	/**
	 * Method visit.
	 * @param x AString
	 * @return IExpression
	 * @see services.parser.IParserVisitor#visit(AString)
	 */
	public IExpression visit(AString x) {
		return new ValueExpression(this.sp, new Text(x.getString()));
	}

	/**
	 * Method visit.
	 * @param x CellReference
	 * @return IExpression
	 * @see services.parser.IParserVisitor#visit(CellReference)
	 */
	public IExpression visit(CellReference x) {
		if (Column.validName(x.getCol()) && Line.validNumber(x.getLine())){
			Line line =  new Line(x.getLine());
			Column column = new Column(x.getCol());
			CellAddress address = this.sp.getCellAddress(column,line);
			return new VarExpression(this.sp, address);
		}
		else
			return new ValueExpression(this.sp, SpecialValue.ERROR);
	}

	/**
	 * Method visit.
	 * @param x a services.parser.Formula
	 * @return IExpression
	 * @see services.parser.IParserVisitor#visit(services.parser.Formula)
	 */
	public IExpression visit(Formula x) {
		if (FunctionCatalog.INSTANCE.contains(x.getFunctionName())){
			Class<? extends FunctionApplication> function = 
				FunctionCatalog.INSTANCE.getFunctionApplication(x.getFunctionName());

			Constructor<?> constructor = null;
			try {
				//get the constructor with two arguments of the given types
				constructor = function.getConstructor(Spreadsheet.class,List.class);
			
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}

			Collection<Value> argus = x.getArgs();	
			List<IExpression> args = new ArrayList<IExpression>();
			for (Value arg : argus) {
				args.add(arg.accept(new ExpressionsConverterVisitor(this.sp)));
			}

			if (constructor != null){
				try {
					
					return (IExpression) constructor.newInstance(this.sp,args);
				
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		return new ErrorExpression(sp);
	}

	/**
	 * Method visit.
	 * @param x Literal
	 * @return IExpression
	 * @see services.parser.IParserVisitor#visit(Literal)
	 */
	public IExpression visit(Literal x) {
		return new ValueExpression(this.sp, new Text(x.getString()));
	}

}

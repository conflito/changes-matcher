package pt.ul.fc.di.dco000.domain.expressions;

import java.util.ArrayList;
import java.util.List;

import pt.ul.fc.di.dco000.domain.CellAddress;
import pt.ul.fc.di.dco000.domain.CellContent;
import pt.ul.fc.di.dco000.domain.Formula;
import pt.ul.fc.di.dco000.domain.PrimitiveValue;
import pt.ul.fc.di.dco000.domain.Spreadsheet;


/**
 * @author antonialopes
 * 
 * The objects of this class are visitors that visit IExpressions 
 * in order to calculate the IExpression defined only in terms of 
 * function applications and primitive values (i.e. without cell 
 * addresses). Cell addresses are represented by the corresponding
 * expansion of their formulas.
 * 
 * @version $Revision: 1.0 $
 */
public class ExpandedExpConverterVisitor implements
		IExpressionVisitor<IExpression> {

    /**
     * Field sp Ñ the spreadsheet wrt which 
     * the visit will take place
     */
    private Spreadsheet sp;

	/**
	 * Constructs a visitor for a given spreadsheet
	 * @param sp
	 */
	public ExpandedExpConverterVisitor(Spreadsheet sp){
		this.sp = sp;
	}
	

	/* (non-Javadoc)
	 * @see IExpressionVisitor#visit(ValueExpression)
	 */
	@Override
	public IExpression visit(ValueExpression v) {
		return v;
	}

	/* (non-Javadoc)
	 * @see pIExpressionVisitor#visit(VarExpression)
	 */
	@Override
	public IExpression visit(VarExpression v) {
		CellAddress addressAux = v.getCellAddress();
		CellContent cc = this.sp.getContent(addressAux);
			if (cc instanceof Formula){
				IExpression e = ((Formula)cc).getExpression();
				return e.accept(new ExpandedExpConverterVisitor(this.sp));
			}
			else{
				return new ValueExpression(this.sp,(PrimitiveValue<?>)cc);
			}
	}

	/* (non-Javadoc)
	 * @see IExpressionVisitor#visit(FunctionApplication)
	 */
	@Override
	public IExpression visit(FunctionApplication f) {
		List<IExpression> args = new ArrayList<IExpression>();
		for (IExpression e: f.getArgs()){
			args.add(e.accept(new ExpandedExpConverterVisitor(this.sp)));
		}
		FunctionApplication result = f.clone();
		result.setArgs(args);
		return result;
	}


    @Override
    public IExpression visit(ErrorExpression errorExpression) {
        return errorExpression;
    }

}

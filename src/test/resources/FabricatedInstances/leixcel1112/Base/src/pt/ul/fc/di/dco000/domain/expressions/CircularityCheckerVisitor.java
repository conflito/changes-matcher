package pt.ul.fc.di.dco000.domain.expressions;

import pt.ul.fc.di.dco000.domain.CellAddress;
import pt.ul.fc.di.dco000.domain.CellContent;
import pt.ul.fc.di.dco000.domain.Formula;
import pt.ul.fc.di.dco000.domain.Spreadsheet;

/**
 * @author antonialopes
 * 
 * The objects of this class are visitors that visit IExpressions 
 * in order to check if that expression can be used as the formula
 * of a given cell without introducing circular references.
 * 
 * Eg. If the content of A2 is =A1*2, then we can not use the expression
 * A2+A3 has the formula that defines the value of A1
 * 
 * @version $Revision: 1.0 $
 */

public class CircularityCheckerVisitor implements
		IExpressionVisitor<Boolean> {

	private CellAddress address;

	public CircularityCheckerVisitor(CellAddress a){
		this.address = a;
	}
	
	@Override
	public Boolean visit(ValueExpression v) {
		return false;
	}

	@Override
	public Boolean visit(VarExpression v) {
		Spreadsheet sp = v.getSpreadsheet();
		boolean result = false;
		CellAddress addressAux = v.getCellAddress();
		if (address.equals(addressAux)){
			result = true;
		}
		else{
			CellContent cc = sp.getContent(addressAux);
			if (cc instanceof Formula){
				IExpression e = ((Formula)cc).getExpression();
				result =  e.accept(new CircularityCheckerVisitor(address));
			}
			else{
				result = false;
			}	
		}
		return result;
	}

	@Override
	public Boolean visit(FunctionApplication f) {
		boolean result = false;
		for (IExpression e: f.getArgs()){
			result = result || e.accept(new CircularityCheckerVisitor(address));
		}
		return result;
	}

    @Override
    public Boolean visit(ErrorExpression errorExpression) {
         return false;
    }

}

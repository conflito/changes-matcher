package pt.ul.fc.di.dco000.domain.expressions;

import java.util.ArrayList;
import java.util.List;

import pt.ul.fc.di.dco000.domain.Cell;
import pt.ul.fc.di.dco000.domain.CellAddress;
import pt.ul.fc.di.dco000.domain.CellContent;
import pt.ul.fc.di.dco000.domain.Formula;
import pt.ul.fc.di.dco000.domain.Spreadsheet;


public class CollectorRefsVisitor implements IExpressionVisitor<List<Cell>> {

	@Override
	public List<Cell> visit(ValueExpression v) {
		return new ArrayList<Cell>();
	}

	@Override
	public List<Cell> visit(VarExpression v) {
		Spreadsheet sp = v.getSpreadsheet();
		ArrayList<Cell> l = new ArrayList<Cell>();
		CellAddress address = v.getCellAddress();
		Cell cell = sp.getCell(address);
		l.add(cell);
		CellContent cc = sp.getContent(address);
		if (cc instanceof Formula){
			IExpression e = ((Formula)cc).getExpression();
			List<Cell> laux =  e.accept(new CollectorRefsVisitor());
			for(Cell c: laux){
				if (!l.contains(c))
					l.add(c);
			}
		}
		return l;
	}

	@Override
	public List<Cell> visit(FunctionApplication f) {
		ArrayList<Cell> l = new ArrayList<Cell>();
		for (IExpression e: f.getArgs()){
			List<Cell> laux = e.accept(new CollectorRefsVisitor());
			for(Cell c: laux){
				if (!l.contains(c))
					l.add(c);
			}
		}
		return l;
	}

    @Override
    public List<Cell> visit(ErrorExpression errorExpression) {
        return new ArrayList<Cell>();
    }

}

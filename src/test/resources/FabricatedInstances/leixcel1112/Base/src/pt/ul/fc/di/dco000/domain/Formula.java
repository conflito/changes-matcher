package pt.ul.fc.di.dco000.domain;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import pt.ul.fc.di.dco000.domain.expressions.CollectorRefsVisitor;
import pt.ul.fc.di.dco000.domain.expressions.ErrorExpression;
import pt.ul.fc.di.dco000.domain.expressions.ExpandedExpConverterVisitor;
import pt.ul.fc.di.dco000.domain.expressions.IExpression;


public class Formula implements CellContent, Observer{

    private IExpression definitionExp;

    private PrimitiveValue<?> value;

    private List<Cell> refs;

    private Spreadsheet sp;

    public Formula(IExpression exp, Spreadsheet sp){
        this.definitionExp = exp;
        this.sp = sp;
        this.refs = definitionExp.accept(new CollectorRefsVisitor());
        for(Cell c: refs)
            c.addObserver(this);
        value = definitionExp.accept(new ExpandedExpConverterVisitor(sp)).eval(); 
    }

    public PrimitiveValue<?> getValue(){
        return value;
    }

    @Override
    public String toString() {
        return  "="+ definitionExp;
    }

    @Override
    public void update(Observable obs, Object event) {
        if(event instanceof CellContentChangedEvent){
            value = definitionExp.accept(new ExpandedExpConverterVisitor(sp)).eval();
            List<Cell> newRefs = definitionExp.accept(new CollectorRefsVisitor());
            for(Cell c: refs){
                if (!newRefs.contains(c))
                    c.deleteObserver(this);
            }
            for(Cell c: newRefs){
                if (!refs.contains(c))
                    c.addObserver(this);
            }
        }
        if(event instanceof CellRemovedEvent){
            this.definitionExp = new ErrorExpression(sp);
            for(Cell c: refs)
                c.deleteObserver(this);
            this.refs.clear();
        }

    }

    public IExpression getExpression() {
        return definitionExp;
    }

}

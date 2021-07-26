package pt.ul.fc.di.dco000.domain.expressions;

import pt.ul.fc.di.dco000.domain.PrimitiveValue;
import pt.ul.fc.di.dco000.domain.Spreadsheet;
import pt.ul.fc.di.dco000.domain.Text;

public class ErrorExpression extends Expression implements IExpression {

    public ErrorExpression(Spreadsheet sp) {
        super(sp);
    }

    @Override
    public <T> T accept(IExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public PrimitiveValue<?> eval() {
        return new Text("ERROR");
    }
    
    @Override
    public String toString() {
        return "ERROR";
    }
    
    public ErrorExpression clone(){
        try {
            return (ErrorExpression) super.clone();
        } catch (CloneNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}

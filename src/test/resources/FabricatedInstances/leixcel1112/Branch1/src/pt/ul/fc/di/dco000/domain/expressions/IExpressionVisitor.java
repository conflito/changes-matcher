package pt.ul.fc.di.dco000.domain.expressions;

/**
 * @author antonialopes
 *
 * This interface is a generic visitor of IExpressions
 * 
 * @param <E> The type of the value calculated by the visitor
 * 
 * @version $Revision: 1.0 $
 */
public interface IExpressionVisitor<E> {
    
    /**
     * Method visit a value expression.
     * @param v ValueExpression
     * @return E
     */
	E visit(ValueExpression v);
	
	/**
     * Method visit a var expression.
     * @param v VarExpression
     * @return E
     */
	E visit(VarExpression v);
	
	/**
     * Method visit a function application.
     * @param f FunctionApplication
     * @return E
     */
    E visit(ErrorExpression errorExpression);
	
	/**
     * Method visit a function application.
     * @param f FunctionApplication
     * @return E
     */
	E visit(FunctionApplication f);
	
	
}

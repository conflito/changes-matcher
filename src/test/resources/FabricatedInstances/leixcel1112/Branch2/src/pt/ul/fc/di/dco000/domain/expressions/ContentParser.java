package pt.ul.fc.di.dco000.domain.expressions;

import pt.ul.fc.di.dco000.domain.CellAddress;
import pt.ul.fc.di.dco000.domain.CellContent;
import pt.ul.fc.di.dco000.domain.Formula;
import pt.ul.fc.di.dco000.domain.SpecialValue;
import pt.ul.fc.di.dco000.domain.Spreadsheet;
import pt.ul.fc.di.dco000.services.parser.CellParseException;
import pt.ul.fc.di.dco000.services.parser.CellParser;
import pt.ul.fc.di.dco000.services.parser.Value;

/**
 * @author antonialopes
 * 
 * The objects of this class are parsers that able to parse 
 * a string provided by the user as the new content of a cell. 
 * 
 * @version $Revision: 1.0 $
 */
public class ContentParser {

    /**
     * Field sp Ñ the spreadsheet wrt which 
     * the parsing will take place
     */
    private Spreadsheet sp;

    /**
     * Constructs a new parser for the given spreadsheet
     * @param spreadsheet
     */
    public ContentParser(Spreadsheet spreadsheet) {
        this.sp = spreadsheet;
    }

    /**
     * Returns a CellContent that results from the parse of the
     * given string as the new content for the cell with a given address.
     * @param content - the string to be parsed
     * @param a - the cell address that will receive the parsed content
     * @return CellContent resulting from the parse
     *     More concretely:
     *     - null if there is a syntax error
     *     - SpecialValue.ERROR if there is a circularity problem
     *     - A ValueExpression if the string corresponds to a primitive value
     *     - A Formula otherwise
     */
    public CellContent parse(String content, CellAddress a) {
        ExpressionsConverterVisitor visitor = 
            new ExpressionsConverterVisitor(this.sp);
        Value result;
        try {
            result = CellParser.parseContent(content);
        } catch (CellParseException e) {
            e.printStackTrace();
            return null;
        }

        IExpression exp = result.accept(visitor);
        if (exp == null)
            return null;
        
        if (exp instanceof ErrorExpression){
            return new Formula(exp,this.sp);
        }
        
        if (exp instanceof ValueExpression){
            return ((ValueExpression)exp).eval();
        }
        
        CircularityCheckerVisitor cVisitor = 
            new CircularityCheckerVisitor(a);
        if (exp.accept(cVisitor)){
            return SpecialValue.ERROR;
        }
        else{
            return new Formula(exp,this.sp);
        }
    }
}

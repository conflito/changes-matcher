package pt.ul.fc.di.dco000.services.parser;

/**
 * A visitor.
 * @author mal
 * @author jcraveiro
 * @param <E>
 * @version $Revision: 1.0 $
 */
public interface IParserVisitor<E>{

		/**
		 * Method visit.
		 * @param x pt.ul.fc.di.dco000.services.parser.ANumber
		 * @return E
		 */
		public E visit(pt.ul.fc.di.dco000.services.parser.ANumber x) ;
		
		/**
		 * Method visit.
		 * @param x pt.ul.fc.di.dco000.services.parser.AString
		 * @return E
		 */
		public E visit(pt.ul.fc.di.dco000.services.parser.AString x) ;
		
		/**
		 * Method visit.
		 * @param x pt.ul.fc.di.dco000.services.parser.CellReference
		 * @return E
		 */
		public E visit(pt.ul.fc.di.dco000.services.parser.CellReference x) ;
		
		/**
		 * Method visit.
		 * @param x pt.ul.fc.di.dco000.services.parser.Formula
		 * @return E
		 */
		public E visit(pt.ul.fc.di.dco000.services.parser.Formula x) ;
		
		/**
		 * Method visit.
		 * @param x pt.ul.fc.di.dco000.services.parser.Literal
		 * @return E
		 */
		public E visit(pt.ul.fc.di.dco000.services.parser.Literal x) ;

}

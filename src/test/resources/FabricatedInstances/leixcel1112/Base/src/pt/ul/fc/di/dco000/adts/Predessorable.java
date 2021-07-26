package pt.ul.fc.di.dco000.adts;

public interface Predessorable<T> extends Comparable<T> {
		   		
		/** O elemento anterior na relacao de ordem. */
		//@requires !isFirst()
		//@ensures compareTo(\result)>0; 
		T pred();
		
		boolean isFirst();
}

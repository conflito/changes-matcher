package pt.ul.fc.di.dco000.adts;

public interface Successorable<T> extends Comparable<T> {
	   
	/** O elemento seguinte na relacao de ordem. */
	// @ensures compareTo(\result)<0; 
	T suc();
	
}	

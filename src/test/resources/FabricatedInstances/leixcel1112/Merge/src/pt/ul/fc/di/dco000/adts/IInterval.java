package pt.ul.fc.di.dco000.adts;

import java.util.Iterator;

public interface IInterval<E extends Successorable<E>> extends Iterable<E>{

	/** O inicio do intervalo.*/
	E min();

	/** O fim do intervalo.*/
	E max();
	
	/** Os elementos pertencentes a este intervalo na ordem crescente.*/
	Iterator<E> iterator();
}

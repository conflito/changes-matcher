package pt.ul.fc.di.dco000.domain;

public interface PrimitiveValue<E> extends CellContent, Cloneable{
	E nativeValue();
}

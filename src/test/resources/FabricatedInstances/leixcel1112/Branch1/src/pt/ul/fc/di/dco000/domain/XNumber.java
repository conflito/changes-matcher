package pt.ul.fc.di.dco000.domain;

public class XNumber implements PrimitiveValue<java.lang.Number>,Comparable<XNumber>{
	
	private java.lang.Number value;
	
	public XNumber(Double valor) {
		this.value = valor;
	}
	
	public XNumber(int valor) {
		this.value = valor;
	}


	public XNumber add(XNumber n) {
		return new XNumber(n.value.doubleValue()+this.value.doubleValue());
	}

	@Override
	public PrimitiveValue<java.lang.Number> getValue() {
		if (Math.rint(value.doubleValue()) == value.doubleValue())
			return new XNumber(value.intValue());
		else
			return this;
	}

	public XNumber clone(){
		try {
			return (XNumber) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public PrimitiveValue<?> div(XNumber n) {
		if (n.value.doubleValue() != 0  )
			return new XNumber(this.value.doubleValue()/n.value.doubleValue());
		return SpecialValue.ERROR;
	}
	

	public String toString(){
		return value.toString();
	}

	@Override
	public java.lang.Number nativeValue() {
		return value;
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}
	@Override
	public int compareTo(XNumber arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
}

package pt.ul.fc.di.dco000.domain;

public enum SpecialValue implements PrimitiveValue<String> {
	ERROR,
	DEFAULT;

	@Override
	public String nativeValue() {
		return "Error";  
	}
	
	public String toString(){
		if (this.equals(ERROR))
			return "Error";
		//if (this.equals(DEFAULT))
			return "";
	}

	@Override
	public PrimitiveValue<String> getValue() {
		return this;
	}
	
}

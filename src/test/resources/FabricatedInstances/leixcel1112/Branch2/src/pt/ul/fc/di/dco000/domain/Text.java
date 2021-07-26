/**
 * 
 */
package pt.ul.fc.di.dco000.domain;

/**
 * @author jcraveiro
 *
 */
public class Text implements PrimitiveValue<String>{
	
	private String value;
	
	/**
	 * @param string
	 */
	public Text(String valor) {
		this.value = valor;
	}

	@Override
	public PrimitiveValue<String> getValue() {
		return this;
	}

	
	public Text clone(){
		try {
			return (Text) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public String toString(){
		return value;
	}

	@Override
	public String nativeValue() {
		return value;
	}

}

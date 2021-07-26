/**
 * 
 */
package pt.ul.fc.di.dco000.domain;

import pt.ul.fc.di.dco000.domain.expressions.FunctionApplication;

/**
 * @author jcraveiro
 *
 */
public class Function {
	
	private String name;
	private Class<? extends FunctionApplication> c;
	
	public Function(String name, Class<? extends FunctionApplication> c){
		this.name = name;
		this.c = c;
	}

	public Class<? extends FunctionApplication> getFunctionApplication(){
		return c;
	}

	public String getName() {
		return name;
	}
}

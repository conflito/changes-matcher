package matcher.entities.deltas;

import java.util.List;

import matcher.entities.ClassInstance;

public class InsertClassAction extends InsertAction {

	private ClassInstance insertedClass;

	public InsertClassAction(ClassInstance insertedClass) {
		super();
		this.insertedClass = insertedClass;
	}

	public ClassInstance getInsertedClass() {
		return insertedClass;
	}
	
	public String getInsertedClassQualifiedName() {
		return insertedClass.getQualifiedName();
	}
	
	public List<String> getInsertedMethodsQualifiedName(){
		return insertedClass.getMethodsQualifiedNames();
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("insert new class ");
		result.append(insertedClass.getQualifiedName());
		result.append("\n" + insertedClass.toStringDebug());
		return result.toString();
	}

}

package matcher.entities.deltas;

import matcher.entities.ConstructorInstance;
import matcher.entities.FieldAccessInstance;
import matcher.entities.FieldInstance;
import matcher.entities.MethodInstance;
import matcher.entities.MethodInvocationInstance;

public class DeleteAction extends ActionInstance {

	private Deletable deletedEntity;
	private Holder holderEntity;
	
	public DeleteAction(Deletable deletedEntity, Holder holderEntity) {
		super(Action.DELETE);
		this.deletedEntity = deletedEntity;
		this.holderEntity = holderEntity;
	}

	public Deletable getDeletedEntity() {
		return deletedEntity;
	}

	public Holder getHolderEntity() {
		return holderEntity;
	}
	
	public boolean isMethodDelete() {
		return deletedEntity instanceof MethodInstance;
	}
	
	public boolean isFieldDelete() {
		return deletedEntity instanceof FieldInstance;
	}
	
	public boolean isConstructorDelete() {
		return deletedEntity instanceof ConstructorInstance;
	}
	
	public boolean isInvocationDelete() {
		return deletedEntity instanceof MethodInvocationInstance;
	}
	
	public boolean isFieldReadDelete() {
		return deletedEntity instanceof FieldAccessInstance 
				&& ((FieldAccessInstance)deletedEntity).isFieldRead();
	}
	
	public boolean isFieldWriteDelete() {
		return deletedEntity instanceof FieldAccessInstance 
				&& ((FieldAccessInstance)deletedEntity).isFieldWrite();
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder("remove ");
		if(isMethodDelete())
			result.append("method ");
		if(isFieldDelete())
			result.append("field ");
		if(isConstructorDelete())
			result.append("constructor ");
		if(isInvocationDelete())
			result.append("method invocation of ");
		if(isFieldReadDelete())
			result.append("field read of ");
		if(isFieldWriteDelete())
			result.append("field write of ");
		result.append(deletedEntity.getQualifiedName() + " from ");
		result.append(holderEntity.getQualifiedName());
		return result.toString();
	}
}

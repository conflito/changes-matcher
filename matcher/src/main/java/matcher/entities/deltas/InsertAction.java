package matcher.entities.deltas;

import matcher.entities.ConstructorInstance;
import matcher.entities.FieldAccessInstance;
import matcher.entities.FieldInstance;
import matcher.entities.MethodInstance;
import matcher.entities.MethodInvocationInstance;

public class InsertAction extends ActionInstance {

	private Insertable insertedEntity;
	private Holder holderEntity;
	
	
	public InsertAction(Insertable insertedEntity, Holder holderEntity) {
		super(Action.INSERT);
		this.insertedEntity = insertedEntity;
		this.holderEntity = holderEntity;
	}

	public Insertable getInsertedEntity() {
		return insertedEntity;
	}

	public Holder getHolderEntity() {
		return holderEntity;
	}
	
	public boolean isMethodInsert() {
		return insertedEntity instanceof MethodInstance;
	}
	
	public boolean isFieldInsert() {
		return insertedEntity instanceof FieldInstance;
	}
	
	public boolean isConstructorInsert() {
		return insertedEntity instanceof ConstructorInstance;
	}
	
	public boolean isInvocationInsert() {
		return insertedEntity instanceof MethodInvocationInstance;
	}
	
	public boolean isFieldReadInsert() {
		return insertedEntity instanceof FieldAccessInstance 
				&& ((FieldAccessInstance)insertedEntity).isFieldRead();
	}
	
	public boolean isFieldWriteInsert() {
		return insertedEntity instanceof FieldAccessInstance 
				&& ((FieldAccessInstance)insertedEntity).isFieldWrite();
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder("insert ");
		if(isMethodInsert())
			result.append("method ");
		if(isFieldInsert())
			result.append("field ");
		if(isConstructorInsert())
			result.append("constructor ");
		if(isInvocationInsert())
			result.append("method invocation of ");
		if(isFieldReadInsert())
			result.append("field read of ");
		if(isFieldWriteInsert())
			result.append("field write of ");
		result.append(insertedEntity.getQualifiedName() + " in ");
		result.append(holderEntity.getQualifiedName());
		return result.toString();
	}
}

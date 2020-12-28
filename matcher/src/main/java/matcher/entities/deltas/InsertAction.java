package matcher.entities.deltas;

import matcher.entities.FieldAccessInstance;
import matcher.entities.MethodInvocationInstance;

public class InsertAction extends ActionInstance {

	private Insertable insertedEntity;
	private Holder holderEntity;
	
	
	public InsertAction(Insertable insertedEntity, Holder holderEntity) {
		super(Action.INSERT);
		this.insertedEntity = insertedEntity;
		this.holderEntity = holderEntity;
	}
	
	public String getInsertedEntityQualifiedName() {
		return insertedEntity.getQualifiedName();
	}
	
	public String getHolderEntityQualifiedName() {
		return holderEntity.getQualifiedName();
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

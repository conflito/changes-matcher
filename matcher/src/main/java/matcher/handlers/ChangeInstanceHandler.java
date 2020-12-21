package matcher.handlers;

import java.io.File;

import matcher.entities.BaseInstance;
import matcher.entities.ChangeInstance;
import matcher.entities.deltas.DeltaInstance;
import matcher.exceptions.ApplicationException;
import matcher.patterns.ConflictPattern;

public class ChangeInstanceHandler {

	private BaseInstanceHandler bih;
	private DeltaInstanceHandler dih;

	public ChangeInstanceHandler(){
		this.bih = new BaseInstanceHandler();
		this.dih = new DeltaInstanceHandler();
	}

	public ChangeInstance getChangeInstance(File base, File firstVariant, File secondVariant, ConflictPattern cp) 
			throws ApplicationException {
		BaseInstance baseInstance = bih.getBaseInstance(base, cp);
		DeltaInstance firstDelta = dih.getDeltaInstance(base, firstVariant, cp);
		DeltaInstance secondDelta = dih.getDeltaInstance(base, secondVariant, cp);
		return new ChangeInstance(baseInstance, firstDelta, secondDelta);
	}
	
//	public static void main(String[] args) throws ApplicationException {
//		File base = new File("src/main/java/base/Square.java");
//		File firstVar = new File("src/main/java/branch01/Square.java");
//		File secondVar = new File("src/main/java/branch02/Square.java");
//		ChangeInstanceHandler cih = new ChangeInstanceHandler();
//		ChangeInstance ci = cih.getChangeInstance(base, firstVar, secondVar, new ConflictPattern());
//		System.out.println(ci);
//	}
}

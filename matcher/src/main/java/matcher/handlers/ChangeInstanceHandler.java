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

	public ChangeInstance getChangeInstance(File base, File variant, File variant2, 
			ConflictPattern cp) throws ApplicationException {
		BaseInstance baseInstance = bih.getBaseInstance(base, cp);
		if(baseInstance == null)
			return null;
		DeltaInstance firstDelta = dih.getDeltaInstance(base, variant, cp);
		DeltaInstance secondDelta = dih.getDeltaInstance(base, variant2, cp);
		return new ChangeInstance(baseInstance, firstDelta, secondDelta);
	}
	
	public ChangeInstance getChangeInstance(File base1, File base2, File variant1, File variant2,
			ConflictPattern cp) throws ApplicationException {
		BaseInstance baseInstance = bih.getBaseInstance(base1, cp);
		if(baseInstance == null)
			return null;
		BaseInstance baseInstance2 = bih.getBaseInstance(base2, cp);
		if(baseInstance2 == null)
			return null;
		baseInstance.merge(baseInstance2);
		DeltaInstance firstDelta = dih.getDeltaInstance(base1, variant1, cp);
		DeltaInstance secondDelta = dih.getDeltaInstance(base2, variant2, cp);
		return new ChangeInstance(baseInstance, firstDelta, secondDelta);
	}
}

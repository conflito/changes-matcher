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

	public ChangeInstanceHandler() {
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
}

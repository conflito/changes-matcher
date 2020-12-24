package matcher.handlers;

import java.io.File;

import gumtree.spoon.AstComparator;
import gumtree.spoon.diff.Diff;
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
		Diff firstDiff = diff(base, firstVariant);
		Diff secondDiff = diff(base, secondVariant);
		BaseInstance baseInstance = bih.getBaseInstance(base, cp);
		DeltaInstance firstDelta = dih.getDeltaInstance(firstDiff, cp);
		DeltaInstance secondDelta = dih.getDeltaInstance(secondDiff, cp);
		return new ChangeInstance(baseInstance, firstDelta, secondDelta);
	}
	
	private Diff diff(File first, File second) throws ApplicationException {
		Diff diff = null;
		try {
			diff = new AstComparator().compare(first, second);
		} catch (Exception e) {
			throw new ApplicationException("Error calculating the delta", e);
		}
		return diff;
	}
}

package matcher.handlers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import matcher.entities.BaseInstance;
import matcher.entities.ChangeInstance;
import matcher.entities.deltas.DeltaInstance;
import matcher.exceptions.ApplicationException;
import matcher.patterns.ConflictPattern;

public class ChangeInstanceHandler {

	private BaseInstanceHandler bih;
	private DeltaInstanceHandler dih;

	private SpoonHandler spoonHandler;
	
	public ChangeInstanceHandler(int trackLimit){
		this.spoonHandler = new SpoonHandler(trackLimit);
		this.bih = new BaseInstanceHandler(spoonHandler);
		this.dih = new DeltaInstanceHandler(spoonHandler);
	}
	
	public ChangeInstance getChangeInstance(File[] bases, File[] variants1, File[] variants2, 
			ConflictPattern  cp) throws ApplicationException {
		if(bases == null || variants1 == null || variants2 == null)
			return null;
		if(!sameLenght(bases, variants1, variants2))
			return null;
		BaseInstance baseInstance = processBases(bases, cp);
		List<DeltaInstance> deltas = processDeltas(bases, variants1, variants2, cp);
		ChangeInstance result = new ChangeInstance();
		result.setBaseInstance(baseInstance);
		result.addDeltaInstances(deltas);
		return result;
	}
	
	private boolean sameLenght(File[] bases, File[] variants1, File[] variants2) {
		return bases.length == variants1.length && bases.length == variants2.length;
	}

	private BaseInstance processBases(File[] bases, ConflictPattern  cp) 
			throws ApplicationException {
		BaseInstance result = new BaseInstance();
		for(File f: bases) {
			if(f != null) {
				BaseInstance b = bih.getBaseInstance(f, cp);
				if(b != null) {
					result.merge(b);
				}
			}
		}
		return result;
	}
	
	private List<DeltaInstance> processDeltas(File[] bases, File[] variants1, File[] variants2, 
			ConflictPattern  cp) throws ApplicationException {
		List<DeltaInstance> result = new ArrayList<>();
		for(int i = 0; i < bases.length; i++) {
			File base = bases[i];
			File variant1 = variants1[i];
			File variant2 = variants2[i];
			DeltaInstance di;
			if(variant1 != null) {
				di = dih.getDeltaInstance(base, variant1, cp);
				result.add(di);
			}
			if(variant2 != null) {
				di = dih.getDeltaInstance(base, variant2, cp);
				result.add(di);
			}
		}
		return result;
	}
}

package matcher.handlers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import gumtree.spoon.diff.Diff;
import matcher.entities.BaseInstance;
import matcher.entities.ChangeInstance;
import matcher.entities.deltas.DeltaInstance;
import matcher.exceptions.ApplicationException;
import matcher.patterns.ConflictPattern;
import spoon.reflect.declaration.CtType;

public class ChangeInstanceHandler {

	private final static Logger logger = Logger.getLogger(ChangeInstanceHandler.class);
	
	private BaseInstanceHandler bih;
	private DeltaInstanceHandler dih;
	
	public ChangeInstanceHandler(){
		this.bih = new BaseInstanceHandler();
		this.dih = new DeltaInstanceHandler();
	}
	
	public ChangeInstance getChangeInstance(File[] bases, File[] variants1, File[] variants2, 
			ConflictPattern  cp) throws ApplicationException {
		if(bases == null || variants1 == null || variants2 == null)
			return null;
		if(!sameLength(bases, variants1, variants2))
			return null;
		
		ChangeInstance result = new ChangeInstance();
		
		logger.info("Building system objects for " + cp.getConflictName() + "...");

		BaseInstance baseInstance = processBases(cp);
		List<DeltaInstance> deltas = processDeltas(bases, variants1, variants2, cp);

		result.setBaseInstance(baseInstance);
		result.addDeltaInstances(deltas);

		return result;
	}
	
	private CtType<?> getFullCtType(File file, Iterable<CtType<?>> elements) 
			throws ApplicationException{
		
		CtType<?> basicType = SpoonHandler.getInstance().getCtType(
				SpoonHandler.getInstance().getSpoonResource(file));
		return SpoonHandler.getInstance()
					.getFullCtType(elements, basicType.getQualifiedName());
	}
	
	private boolean sameLength(File[] bases, File[] variants1, File[] variants2) {
		return bases.length == variants1.length && bases.length == variants2.length;
	}
	
	private BaseInstance processBases(ConflictPattern cp) {
		return bih.getBaseInstance(SpoonHandler.getInstance().baseTypes(), cp);
	}
	
	private List<DeltaInstance> processDeltas(File[] bases, File[] variants1, File[] variants2, 
			ConflictPattern  cp) throws ApplicationException {
		List<DeltaInstance> result = new ArrayList<>();
		for(int i = 0; i < bases.length; i++) {
			File base = bases[i];
			File variant1 = variants1[i];
			File variant2 = variants2[i];
			
			CtType<?> baseType = null;
			CtType<?> var1Type = null;
			CtType<?> var2Type = null;
			Diff diff = null;
			if(base != null) {
				baseType = getFullCtType(base, 
						SpoonHandler.getInstance().baseTypes());
			}
			if(variant1 != null) {
				var1Type = getFullCtType(variant1, 
						SpoonHandler.getInstance().firstVariantTypes());
				diff = dih.diff(baseType, var1Type);
				result.add(dih.getDeltaInstance(diff, var1Type, cp));
			}
			if(variant2 != null) {
				var2Type = getFullCtType(variant2, 
						SpoonHandler.getInstance().secondVariantTypes());
				diff = dih.diff(baseType, var2Type);
				result.add(dih.getDeltaInstance(diff, var2Type, cp));
			}
		}
		return result;
	}
}

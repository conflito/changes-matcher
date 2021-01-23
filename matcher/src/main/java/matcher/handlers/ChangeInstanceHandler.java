package matcher.handlers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import matcher.entities.BaseInstance;
import matcher.entities.ChangeInstance;
import matcher.entities.deltas.DeltaInstance;
import matcher.exceptions.ApplicationException;
import matcher.patterns.ConflictPattern;
import spoon.reflect.declaration.CtType;

public class ChangeInstanceHandler {

	private BaseInstanceHandler bih;
	private DeltaInstanceHandler dih;

	private SpoonHandler spoonHandler;
	
	public ChangeInstanceHandler(int trackLimit){
		this.spoonHandler = new SpoonHandler(trackLimit);
		this.bih = new BaseInstanceHandler();
		this.dih = new DeltaInstanceHandler();
	}
	
	public ChangeInstance getChangeInstance(File[] bases, File[] variants1, File[] variants2, 
			ConflictPattern  cp) throws ApplicationException {
		if(bases == null || variants1 == null || variants2 == null)
			return null;
		if(!sameLenght(bases, variants1, variants2))
			return null;
		
		ChangeInstance result = new ChangeInstance();
		spoonHandler.loadLaunchers(bases, variants1, variants2);
		spoonHandler.buildLaunchers();
		BaseInstance baseInstance = processBases(cp);
		List<DeltaInstance> deltas = processDeltas(bases, variants1, variants2, cp);
		
		result.setBaseInstance(baseInstance);
		result.addDeltaInstances(deltas);

		return result;
	}

	
	private boolean sameLenght(File[] bases, File[] variants1, File[] variants2) {
		return bases.length == variants1.length && bases.length == variants2.length;
	}
	
	private BaseInstance processBases(ConflictPattern cp) {
		BaseInstance result = new BaseInstance();
		for(CtType<?> type: spoonHandler.baseTypes()) {
			result.merge(bih.getBaseInstance(type, cp));
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
			
			CtType<?> baseType = null;
			CtType<?> var1Type = null;
			CtType<?> var2Type = null;
			if(base != null) {
				CtType<?> basicType = spoonHandler.getCtType(spoonHandler.getSpoonResource(base));
				baseType = spoonHandler.getFullChangedCtType(
								spoonHandler.getBaseLauncher(), basicType.getQualifiedName());
			}
			if(variant1 != null) {
				CtType<?> basicType = spoonHandler.getCtType(
						spoonHandler.getSpoonResource(variant1));
				var1Type = spoonHandler.getFullChangedCtType(
						spoonHandler.getVariantLauncher1(), basicType.getQualifiedName());
				result.add(dih.getDeltaInstance(baseType, var1Type, cp));
			}
			if(variant2 != null) {
				CtType<?> basicType = spoonHandler.getCtType(
						spoonHandler.getSpoonResource(variant2));
				var2Type = spoonHandler.getFullChangedCtType(
						spoonHandler.getVariantLauncher2(), basicType.getQualifiedName());
				result.add(dih.getDeltaInstance(baseType, var2Type, cp));
			}
		}
		return result;
	}
}

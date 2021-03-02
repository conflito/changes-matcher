package matcher.handlers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import gumtree.spoon.diff.Diff;
import matcher.catalogs.ConflictPatternCatalog;
import matcher.entities.BaseInstance;
import matcher.entities.ChangeInstance;
import matcher.entities.deltas.DeltaInstance;
import matcher.exceptions.ApplicationException;
import matcher.patterns.ConflictPattern;
import matcher.utils.Pair;
import spoon.reflect.declaration.CtType;

public class ChangeInstanceHandler {

	private final static Logger logger = Logger.getLogger(ChangeInstanceHandler.class);
	
	private BaseInstanceHandler bih;
	private DeltaInstanceHandler dih;

	private SpoonHandler spoonHandler;
	
	private ConflictPatternCatalog conflictsCatalog;
	
	public ChangeInstanceHandler(){
		this.spoonHandler = new SpoonHandler();
		this.bih = new BaseInstanceHandler();
		this.dih = new DeltaInstanceHandler();
		this.conflictsCatalog = new ConflictPatternCatalog();
	}
	
	public List<Pair<ChangeInstance, ConflictPattern>> getChangeInstances(File[] bases, 
			File[] variants1, File[] variants2) throws ApplicationException{
		if(bases == null || variants1 == null || variants2 == null)
			return null;
		if(!sameLenght(bases, variants1, variants2))
			return null;
		
		List<Pair<ChangeInstance, ConflictPattern>> result =
				new ArrayList<>();
		
		spoonHandler.loadLaunchers(bases, variants1, variants2);
		
		Diff[] firstDiffs = calculateDiffs(bases, variants1, spoonHandler.firstVariantTypes());
		Diff[] secondDiffs = calculateDiffs(bases, variants2, spoonHandler.secondVariantTypes());
		
		logger.info("Building system objects...");
		
		InstancesCache.createInstance();
		
		for(ConflictPattern cp: conflictsCatalog.getPatterns()) {
			ChangeInstance ci = new ChangeInstance();
			BaseInstance baseInstance = processBases(cp);
			List<DeltaInstance> deltas = new ArrayList<>();
			deltas.addAll(processDeltas(variants1, firstDiffs, cp));
			deltas.addAll(processDeltas(variants2, secondDiffs, cp));

			ci.setBaseInstance(baseInstance);
			ci.addDeltaInstances(deltas);

			result.add(new Pair<>(ci, cp));
		}
		
		return result;
	}
	
	public ChangeInstance getChangeInstance(File[] bases, File[] variants1, File[] variants2, 
			ConflictPattern  cp) throws ApplicationException {
		if(bases == null || variants1 == null || variants2 == null)
			return null;
		if(!sameLenght(bases, variants1, variants2))
			return null;
		
		InstancesCache.createInstance();
		
		ChangeInstance result = new ChangeInstance();

		spoonHandler.loadLaunchers(bases, variants1, variants2);
		
		logger.info("Building system objects...");

		BaseInstance baseInstance = processBases(cp);
		List<DeltaInstance> deltas = processDeltas(bases, variants1, variants2, cp);

		result.setBaseInstance(baseInstance);
		result.addDeltaInstances(deltas);

		return result;
	}
	
	private Diff[] calculateDiffs(File[] bases,	File[] variants, 
			Iterable<CtType<?>> elements) throws ApplicationException {
		Diff[] result = new Diff[bases.length];
		for(int i = 0; i < bases.length; i++) {
			File base = bases[i];
			File variant = variants[i];

			CtType<?> baseType = null;
			CtType<?> varType = null;
			
			if(base != null) {
				baseType = getFullCtType(base, spoonHandler.baseTypes());
			}
			if(variant != null) {
				varType = getFullCtType(variant, elements);
			}
			result[i] = dih.diff(baseType, varType);
		}
		return result;
	}
	
	private CtType<?> getFullCtType(File file, Iterable<CtType<?>> elements) 
			throws ApplicationException{
		
		CtType<?> basicType = spoonHandler.getCtType(spoonHandler.getSpoonResource(file));
		return spoonHandler.getFullCtType(elements, basicType.getQualifiedName());
	}
	
	private boolean sameLenght(File[] bases, File[] variants1, File[] variants2) {
		return bases.length == variants1.length && bases.length == variants2.length;
	}
	
	private BaseInstance processBases(ConflictPattern cp) {
		return bih.getBaseInstance(spoonHandler.baseTypes(), cp);
	}
	
	private List<DeltaInstance> processDeltas(File[] variants, Diff[] diffs, 
			ConflictPattern  cp) throws ApplicationException {
		List<DeltaInstance> result = new ArrayList<>();
		for(int i = 0; i < variants.length; i++) {
			File variant = variants[i];
			if(variant != null) {
				CtType<?> basicType = spoonHandler.getCtType(
						spoonHandler.getSpoonResource(variant));

				CtType<?> varType = spoonHandler.getFullCtType(spoonHandler.firstVariantTypes(), 
						basicType.getQualifiedName());
				
				result.add(dih.getDeltaInstance(diffs[i], varType, cp));
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
			
			CtType<?> baseType = null;
			CtType<?> var1Type = null;
			CtType<?> var2Type = null;
			Diff diff = null;
			if(base != null) {
				baseType = getFullCtType(base, spoonHandler.baseTypes());
			}
			if(variant1 != null) {
				var1Type = getFullCtType(variant1, spoonHandler.firstVariantTypes());
				diff = dih.diff(baseType, var1Type);
				result.add(dih.getDeltaInstance(diff, var1Type, cp));
			}
			if(variant2 != null) {
				var2Type = getFullCtType(variant2, spoonHandler.secondVariantTypes());
				diff = dih.diff(baseType, var2Type);
				result.add(dih.getDeltaInstance(diff, var2Type, cp));
			}
		}
		return result;
	}
}

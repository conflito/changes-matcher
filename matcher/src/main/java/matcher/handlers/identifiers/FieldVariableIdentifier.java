package matcher.handlers.identifiers;

import java.io.File;
import java.util.List;
import java.util.Map;

import matcher.entities.ChangeInstance;
import matcher.exceptions.ApplicationException;
import matcher.handlers.ChangeInstanceHandler;
import matcher.handlers.MatchingHandler;
import matcher.patterns.BasePattern;
import matcher.patterns.ClassPattern;
import matcher.patterns.FieldPattern;
import matcher.patterns.FreeVariable;
import matcher.patterns.MethodPattern;
import matcher.patterns.deltas.DeltaPattern;
import matcher.patterns.deltas.InsertConstructorPatternAction;
import matcher.patterns.deltas.InsertFieldPatternAction;
import matcher.patterns.deltas.InsertMethodPatternAction;
import matcher.utils.MapUtilities;
import matcher.utils.Pair;
import matcher.patterns.ConflictPattern;
import matcher.patterns.ConstructorPattern;
import matcher.patterns.FieldAccessPattern;

public class FieldVariableIdentifier implements IVariableIdentifier{
	
	@Override
	public Map<Integer, List<String>> identify(ChangeInstance changeInstance, 
			ConflictPattern conflictPattern) {
		Map<Integer, List<String>> result = fieldsInBase(changeInstance, conflictPattern);
		MapUtilities.mergeMaps(result, fieldsInDeltas(changeInstance, conflictPattern));
		return result;
	}
	
	private Map<Integer, List<String>> fieldsInBase(ChangeInstance changeInstance,
			ConflictPattern conflictPattern){
		List<Integer> vars = conflictPattern.getFieldsVariableIds();
		List<String> fields = changeInstance.getFieldsQualifiedNames();
		return MapUtilities.combine(vars, fields);
	}
	
	private Map<Integer, List<String>> fieldsInDeltas(ChangeInstance changeInstance,
			ConflictPattern conflictPattern){		
		List<Integer> vars = conflictPattern.getDeltaFieldsVariableIds();
		List<String> fields = changeInstance.getDeltaFieldsQualifiedNames();
		return MapUtilities.combine(vars, fields);
	}
	
	public static void main(String[] args) throws ApplicationException {
		BasePattern pattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(new FreeVariable(0));
		FieldPattern fieldPattern = new FieldPattern(new FreeVariable(1), null);
		classPattern.addFieldPattern(fieldPattern);
//		ConstructorPattern cPattern = new ConstructorPattern(new FreeVariable(5), null);
//		classPattern.addConstructorPattern(cPattern);
//		MethodPattern methodPattern = new MethodPattern(new FreeVariable(3), null);
//		FieldAccessPattern fAccess = new FieldAccessPattern(new FreeVariable(1), null);
//		methodPattern.addFieldAccessPattern(fAccess);
//		classPattern.addMethodPattern(methodPattern);
		pattern.addClassPattern(classPattern);
		
		DeltaPattern dp = new DeltaPattern();
		dp.addActionPattern(new InsertFieldPatternAction(new FreeVariable(2),
				new FreeVariable(0), null));
//		dp.addActionPattern(new InsertMethodPatternAction(new FreeVariable(4), 
//				new FreeVariable(0), null));
//		dp.addActionPattern(new InsertConstructorPatternAction(new FreeVariable(6), 
//				new FreeVariable(0)	, null));
		ConflictPattern cp = new ConflictPattern(pattern, dp, new DeltaPattern());

		
		File base = new File("src/test/resources/Square.java");
		File firstVar = new File("src/test/resources/Square01.java");
		File secondVar = new File("src/test/resources/Square02.java");
		ChangeInstanceHandler cih = new ChangeInstanceHandler();

		ChangeInstance ci = cih.getChangeInstance(base, firstVar, secondVar, cp);
		System.out.println(ci);
		
		MatchingHandler mh = new MatchingHandler();
		System.out.println(mh.matchingAssignments(ci, cp));
	}

}

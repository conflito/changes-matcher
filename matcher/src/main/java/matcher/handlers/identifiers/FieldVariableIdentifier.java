package matcher.handlers.identifiers;

import java.io.File;
import java.util.List;
import java.util.Map;

import matcher.entities.ChangeInstance;
import matcher.exceptions.ApplicationException;
import matcher.handlers.ChangeInstanceHandler;
import matcher.patterns.BasePattern;
import matcher.patterns.ClassPattern;
import matcher.patterns.FieldPattern;
import matcher.patterns.FreeVariable;
import matcher.patterns.MethodPattern;
import matcher.patterns.deltas.DeltaPattern;
import matcher.patterns.deltas.InsertConstructorPatternAction;
import matcher.patterns.deltas.InsertFieldPatternAction;
import matcher.patterns.deltas.InsertMethodPatternAction;
import matcher.patterns.ConflictPattern;
import matcher.patterns.ConstructorPattern;
import matcher.patterns.FieldAccessPattern;

public class FieldVariableIdentifier extends AbstractVariableIdentifier{

	
	@Override
	public Map<Integer, List<String>> identify(ChangeInstance changeInstance, 
			ConflictPattern conflictPattern) {
		Map<Integer, List<String>> result = fieldsInBase(changeInstance, conflictPattern);
		mergeMaps(result, fieldsInDeltas(changeInstance, conflictPattern));
		return result;
	}
	
	private Map<Integer, List<String>> fieldsInBase(ChangeInstance changeInstance,
			ConflictPattern conflictPattern){
		List<Integer> vars = conflictPattern.getFieldsVariableIds();
		List<String> fields = changeInstance.getFieldsQualifiedNames();
		return combine(vars, fields);
	}
	
	private Map<Integer, List<String>> fieldsInDeltas(ChangeInstance changeInstance,
			ConflictPattern conflictPattern){		
		List<Integer> vars = conflictPattern.getDeltaFieldsVariableIds();
		List<String> fields = changeInstance.getDeltaFieldsQualifiedNames();
		return combine(vars, fields);
	}
	
	public static void main(String[] args) throws ApplicationException {
		BasePattern pattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(new FreeVariable(0));
		FieldPattern fieldPattern = new FieldPattern(new FreeVariable(1), null);
		classPattern.addFieldPattern(fieldPattern);
		ConstructorPattern cPattern = new ConstructorPattern(new FreeVariable(5), null);
		classPattern.addConstructorPattern(cPattern);
		MethodPattern methodPattern = new MethodPattern(new FreeVariable(3), null);
		FieldAccessPattern fAccess = new FieldAccessPattern(new FreeVariable(1), null);
		methodPattern.addFieldAccessPattern(fAccess);
		classPattern.addMethodPattern(methodPattern);
		pattern.addClassPattern(classPattern);
		
		DeltaPattern dp = new DeltaPattern();
		dp.addActionPattern(new InsertFieldPatternAction(new FreeVariable(2),
				new FreeVariable(0), null));
		dp.addActionPattern(new InsertMethodPatternAction(new FreeVariable(4), 
				new FreeVariable(0), null));
		dp.addActionPattern(new InsertConstructorPatternAction(new FreeVariable(6), 
				new FreeVariable(0)	, null));
		ConflictPattern cp = new ConflictPattern(pattern, dp, new DeltaPattern());

		
		File base = new File("src/main/java/base/Square.java");
		File firstVar = new File("src/main/java/branch01/Square.java");
		File secondVar = new File("src/main/java/branch02/Square.java");
		ChangeInstanceHandler cih = new ChangeInstanceHandler();

		ChangeInstance ci = cih.getChangeInstance(base, firstVar, secondVar, cp);
		System.out.println(ci);
		
		FieldVariableIdentifier fvi = new FieldVariableIdentifier();
		MethodVariableIdentifier mvi = new MethodVariableIdentifier();
		ConstructorVariableIdentifier cvi = new ConstructorVariableIdentifier();
		ClassVariableIdentifier clvi = new ClassVariableIdentifier();
		
		Map<Integer, List<String>> res = fvi.identify(ci, cp);
		System.out.println("FIELDS: " + res);
		
		res = mvi.identify(ci, cp);
		System.out.println("METHODS: " + res);
		
		res = cvi.identify(ci, cp);
		System.out.println("CONSTRUCTORS: " + res);
		
		res = clvi.identify(ci, cp);
		System.out.println("CLASSES: " + res);
	}

}

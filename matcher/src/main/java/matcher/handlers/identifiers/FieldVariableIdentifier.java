package matcher.handlers.identifiers;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import matcher.entities.ChangeInstance;
import matcher.exceptions.ApplicationException;
import matcher.handlers.ChangeInstanceHandler;
import matcher.patterns.BasePattern;
import matcher.patterns.ClassPattern;
import matcher.patterns.FieldPattern;
import matcher.patterns.FreeVariable;
import matcher.patterns.deltas.DeltaPattern;
import matcher.patterns.ConflictPattern;

public class FieldVariableIdentifier implements VariableValueIdentifier {

	
	@Override
	public Map<Integer, List<String>> identify(ChangeInstance changeInstance, 
			ConflictPattern conflictPattern) {
		Map<Integer, List<String>> result =  new HashMap<>();
		List<Integer> vars = conflictPattern.getFieldsVariableIds();
		List<String> fields = changeInstance.getFieldsQualifiedNames();
		for(int i: vars) {
			result.put(i, fields);
		}
		return result;
	}
	
	public static void main(String[] args) throws ApplicationException {
		BasePattern pattern = new BasePattern();
		ClassPattern classPattern = new ClassPattern(new FreeVariable(0));
		ClassPattern classPattern2 = new ClassPattern(new FreeVariable(4));
		FieldPattern fieldPattern3 = new FieldPattern(new FreeVariable(5), null);
		classPattern2.addFieldPattern(fieldPattern3);
		classPattern.setSuperClass(classPattern2);
		FieldPattern fieldPattern = new FieldPattern(new FreeVariable(1), null);
		FieldPattern fieldPattern2 = new FieldPattern(new FreeVariable(2), null);
		classPattern.addFieldPattern(fieldPattern);
		classPattern.addFieldPattern(fieldPattern2);
		pattern.addClassPattern(classPattern);
		ConflictPattern cp = new ConflictPattern(pattern, new DeltaPattern(), new DeltaPattern());

		FieldVariableIdentifier fvi = new FieldVariableIdentifier();
		File base = new File("src/main/java/base/Square.java");
		File firstVar = new File("src/main/java/branch01/Square.java");
		File secondVar = new File("src/main/java/branch02/Square.java");
		ChangeInstanceHandler cih = new ChangeInstanceHandler();

		ChangeInstance ci = cih.getChangeInstance(base, firstVar, secondVar, cp);
		System.out.println(ci);
		Map<Integer, List<String>> res = fvi.identify(ci, cp);
		System.out.println(res);
	}

}

package matcher.patterns.goals;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import matcher.patterns.ClassPattern;
import matcher.patterns.MethodPattern;

public class BDDTestingGoal {

	private Map<ClassPattern, List<MethodPattern>> context;
	
	private ClassPattern initialize;
	
	private List<MethodPattern> toCall;
	
	public BDDTestingGoal() {
		super();
		this.context = new HashMap<>();
		this.toCall = new ArrayList<>();
	}
	
	public BDDTestingGoal(BDDTestingGoal testingGoal) {
		super();
		this.context = new HashMap<>();
		this.toCall = new ArrayList<>();
		
		for(Entry<ClassPattern, List<MethodPattern>> e: testingGoal.context.entrySet()) {
			ClassPattern classCopy = new ClassPattern(e.getKey());
			List<MethodPattern> methodsCopy = new ArrayList<>();
			for(MethodPattern m: e.getValue()) {
				methodsCopy.add(new MethodPattern(m));
			}
			this.context.put(classCopy, methodsCopy);
		}
		
		this.initialize = new ClassPattern(testingGoal.initialize);
		
		for(MethodPattern m: testingGoal.toCall) {
			this.toCall.add(new MethodPattern(m));
		}
	}
	
	public void addClassToContext(ClassPattern c) {
		if(!context.containsKey(c))
			context.put(c, new ArrayList<>());
	}
	
	public void addMethodToContext(ClassPattern c, MethodPattern m) {
		addClassToContext(c);
		context.get(c).add(m);
	}
	
	public void setClassToInitialize(ClassPattern c) {
		this.initialize = c;
	}
	
	public void addMethodToCall(MethodPattern m) {
		this.toCall.add(m);
	}
	
	private void merge() {
		Map<ClassPattern, List<MethodPattern>> archive = new HashMap<>();
		Map<String, List<MethodPattern>> seen = new HashMap<>();
		
		for(Entry<ClassPattern, List<MethodPattern>> e: context.entrySet()) {
			ClassPattern c = e.getKey();
			String cName = c.getFreeVariable().getValue();
			if(!seen.containsKey(cName)) {
				seen.put(cName, e.getValue());
			}
			else {
				seen.get(cName).addAll(e.getValue());
			}
		}
		Set<String> added = new HashSet<>();
		for(ClassPattern c: context.keySet()) {
			String cName = c.getFreeVariable().getValue();
			if(!added.contains(cName)) {
				archive.put(c, seen.get(cName));
				added.add(cName);
			}
				
		}
		
		context = archive;
	}
	
	public String getTestBDD() {
		merge();
		StringBuilder result = new StringBuilder();
		for(ClassPattern c: context.keySet()) {
			result.append(giveContext(c) + "\n");
		}
		result.append("Create the initialization of " 
			+ initialize.getFreeVariable().getValue() + "\n");
		for(MethodPattern m: toCall) {
			result.append("And a call to " 
					+ m.getLastMatchedMethodIdentifier() + "\n");
		}
		return result.toString();
	}
	
	private String giveContext(ClassPattern c) {
		StringBuilder result = new StringBuilder("Given a class ");
		result.append(c.getFreeVariable().getValue());
		
		List<MethodPattern> methods = context.get(c);
		for(MethodPattern m: methods) {
			result.append(" with method " + m.getLastMatchedMethodIdentifier());
			result.append(" and");
		}
		if(result.length() > 0)
			result.delete(result.length()-4, result.length());
		
		for(MethodPattern m: methods) {
			String methodContext = giveContext(c, m);
			if(methodContext.length() > 0)
				result.append("\n" + methodContext);
		}
		
		return result.toString();
	}
	
	private String giveContext(ClassPattern c, MethodPattern m) {
		StringBuilder result = new StringBuilder();
		
		for(int id: m.getDependenciesVariableIds()) {
			result.append("And method " + m.getLastMatchedMethodIdentifier());
			result.append(" depends on " + findByVariableId(id).getLastMatchedMethodIdentifier());
			result.append("\n");
		}
		if(result.length() > 0)
			result.delete(result.length()-1, result.length());
		
		List<Integer> compatibles = c.getCompatibles(m.getVariableId());
		if(compatibles != null) {
			result.append("\n");
			for(int id: compatibles) {
				result.append("And method " + m.getLastMatchedMethodIdentifier());
				result.append(" compatible with " + 
						findByVariableId(id).getLastMatchedMethodIdentifier());
				result.append("\n");
			}
		}
		
		return result.toString();
	}
	
	private MethodPattern findByVariableId(int id) {
		for(List<MethodPattern> l: context.values()) {
			for(MethodPattern m: l) {
				if(m.getVariableId() == id)
					return m;
			}
		}
		return null;
	}
	
}

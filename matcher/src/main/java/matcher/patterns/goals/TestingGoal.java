package matcher.patterns.goals;

import java.util.ArrayList;
import java.util.List;

import matcher.patterns.ClassPattern;
import matcher.patterns.MethodPattern;
import matcher.utils.Pair;

public class TestingGoal {

	private ClassPattern targetClass;

	private List<Pair<ClassPattern, MethodPattern>> methodsToCall;

	public TestingGoal(ClassPattern targetClass) {
		super();
		this.targetClass = targetClass;
		methodsToCall = new ArrayList<>();
	}
	
	public TestingGoal(TestingGoal goal) {
		super();
		this.targetClass = new ClassPattern(goal.targetClass);
		this.methodsToCall = new ArrayList<>();
		for(Pair<ClassPattern, MethodPattern> p: goal.methodsToCall) {
			ClassPattern cp = new ClassPattern(p.getFirst());
			MethodPattern mp = new MethodPattern(p.getSecond());
			this.methodsToCall.add(new Pair<>(cp, mp));
		}
	}
	
	public void addMethodToCall(ClassPattern cp, MethodPattern mp) {
		this.methodsToCall.add(new Pair<>(cp, mp));
	}
	
	public String getTargetClass() {
		return getClassName(targetClass);
	}
	
	public List<String> getMethodsToCall() {
		List<String> result = new ArrayList<>();
		
		for(Pair<ClassPattern, MethodPattern> p: methodsToCall) {
			ClassPattern cp = p.getFirst();
			MethodPattern mp = p.getSecond();
			
			result.add(getClassName(cp) + "." 
					+ mp.getLastMatchedMethodIdentifier());
		}
		
		return result;
	}
	
	private String getClassName(ClassPattern cp) {
		return cp.getFreeVariable().getValue();
	}
}

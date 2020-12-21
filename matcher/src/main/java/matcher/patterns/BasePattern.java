package matcher.patterns;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import matcher.entities.BaseInstance;

public class BasePattern {

	private List<ClassPattern> classPatterns;

	public BasePattern() {
		super();
		classPatterns =  new ArrayList<>();
	}
	
	public void addClassPattern(ClassPattern pattern) {
		classPatterns.add(pattern);
	}
	
	public boolean hasFields() {
		return classPatterns.stream().anyMatch(ClassPattern::hasFields);
	}
	
	public boolean hasMethods() {
		return classPatterns.stream().anyMatch(ClassPattern::hasMethods);
	}
	
	public boolean hasConstructors() {
		return classPatterns.stream().anyMatch(ClassPattern::hasConstructors);
	}
	
	public boolean hasSuperClass() {
		return classPatterns.stream().anyMatch(ClassPattern::hasSuperClass);
	}
	
	public boolean hasCompatible() {
		return classPatterns.stream().anyMatch(ClassPattern::hasMethods);
	}
	
	public boolean hasFieldAccesses() {
		return classPatterns.stream().anyMatch(ClassPattern::hasFieldAccesses);
	}
	
	public boolean hasInvocations() {
		return classPatterns.stream().anyMatch(ClassPattern::hasInvocations);
	}
	
	public List<Integer> getFieldsVariableIds(){
		List<Integer> result = new ArrayList<>();
		for(ClassPattern c: classPatterns) {
			result.addAll(c.getFieldVariableIds());
		}
		return distinct(result);
	}
	
	public List<Integer> getMethodsVariableIds(){
		List<Integer> result = new ArrayList<>();
		for(ClassPattern c: classPatterns) {
			result.addAll(c.getMethodVariableIds());
		}
		return distinct(result);
	}
	
	public List<Integer> getConstructorsVariableIds(){
		List<Integer> result = new ArrayList<>();
		for(ClassPattern c: classPatterns) {
			result.addAll(c.getConstructorsVariableIds());
		}
		return distinct(result);
	}
	
	public List<Integer> getFieldAccessesVariableIds(){
		List<Integer> result = new ArrayList<>();
		for(ClassPattern c: classPatterns) {
			result.addAll(c.getFieldAccessesIds());
		}
		return distinct(result);
	}
	
	public List<Integer> getInvocationsVariableIds(){
		List<Integer> result = new ArrayList<>();
		for(ClassPattern c: classPatterns) {
			result.addAll(c.getInvocationsVariableIds());
		}
		return distinct(result);
	}
	
	public void setVariableValue(int id, String value) {
		for(ClassPattern c: classPatterns) {
			c.setVariableValue(id, value);
		}
	}
	
	public boolean filled() {
		return classPatterns.stream().allMatch(ClassPattern::filled);
	}
	
	public boolean matches(BaseInstance instance) {
		return filled() && 
			   classPatterns.stream().allMatch(c-> c.matchesOne(instance.getClassInstances()));
	}
	
	private List<Integer> distinct(List<Integer> list){
		return list.stream().distinct().collect(Collectors.toList());
	}
	
	public String toStringDebug() {
		StringBuilder result = new StringBuilder();
		
		for(ClassPattern c: classPatterns)
			result.append(c.toStringDebug() + "\n");
		
		return result.toString();
	}
}

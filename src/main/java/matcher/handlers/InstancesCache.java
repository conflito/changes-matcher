package matcher.handlers;

import java.util.HashMap;
import java.util.Map;

import matcher.utils.Pair;
import matcher.entities.MethodInstance;
import matcher.entities.deltas.ActionInstance;
import matcher.entities.ClassInstance;
import matcher.entities.ConstructorInstance;
import matcher.entities.FieldInstance;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.*;
import gumtree.spoon.diff.Diff;

public class InstancesCache {

	private static InstancesCache instance;
	
	private Map<CtMethod<?>, MethodInstance> methods;
	private Map<CtClass<?>, ClassInstance> classes;
	private Map<CtConstructor<?>, ConstructorInstance> constructors;
	private Map<CtField<?>, FieldInstance> fields;
	
	private Map<Pair<CtType<?>, CtType<?>>, Diff> diffs;
	private Map<String, CtType<?>> basicTypes;
	
	private InstancesCache() {
		methods = new HashMap<>();
		classes = new HashMap<>();
		constructors = new HashMap<>();
		fields = new HashMap<>();
		diffs = new HashMap<>();
		basicTypes = new HashMap<>();
	}
	
	public boolean hasMethod(CtMethod<?> method) {
		return methods.containsKey(method);
	}
	
	public MethodInstance getMethod(CtMethod<?> method) {
		return methods.get(method);
	}
	
	public void putMethod(CtMethod<?> method, MethodInstance methodInstance) {
		methods.put(method, methodInstance);
	}
	
	public boolean hasClass(CtClass<?> c) {
		return classes.containsKey(c);
	}
	
	public ClassInstance getClass(CtClass<?> c) {
		return classes.get(c);
	}
	
	public void putClass(CtClass<?> c, ClassInstance classInstance) {
		classes.put(c, classInstance);
	}
	
	public boolean hasConstructor(CtConstructor<?> c) {
		return constructors.containsKey(c);
	}
	
	public ConstructorInstance getConstructor(CtConstructor<?> c) {
		return constructors.get(c);
	}
	
	public void putConstructor(CtConstructor<?> c, ConstructorInstance cInstance) {
		constructors.put(c, cInstance);
	}
	
	public boolean hasField(CtField<?> field) {
		return fields.containsKey(field);
	}
	
	public FieldInstance getField(CtField<?> field) {
		return fields.get(field);
	}
	
	public void putField(CtField<?> field, FieldInstance fieldInstance) {
		fields.put(field, fieldInstance);
	}
	
	public boolean hasDiff(CtType<?> t1, CtType<?> t2) {
		return diffs.containsKey(new Pair<>(t1, t2));
	}
	
	public Diff getDiff(CtType<?> t1, CtType<?> t2) {
		return diffs.get(new Pair<>(t1, t2));
	}
	
	public void putDiff(CtType<?> t1, CtType<?> t2, Diff diff) {
		diffs.put(new Pair<>(t1, t2), diff);
	}
	
	public boolean hasBasicType(String s) {
		return basicTypes.containsKey(s);
	}
	
	public CtType<?> getBasicType(String s){
		return basicTypes.get(s);
	}
	
	public void putBasicType(String s, CtType<?> t) {
		basicTypes.put(s, t);
	}
	
	public static void createInstance() {
		instance = new InstancesCache();
	}
	
	public static InstancesCache getInstance() {
		return instance;
	}
}

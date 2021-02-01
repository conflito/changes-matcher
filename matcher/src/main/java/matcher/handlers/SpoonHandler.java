package matcher.handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Queue;

import gumtree.spoon.AstComparator;
import matcher.exceptions.ApplicationException;
import spoon.Launcher;
import spoon.compiler.SpoonResource;
import spoon.compiler.SpoonResourceHelper;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtSuperAccess;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtArrayTypeReference;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;

public class SpoonHandler {
	
	private Launcher baseLauncher;
	private Launcher variantLauncher1;
	private Launcher variantLauncher2;
	
	private Set<CtType<?>> baseTypes;
	private Set<CtType<?>> variant1Types;
	private Set<CtType<?>> variant2Types;
	
	public SpoonHandler() {
		this.baseLauncher = new Launcher();
		this.variantLauncher1 = new Launcher();
		this.variantLauncher2 = new Launcher();
	}
	
	public Launcher getBaseLauncher() {
		return baseLauncher;
	}

	public Launcher getVariantLauncher1() {
		return variantLauncher1;
	}

	public Launcher getVariantLauncher2() {
		return variantLauncher2;
	}

	public SpoonResource getSpoonResource(File f) throws ApplicationException {
		SpoonResource resource;
		try {
			resource = SpoonResourceHelper.createResource(f);
		} catch (FileNotFoundException e) {
			throw new ApplicationException("Invalid specified file", e);
		}
		return resource;
	}
	
	public CtType<?> getCtType(SpoonResource resource){
		return new AstComparator().getCtType(resource);
	}
	
	public CtClass<?> getCtClass(SpoonResource resource){
		return (CtClass<?>) getCtType(resource);
	}
	
	public void buildLaunchers() {
		baseLauncher.buildModel();
		variantLauncher1.buildModel();
		variantLauncher2.buildModel();
	}
	
	public Iterable<CtType<?>> baseTypes(){
		return baseTypes;
	}
	
	public Iterable<CtType<?>> firstVariantTypes(){
		return variant1Types;
	}
	
	public Iterable<CtType<?>> secondVariantTypes(){
		return variant2Types;
	}
	
	public void loadLaunchers(File[] bases, File[] variants1, File[] variants2) 
					throws ApplicationException {
		baseLauncher.addInputResource(PropertiesHandler.getInstance().getBaseSourceDirPath());
		
		variantLauncher1.addInputResource(PropertiesHandler.getInstance()
				.getFirstVariantSourceDirPath());
		
		variantLauncher2.addInputResource(PropertiesHandler.getInstance()
				.getSecondVariantSourceDirPath());
		
		buildLaunchers();
		
		baseTypes = getElements(baseLauncher, bases);
		variant1Types = getElements(variantLauncher1, variants1);
		variant2Types = getElements(variantLauncher2, variants2);

		addDeltaTypesToBase(variants1, variants2);
	}
	
	private void addDeltaTypesToBase(File[] variants1, File[] variants2) throws ApplicationException {
		Set<String> baseNames = baseTypes.stream()
				.map(t -> t.getQualifiedName())
				.collect(Collectors.toSet());
		for(CtType<?> type: variant1Types) {
			if(!baseNames.contains(type.getQualifiedName()) && 
					!isNewClass(type.getQualifiedName(), variants1)) {
				baseTypes.add(type);
			}
		}
		for(CtType<?> type: variant2Types) {
			if(!baseNames.contains(type.getQualifiedName()) && 
					!isNewClass(type.getQualifiedName(), variants2)) {
				baseTypes.add(type);
			}
		}
	}
	
	private boolean isNewClass(String className, File[] variants) throws ApplicationException {
		for(File f: variants) {
			if(f != null) {
				SpoonResource resource = getSpoonResource(f);
				CtType<?> type = getCtType(resource);
				if(className.equals(type.getQualifiedName()))
					return true;
			}
			
		}
		return false;
	}
	
	private boolean isComposedField(CtTypeReference<?> fieldType)  {
		try {
			fieldType.getAccessType();
			return true;
		}
		catch(Exception e) {}
		return false;
	}
	
	private static boolean fromTheSameClass(CtInvocation<?> invocation, String classQualifiedName) {
		try {
			return getInvocationClassQualifiedName(invocation).equals(classQualifiedName);
		}
		catch(Exception e) {
			return false;
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean isSuperInvocation(CtInvocation<?> invocation) {
		return invocation.getExecutable().isConstructor() ||
				!invocation.getElements(new TypeFilter(CtSuperAccess.class)).isEmpty();
	}
	
	public CtType<?> getFullCtType(Iterable<CtType<?>> types, String typeName){
		for(CtType<?> type: types) {
			if(type.getQualifiedName().equals(typeName)) {
				return type;
			}
		}
		return null;
	}
	
	private Set<CtType<?>> getElements(Launcher launcher, File[] files) throws ApplicationException {
		Set<CtType<?>> result = new HashSet<>();
		for(File f: files) {
			if(f != null) {
				SpoonResource resource = getSpoonResource(f);
				CtType<?> changedType = getCtType(resource);
				getElements(result, launcher, changedType.getQualifiedName());
			}
		}
		return result;
	}
	
	private void getElements(Set<CtType<?>> result, 
			Launcher launcher, String changedClassName) {

		Collection<CtType<?>> types = launcher.getModel().getAllTypes();
		Queue<CtMethod<?>> methodsToVisit = new ArrayDeque<>();
		
		CtType<?> changedType = addChangedClass(types, changedClassName, result);
		addDirectDependants(types, changedClassName, result, methodsToVisit);
		addDependants(types, result, methodsToVisit);
		addDirectDependencies(changedType, result, methodsToVisit);
		addDependencies(types, result, methodsToVisit);

	}
	
	private void addType(CtType<?> type, Set<CtType<?>> result) {
		result.add(type);
		type.getSuperInterfaces().forEach(i -> result.add(i.getTypeDeclaration()));
		addSuperClasses(type, result);
		addFields(type, result);
	}
	
	private void addFields(CtType<?> type, Set<CtType<?>> result) {
		for(CtFieldReference<?> f: type.getDeclaredFields()) {
			CtTypeReference<?> fieldType = f.getDeclaration().getType();
			if(!fieldType.isPrimitive()) {
				if(isComposedField(fieldType)) {
					fieldType = fieldType.getAccessType();
				}
				else if(fieldType.isArray()){
					fieldType = getArrayType(fieldType);
				}
				result.add(fieldType.getTypeDeclaration());
			}
		}
	}
	
	private void addSuperClasses(CtType<?> type, Set<CtType<?>> result) {
		CtTypeReference<?> superClass = type.getSuperclass();
		if(superClass != null) {
			result.add(superClass.getTypeDeclaration());
			addSuperClasses(superClass.getTypeDeclaration(), result);
		}
	}
	
	private CtTypeReference<?> getArrayType(CtTypeReference<?> type){
		CtTypeReference<?> result = type;
		do {
			CtArrayTypeReference<?> arrayTypeRef = (CtArrayTypeReference<?>) result;
			result = arrayTypeRef.getComponentType();
		}while(result.isArray());
		return result;
	}
	
	private CtType<?> addChangedClass(Collection<CtType<?>> types, String changedClassName,
			Set<CtType<?>> result) {
		CtType<?> typeResult = null;
		for(CtType<?> type: types) {
			if(type.getQualifiedName().equals(changedClassName)) {
				typeResult = type;
				addType(type, result);
				break;
			}
		}
		return typeResult;
	}
	
	private void addDirectDependants(Collection<CtType<?>> types, String changedClassName,
			Set<CtType<?>> result, Queue<CtMethod<?>> methodsToVisit) {
		types.forEach(type -> {
			if(!type.getQualifiedName().equals(changedClassName)) {
				List<CtInvocation<?>> invocations = getInvocationsInType(type);
				for(CtInvocation<?> i: invocations) {
					if(invocationFromTheSystem(i) && !isSuperInvocation(i) &&
							isInvocationToClass(i, changedClassName)) {
						addType(type, result);
						CtMethod<?> invokingMethod = i.getParent(CtMethod.class);
						if(!methodsToVisit.contains(invokingMethod)) {
							methodsToVisit.add(invokingMethod);
						}
					}
				}
			}
			
		});
	}
	
	private void addDependants(Collection<CtType<?>> types, Set<CtType<?>> result, 
			Queue<CtMethod<?>> methodsToVisit) {
		while(!methodsToVisit.isEmpty()) {
			CtMethod<?> method = methodsToVisit.poll();
			types.forEach(type -> {
				if(!result.contains(type)) {
					List<CtInvocation<?>> invocations = getInvocationsInType(type);
					for(CtInvocation<?> i: invocations) {
						if(invocationFromTheSystem(i)) {
							CtMethod<?> invokedMethod = getMethodFromInvocation(i);
							if(method.equals(invokedMethod)) {
								CtMethod<?> invokingMethod = i.getParent(CtMethod.class);
								addType(type, result);
								if(!methodsToVisit.contains(invokingMethod)) {
									methodsToVisit.add(invokingMethod);
								}
							}
						}
					}
				}
			});
		}
	}
	
	private void addDirectDependencies(CtType<?> changedType, Set<CtType<?>> result, 
			Queue<CtMethod<?>> methodsToVisit) {
		List<CtInvocation<?>> invocations = getInvocationsInType(changedType);
		for(CtInvocation<?> i: invocations) {
			if(invocationFromTheSystem(i) && !isSuperInvocation(i) &&
					!fromTheSameClass(i, changedType.getQualifiedName())) {
				addType(getParentTypeFromInvocation(i), result);
				CtMethod<?> invokedMethod = SpoonHandler.getMethodFromInvocation(i);
				if(!methodsToVisit.contains(invokedMethod)) {
					methodsToVisit.add(invokedMethod);
				}
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addDependencies(Collection<CtType<?>> types, Set<CtType<?>> result, 
			Queue<CtMethod<?>> methodsToVisit) {
		while(!methodsToVisit.isEmpty()) {
			CtMethod<?> method = methodsToVisit.poll();
			types.forEach(type -> {
				Set<CtMethod<?>> methods = type.getMethods();
				for(CtMethod<?> m: methods) {
					if(method.equals(m)) {
						addType(type, result);
						List<CtInvocation<?>> invocations = 
								m.getElements(new TypeFilter(CtInvocation.class));
						for(CtInvocation<?> i: invocations) {
							CtMethod<?> invokedMethod = getMethodFromInvocation(i);
							if(!methodsToVisit.contains(invokedMethod)) {
								methodsToVisit.add(invokedMethod);
							}
						}
					}
				}
			});
		}
	}
	
	public static boolean invocationFromTheSystem(CtInvocation<?> invocation) {
		return invocation.getExecutable() != null && 
				invocation.getExecutable().getDeclaringType() != null;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<CtInvocation<?>> getInvocationsInType(CtType<?> type){
		return type.getElements(new TypeFilter(CtInvocation.class));
	}
	
	public static CtMethod<?> getMethodFromInvocation(CtInvocation<?> invocation){
		String invokedName = invocation.getExecutable().getSimpleName();
		CtTypeReference<?>[] types = invocation.getExecutable().getParameters()
				.toArray(new CtTypeReference<?>[0]);
		return invocation.getExecutable()
			.getDeclaringType()
			.getTypeDeclaration()
			.getMethod(invokedName, types);
	}
	
	public CtType<?> getParentTypeFromInvocation(CtInvocation<?> invocation){
		return getMethodFromInvocation(invocation).getTopLevelType();
	}
	
	public static String getInvocationFullName(CtInvocation<?> invocation) {
		return getInvocationClassQualifiedName(invocation) + "." + 
				getInvocationQualifiedName(invocation);
	}

	public static String getInvocationQualifiedName(CtInvocation<?> invocation) {
		return invocation.getExecutable().getSignature().replace(",", ", ");
	}

	public static String getInvocationClassSimpleName(CtInvocation<?> invocation) {
		return invocation.getExecutable().getDeclaringType().getSimpleName();
	}
	
	public static String getInvocationClassQualifiedName(CtInvocation<?> invocation) {
		return invocation.getExecutable().getDeclaringType().getQualifiedName();
	}
	
	public boolean isInvocationToClass(CtInvocation<?> invocation, String className) {
		return getInvocationClassQualifiedName(invocation).equals(className);
	}
	
	public boolean isInvocationToMethod(CtInvocation<?> invocation, 
			String invocationQualifiedName) {
		return getInvocationFullName(invocation).equals(invocationQualifiedName);
	}

	public static String getFieldQualifiedName(CtFieldReference<?> field) {
		String fieldName = field.getSimpleName();
		String classQualifiedName =  field.getFieldDeclaration().getTopLevelType().getQualifiedName();
		return classQualifiedName + "." + fieldName;
	}
}

package matcher.handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;

import gumtree.spoon.AstComparator;
import matcher.exceptions.ApplicationException;
import spoon.Launcher;
import spoon.compiler.SpoonResource;
import spoon.compiler.SpoonResourceHelper;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtArrayTypeReference;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.reference.CtTypeReference;

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
		long start = System.currentTimeMillis();
		baseLauncher.buildModel();
		long end = System.currentTimeMillis();
		System.out.println("Base launcher: " + (end-start));
		start = System.currentTimeMillis();
		variantLauncher1.buildModel();
		end = System.currentTimeMillis();
		System.out.println("Var 1 launcher: " + (end-start));
		start = System.currentTimeMillis();
		variantLauncher2.buildModel();
		end = System.currentTimeMillis();
		System.out.println("Var 2 launcher: " + (end-start));
		System.out.println("Files loaded in base: " + baseLauncher.getModel().getAllTypes().size());
		System.out.println("Files loaded in var 1: " + variantLauncher1.getModel().getAllTypes().size());
		System.out.println("Files loaded in var 2: " + variantLauncher2.getModel().getAllTypes().size());
		
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
		System.out.println("##### Spoon Loading #####");
		long start = System.currentTimeMillis();
		baseLauncher.addInputResource(PropertiesHandler.getInstance().getBaseSourceDirPath());
		
		variantLauncher1.addInputResource(PropertiesHandler.getInstance()
				.getFirstVariantSourceDirPath());
		
		variantLauncher2.addInputResource(PropertiesHandler.getInstance()
				.getSecondVariantSourceDirPath());
		long end = System.currentTimeMillis();
		System.out.println("Loading spoon: " + (end-start));
		start=System.currentTimeMillis();
		buildLaunchers();
		end=System.currentTimeMillis();
		System.out.println("Building launchers: " + (end-start));
		start = System.currentTimeMillis();
		baseTypes = getElements(baseLauncher, bases);
		variant1Types = getElements(variantLauncher1, variants1);
		variant2Types = getElements(variantLauncher2, variants2);
		end = System.currentTimeMillis();
		System.out.println("Getting relevant elements: " + (end-start));
		start = System.currentTimeMillis();
		addDeltaTypesToBase(variants1, variants2);
		end = System.currentTimeMillis();
		System.out.println("Adding from deltas: " + (end-start));
	}
	
	private void addDeltaTypesToBase(File[] variants1, File[] variants2) throws ApplicationException {
		Set<String> baseNames = baseTypes.stream()
				.map(t -> t.getQualifiedName())
				.collect(Collectors.toSet());
		for(CtType<?> type: variant1Types) {
			if(!baseNames.contains(type.getQualifiedName()) && 
					!isNewClass(type.getQualifiedName(), variants1) &&
					!type.getSimpleName().equals("Object")) {
				baseTypes.add(type);
			}
		}
		for(CtType<?> type: variant2Types) {
			if(!baseNames.contains(type.getQualifiedName()) && 
					!isNewClass(type.getQualifiedName(), variants2) &&
					!type.getSimpleName().equals("Object")) {
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
		
		Set<CtType<?>> modelTypes = Collections.newSetFromMap(new IdentityHashMap<>());
		Map<CtMethod<?>, Set<CtMethod<?>>> directDependents = new IdentityHashMap<>();
		Map<CtMethod<?>, Set<CtMethod<?>>> directDependencies = new IdentityHashMap<>();
		modelTypes.addAll(launcher.getModel().getAllTypes());
		
		calculateDependantsAndDependencies(directDependents, directDependencies, modelTypes);
		
		for(File f: files) {
			if(f != null) {
				SpoonResource resource = getSpoonResource(f);
				CtType<?> changedType = getCtType(resource);
				addElements(result, modelTypes, changedType.getQualifiedName(), 
						directDependents, directDependencies);
			}
		}
		return result;
	}
	
	private void addElements(Set<CtType<?>> result, 
			Set<CtType<?>> modelTypes, String changedClassName,
			Map<CtMethod<?>, Set<CtMethod<?>>> directDependents, 
			Map<CtMethod<?>, Set<CtMethod<?>>> directDependencies) {

		Queue<CtMethod<?>> methodsToVisit = new ArrayDeque<>();
		
		CtType<?> changedType = addChangedClass(modelTypes, changedClassName, result, methodsToVisit);
		
		methodsToVisit.addAll(changedType.getMethods());
		addElements(directDependents, result, methodsToVisit);
		
		methodsToVisit.addAll(changedType.getMethods());
		addElements(directDependencies, result, methodsToVisit);
	}
	
	private void addType(CtType<?> type, Set<CtType<?>> result) {
		if(!type.getSimpleName().equals("Object")) {
			result.add(type);
			type.getSuperInterfaces().forEach(i -> result.add(i.getTypeDeclaration()));
			addSuperClasses(type, result);
			addFields(type, result);
		}
	}
	
	private void addFields(CtType<?> type, Set<CtType<?>> result) {
		for(CtFieldReference<?> f: type.getDeclaredFields()) {
			if(f.getDeclaration() != null) {
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
			Set<CtType<?>> result, Queue<CtMethod<?>> methodsToVisit) {
		CtType<?> typeResult = null;
		Optional<CtType<?>> op = 
				types.stream()
					 .filter(type -> type.getQualifiedName().equals(changedClassName))
					 .findFirst();
		if(op.isPresent()) {
			typeResult = op.get();
			addType(typeResult, result);
		}
		return typeResult;
	}
	
	private void calculateDependantsAndDependencies(Map<CtMethod<?>, Set<CtMethod<?>>> directDependents,
			Map<CtMethod<?>, Set<CtMethod<?>>> directDependencies, Set<CtType<?>> modelTypes) {
		prepareMap(directDependents, modelTypes);
		prepareMap(directDependencies, modelTypes);
		
		for (CtType<?> modelType : modelTypes) {
			List<CtTypeReference<?>> modelRefs = modelType.filterChildren(
					(CtTypeReference<?> ref) -> modelTypes.contains(ref.getTypeDeclaration()))
					.list();
			
			for (CtTypeReference<?> modelRef : modelRefs) {
				CtMethod<?> invokingMethod = modelRef.getParent(CtMethod.class);
				CtInvocation<?> invocation = modelRef.getParent(CtInvocation.class);
				
				if(invokingMethod != null && invocation != null 
						&& invocationFromTheSystem(invocation)) {
					CtMethod<?> invokedMethod = SpoonHandler.getMethodFromInvocation(invocation);
					if(invokedMethod != null) {
						Set<CtMethod<?>> dependents = directDependents.get(invokedMethod);
						if(dependents != null)
							dependents.add(invokingMethod);
						
						Set<CtMethod<?>> dependencies = directDependencies.get(invokingMethod);
						if(dependencies != null)
							dependencies.add(invokedMethod);
					}
				}
		    }
		}
	}
	
	private void prepareMap(Map<CtMethod<?>, Set<CtMethod<?>>> map, Set<CtType<?>> modelTypes) {
		modelTypes.stream()
		.forEach(type -> {
			type.getMethods().stream().forEach(m -> {
				map.put(m, Collections.newSetFromMap(new IdentityHashMap<>()));
			});
		});
	}
	
	private void addElements(Map<CtMethod<?>, Set<CtMethod<?>>> map, Set<CtType<?>> result, 
			Queue<CtMethod<?>> methodsToVisit) {
		Set<CtMethod<?>> seenMethods = new HashSet<>();
		while(!methodsToVisit.isEmpty()) {
			CtMethod<?> method = methodsToVisit.poll();
			if(!seenMethods.contains(method)) {
				seenMethods.add(method);
				if(map.containsKey(method)) {
					for(CtMethod<?> m: map.get(method)) {
						methodsToVisit.add(m);
						addType(m.getTopLevelType(), result);
					}
				}
				
			}
		}
	}
	
	public static boolean invocationFromTheSystem(CtInvocation<?> invocation) {
		return  invocation.getExecutable() != null && 
				invocation.getExecutable().getDeclaringType() != null &&
				FileSystemHandler.getInstance().fromTheSystem(
						invocation.getExecutable()
								  .getDeclaringType().getSimpleName() + ".java");
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

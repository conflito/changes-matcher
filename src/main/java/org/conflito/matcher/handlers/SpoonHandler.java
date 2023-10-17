package org.conflito.matcher.handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;

import gumtree.spoon.AstComparator;
import org.conflito.matcher.exceptions.ApplicationException;
import spoon.Launcher;
import spoon.compiler.ModelBuildingException;
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
	
	private final static Logger logger = Logger.getLogger(SpoonHandler.class);
	
	private static SpoonHandler instance;
	
	private Launcher baseLauncher;
	private Launcher variantLauncher1;
	private Launcher variantLauncher2;
	
	private Set<CtType<?>> baseTypes;
	private Set<CtType<?>> variant1Types;
	private Set<CtType<?>> variant2Types;
	
	private Map<String, CtType<?>> baseMap;
	private Map<String, CtType<?>> variant1Map;
	private Map<String, CtType<?>> variant2Map;
	
	public static void createInstance() {
		instance  = new SpoonHandler();
	}
	
	public static SpoonHandler getInstance() {
		return instance;
	}
	
	private SpoonHandler() {
		this.baseLauncher = new Launcher();
		this.variantLauncher1 = new Launcher();
		this.variantLauncher2 = new Launcher();
		
		baseMap = new HashMap<>();
		variant1Map = new HashMap<>();
		variant2Map = new HashMap<>();
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
	
	public void buildLaunchers() throws ApplicationException {
		logger.info("Building launchers...");
		try {
			baseLauncher.buildModel();
		}
		catch(ModelBuildingException e) {
			throw new ApplicationException("Difference instances of the same class exist in the "
					+ "specified base source folder");
		}
		catch(Exception e) {
			throw new ApplicationException("Something went wrong building the class model "
					+ "for the specified base source folder");
		}
		
		try {
			variantLauncher1.buildModel();
		}
		catch(ModelBuildingException e) {
			throw new ApplicationException("Difference instances of the same class exist in the "
					+ "specified first variant source folder");
		}
		catch(Exception e) {
			throw new ApplicationException("Something went wrong building the class model "
					+ "for the specified first variant source folder");
		}
		
		try {
			variantLauncher2.buildModel();
		}
		catch(ModelBuildingException e) {
			throw new ApplicationException("Difference instances of the same class exist in the "
					+ "specified second variant source folder");
		}
		catch(Exception e) {
			throw new ApplicationException("Something went wrong building the class model "
					+ "for the specified second variant source folder");
		}
		
	}
	
	public Map<String, CtType<?>> baseTypes(){
		return baseMap;
	}
	
	public Map<String, CtType<?>> firstVariantTypes(){
		return variant1Map;
	}
	
	public Map<String, CtType<?>> secondVariantTypes(){
		return variant2Map;
	}
	
	public void loadLaunchers(File[] bases, File[] variants1, File[] variants2) 
					throws ApplicationException {
		logger.info("Loading launchers...");
		baseLauncher.addInputResource(PropertiesHandler.getInstance().getBaseSourceDirPath());
		
		variantLauncher1.addInputResource(PropertiesHandler.getInstance()
				.getFirstVariantSourceDirPath());
		
		variantLauncher2.addInputResource(PropertiesHandler.getInstance()
				.getSecondVariantSourceDirPath());

		buildLaunchers();

		logger.info("Calculating dependencies...");
		
		baseTypes = getElements(baseLauncher, bases);
		variant1Types = getElements(variantLauncher1, variants1);
		variant2Types = getElements(variantLauncher2, variants2);
		
		logger.info("Adding information from deltas to base...");
		
		addDeltaTypesToBase(variants1, variants2);
		
		fillMap(baseMap, baseTypes);
		fillMap(variant1Map, variant1Types);
		fillMap(variant2Map, variant2Types);
		
		baseTypes = null;
		variant1Types = null;
		variant2Types = null;
	}
	
	private void fillMap(Map<String, CtType<?>> map, Iterable<CtType<?>> elements) {
		for(CtType<?> type: elements) {
			map.put(type.getQualifiedName(), type);
		}
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
			Map<CtMethod<?>, Set<CtMethod<?>>> directDependencies) throws ApplicationException {

		Queue<CtMethod<?>> methodsToVisit = new ArrayDeque<>();
		
		CtType<?> changedType = addChangedClass(modelTypes, changedClassName, result, methodsToVisit);
		
		methodsToVisit.addAll(changedType.getMethods());
		addElements(directDependents, result, methodsToVisit);
		
		methodsToVisit.addAll(changedType.getMethods());
		addElements(directDependencies, result, methodsToVisit);
	}
	
	private void addType(CtType<?> type, Set<CtType<?>> result) {
		if(type != null && fromTheSystem(type.getReference())) {
			result.add(type);
			type.getSuperInterfaces().forEach(i -> {
				if(fromTheSystem(i))
					result.add(i.getTypeDeclaration());
			});
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
					if(fromTheSystem(fieldType))
						result.add(fieldType.getTypeDeclaration());
				}
			}
		}
	}
	
	private void addSuperClasses(CtType<?> type, Set<CtType<?>> result) {
		CtTypeReference<?> superClass = type.getSuperclass();
		if(fromTheSystem(superClass)) {
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
			Set<CtType<?>> result, Queue<CtMethod<?>> methodsToVisit) 
					throws ApplicationException {
		CtType<?> typeResult = null;
		Optional<CtType<?>> op = 
				types.stream()
					 .filter(type -> type.getQualifiedName().equals(changedClassName))
					 .findFirst();
		if(op.isPresent()) {
			typeResult = op.get();
			addType(typeResult, result);
		}
		else {
			throw new ApplicationException("Type in changed file not found");
		}
		return typeResult;
	}
	
	private void calculateDependantsAndDependencies(Map<CtMethod<?>, Set<CtMethod<?>>> directDependents,
			Map<CtMethod<?>, Set<CtMethod<?>>> directDependencies, Set<CtType<?>> modelTypes) {
		prepareMap(directDependents, modelTypes);
		prepareMap(directDependencies, modelTypes);
		
		for (CtType<?> modelType : modelTypes) {
			List<CtTypeReference<?>> modelRefs = modelType.filterChildren(
					(CtTypeReference<?> ref) -> fromTheSystem(ref))
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
	
	private static boolean fromTheSystem(CtTypeReference<?> ref) {
		try {
			return ref != null && ref.getTypeDeclaration() != null &&
				FileSystemHandler.getInstance()
					.fromTheSystem(ref.getSimpleName() + ".java");
		}
		catch(NoClassDefFoundError e) {
			return false;
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
//				invocation.getExecutable().getDeclaringType() != null &&
//				invocation.getExecutable().getDeclaringType().getTypeDeclaration() != null &&
//				FileSystemHandler.getInstance().fromTheSystem(
//						invocation.getExecutable()
//								  .getDeclaringType().getSimpleName() + ".java");
				fromTheSystem(invocation.getExecutable().getDeclaringType());
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
	
	public static boolean invocationOfObjectMethod(CtInvocation<?> invocation) {
		return getInvocationClassSimpleName(invocation).equals("Object");
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
	
	public static boolean validInvocation(CtInvocation<?> invocation) {
		return invocation.getExecutable() != null &&
				invocation.getExecutable().getDeclaringType() != null &&
				invocation.getExecutable().getDeclaringType()
											.getTypeDeclaration() != null &&
				!isThis(invocation) &&
				(!invocation.getExecutable().isConstructor() || 
						!isSuper(invocation));
	}
	
	private static boolean isSuper(CtInvocation<?> invocation) {
		return invocation.toString().startsWith("super(");
	}
	
	private static boolean isThis(CtInvocation<?> invocation) {
		return invocation.toString().startsWith("this(");
	}

	public static String getFieldQualifiedName(CtFieldReference<?> field) {
		String fieldName = field.getSimpleName();
		String classQualifiedName =  field.getFieldDeclaration().getTopLevelType().getQualifiedName();
		return classQualifiedName + "." + fieldName;
	}
}

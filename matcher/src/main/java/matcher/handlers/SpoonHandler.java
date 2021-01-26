package matcher.handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import gumtree.spoon.AstComparator;
import matcher.exceptions.ApplicationException;
import spoon.Launcher;
import spoon.compiler.SpoonResource;
import spoon.compiler.SpoonResourceHelper;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;

public class SpoonHandler {
		
	private int trackLimit;
	
	private Launcher baseLauncher;
	private Launcher variantLauncher1;
	private Launcher variantLauncher2;
	
	private Set<String> baseLoaded;
	private Set<String> variant1Loaded;
	private Set<String> variant2Loaded;
	
	public SpoonHandler(int trackLimit) {
		this.trackLimit = trackLimit;
		
		this.baseLauncher = new Launcher();
		this.variantLauncher1 = new Launcher();
		this.variantLauncher2 = new Launcher();
		
		this.baseLoaded = new HashSet<>();
		this.variant1Loaded = new HashSet<>();
		this.variant2Loaded = new HashSet<>();
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
	
	public Launcher loadLauncher(SpoonResource resource) 
			throws ApplicationException {
		Launcher launcher = new Launcher();
		Set<String> loaded = new HashSet<>();
		
		loadClass(resource, launcher, loaded, 0);

		return launcher;
	}
	
	public void buildLaunchers() {
		baseLauncher.buildModel();
		variantLauncher1.buildModel();
		variantLauncher2.buildModel();
	}
	
	public Iterable<CtType<?>> baseTypes(){
		return baseLauncher.getModel().getAllTypes();
	}
	
	public void loadLaunchers(File[] bases, File[] variants1, File[] variants2) 
					throws ApplicationException {
		for(int i = 0; i < bases.length; i++) {
			loadLaunchers(bases[i], variants1[i], variants2[i]);
		}
	}
	
	public void loadLaunchers(File base, File variant1, File variant2) 
					throws ApplicationException {		
		if(base != null) {
			SpoonResource baseResource = getSpoonResource(base);
			loadClass(baseResource, baseLauncher, baseLoaded, 0);
		}
		
		if(base != null && variant1 != null) {
			SpoonResource variantResource1 = getSpoonResource(variant1);
			CtType<?> var1 = getCtType(variantResource1);
			loadClass(variantResource1, variantLauncher1, variant1Loaded, 0);
			loadTypesInDelta(baseLauncher, baseLoaded, variant1Loaded, var1);
		}
		else if(variant1 != null) {
			SpoonResource variantResource1 = getSpoonResource(variant1);
			loadClass(variantResource1, variantLauncher1, variant1Loaded, 0);
		}
		
		if(base != null && variant2 != null) {
			SpoonResource variantResource2 = getSpoonResource(variant2);
			CtType<?> var2 = getCtType(variantResource2);
			loadClass(variantResource2, variantLauncher2, variant2Loaded, 0);
			loadTypesInDelta(baseLauncher, baseLoaded, variant2Loaded, var2);
		}
		else if(variant2 != null) {
			SpoonResource variantResource2 = getSpoonResource(variant2);
			loadClass(variantResource2, variantLauncher2, variant2Loaded, 0);
		}
		
		
	}
	
	private void loadTypesInDelta(Launcher baseLauncher, Set<String> baseLoaded, 
			Set<String> variantLoaded, CtType<?> varType) throws ApplicationException {
		for(String s: variantLoaded) {
			if(!varType.getSimpleName().equals(s) && !baseLoaded.contains(s)) {
				Optional<File> srcFile = FileSystemHandler.getInstance().getSrcFile(s + ".java");
				if(srcFile.isPresent()) {
					baseLauncher.addInputResource(getSpoonResource(srcFile.get()));
					baseLoaded.add(varType.getSimpleName());
				}
					
			}
		}
	}
	
	
	private void loadClass(SpoonResource resource, Launcher launcher, Set<String> loaded
			, int currentStep) throws ApplicationException {
		if(currentStep <= trackLimit) {
			CtType<?> changedType = getCtType(resource);
			if(!loaded.contains(changedType.getSimpleName())) {
				launcher.addInputResource(resource);
				loaded.add(changedType.getSimpleName());
			}
			if(changedType.isClass()) {
				CtClass<?> changedClass = getCtClass(resource);
				loadFields(changedClass, launcher, loaded, currentStep);
				loadInterfaces(changedClass, launcher, loaded);
				loadClassTree(changedClass, launcher, loaded, currentStep);
				loadInvokedClasses(changedClass, launcher, loaded, currentStep);
			}
		}
	}
	
	private void loadFields(CtClass<?> changedClass, Launcher launcher, Set<String> loaded
			, int currentStep) throws ApplicationException {
		for(CtFieldReference<?> f: changedClass.getDeclaredFields()) {
			CtTypeReference<?> fieldType = f.getDeclaration().getType();
			if(!fieldType.isPrimitive()) {
				Optional<File> srcFile;
				if(isComposedField(fieldType)) {
					srcFile = 
							FileSystemHandler.getInstance()
											 .getSrcFile(fieldType.getAccessType()
													 			  .getSimpleName() + ".java");
				}
				else {
					srcFile = FileSystemHandler.getInstance()
											   .getSrcFile(fieldType.getSimpleName() + ".java");
				}
				
				if(srcFile.isPresent()) {
					SpoonResource resource = getSpoonResource(srcFile.get());
					loadClass(resource, launcher, loaded, currentStep + 1);
				}
			}
		}
	}
	
	private boolean isComposedField(CtTypeReference<?> fieldType)  {
		try {
			fieldType.getAccessType();
			return true;
		}
		catch(Exception e) {}
		return false;
	}
	private void loadInterfaces(CtClass<?> changedClass, Launcher launcher, 
			Set<String> loaded) throws ApplicationException {
		for(CtTypeReference<?> i: changedClass.getSuperInterfaces()) {
			Optional<File> srcFile = 
					FileSystemHandler.getInstance().getSrcFile(i.getSimpleName() + ".java");
			if(srcFile.isPresent()) {
				SpoonResource resource = getSpoonResource(srcFile.get());
				CtType<?> type = getCtType(resource);
				if(!loaded.contains(type.getSimpleName())) {
					launcher.addInputResource(resource);
					loaded.add(type.getSimpleName());
				}
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void loadInvokedClasses(CtClass<?> changedClass, Launcher launcher, 
			Set<String> loaded, int currentStep) throws ApplicationException {
		List<CtInvocation<?>> invocations = 
				changedClass.getElements(new TypeFilter(CtInvocation.class));
		invocations = filterInvocations(changedClass.getQualifiedName(), invocations);
		for(CtInvocation<?> invocation: invocations) {
			if(invocation.getExecutable() != null 
					&& invocation.getExecutable().getDeclaringType() != null) {
				String simpleName = invocation.getExecutable().getDeclaringType().getSimpleName();
				Optional<File> srcFile = FileSystemHandler.getInstance().getSrcFile(simpleName + ".java");
				if(srcFile.isPresent()) {
					SpoonResource resource = getSpoonResource(srcFile.get());
					loadClass(resource, launcher, loaded, currentStep + 1);
				}
			}
			
		}
	}
	
	private List<CtInvocation<?>> filterInvocations(String classQualifiedName, 
				List<CtInvocation<?>> invocations){
		return invocations.stream()
						  .filter(i -> isNotSuperInvocation(i) &&
								  !fromTheSameClass(i, classQualifiedName))
						  .collect(Collectors.toList());
	}
	
	private boolean fromTheSameClass(CtInvocation<?> invocation
			, String classQualifiedName) {
		try {
			return getInvocationClassQualifiedName(invocation).equals(classQualifiedName);
		}
		catch(Exception e) {
			return false;
		}
	}
	
	private boolean isNotSuperInvocation(CtInvocation<?> invocation) {
		String invocationName = invocation.toString();
		return !invocationName.equals("super()") && !invocationName.startsWith("super.");
	}

	private void loadClassTree(CtClass<?> changedClass, Launcher launcher, 
			Set<String> loaded, int currentStep) throws ApplicationException {
		CtTypeReference<?> superClass = changedClass.getSuperclass();
		if(superClass != null) {
			Optional<File> superFile = 
					FileSystemHandler.getInstance().getSrcFile(superClass.getSimpleName() + ".java");
			if(superFile.isPresent()) {
				File sf = superFile.get();
				SpoonResource resource = getSpoonResource(sf);
				loadClass(resource, launcher, loaded, currentStep);
			}
		}
	}
	
	public CtType<?> getFullChangedCtType(Launcher launcher, String changedType){
		List<CtType<?>> l = launcher.getModel()
									.getAllTypes()
									.stream()
									.filter(c -> c.getQualifiedName().equals(changedType))
									.collect(Collectors.toList());
		return l.get(0);
	}
	
	private String getInvocationClassQualifiedName(CtInvocation<?> invocation) {
		return invocation.getExecutable().getDeclaringType().getQualifiedName();
	}
}

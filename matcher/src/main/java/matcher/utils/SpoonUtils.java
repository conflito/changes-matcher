package matcher.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import gumtree.spoon.AstComparator;
import matcher.exceptions.ApplicationException;
import matcher.handlers.FileSystemHandler;
import spoon.Launcher;
import spoon.compiler.SpoonResource;
import spoon.compiler.SpoonResourceHelper;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;

public class SpoonUtils {
	
	private SpoonUtils() {
		
	}
	
	public static SpoonResource getSpoonResource(File f) throws ApplicationException {
		SpoonResource resource;
		try {
			resource = SpoonResourceHelper.createResource(f);
		} catch (FileNotFoundException e) {
			throw new ApplicationException("Invalid specified file", e);
		}
		return resource;
	}
	
	public static CtType<?> getCtType(SpoonResource resource){
		return new AstComparator().getCtType(resource);
	}
	
	public static CtClass<?> getCtClass(SpoonResource resource){
		return (CtClass<?>) getCtType(resource);
	}
	
	public static Launcher loadLauncher(SpoonResource resource) 
			throws ApplicationException {
		Launcher launcher = new Launcher();
		Set<String> loaded = new HashSet<>();
		loadClass(resource, launcher, loaded);
		if(getCtType(resource).isClass()) {
			CtClass<?> changedClass = getCtClass(resource);
			loadInvokedClasses(changedClass, launcher, loaded);
		}
		return launcher;
	}
	
	private static void loadClass(SpoonResource resource, Launcher launcher, Set<String> loaded) 
			throws ApplicationException {
		CtType<?> changedType = getCtType(resource);
		if(!loaded.contains(changedType.getQualifiedName())) {
			launcher.addInputResource(resource);
			loaded.add(changedType.getQualifiedName());
		}
		if(changedType.isClass()) {
			CtClass<?> changedClass = SpoonUtils.getCtClass(resource);
			loadFields(changedClass, launcher, loaded);
			loadInterfaces(changedClass, launcher, loaded);
			loadClassTree(changedClass, launcher, loaded);
		}
	}
	
	private static void loadFields(CtClass<?> changedClass, Launcher launcher, Set<String> loaded) 
			throws ApplicationException {
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
					loadClass(resource, launcher, loaded);
				}
			}
		}
	}
	
	private static boolean isComposedField(CtTypeReference<?> fieldType)  {
		try {
			fieldType.getAccessType();
			return true;
		}
		catch(Exception e) {}
		return false;
	}
	private static void loadInterfaces(CtClass<?> changedClass, Launcher launcher, 
			Set<String> loaded) throws ApplicationException {
		for(CtTypeReference<?> i: changedClass.getSuperInterfaces()) {
			Optional<File> srcFile = 
					FileSystemHandler.getInstance().getSrcFile(i.getSimpleName() + ".java");
			if(srcFile.isPresent()) {
				SpoonResource resource = getSpoonResource(srcFile.get());
				CtType<?> type = getCtType(resource);
				if(!loaded.contains(type.getQualifiedName())) {
					launcher.addInputResource(resource);
					loaded.add(type.getQualifiedName());
				}
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void loadInvokedClasses(CtClass<?> changedClass, Launcher launcher, 
			Set<String> loaded) throws ApplicationException {
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
					loadClass(resource, launcher, loaded);
				}
			}
			
		}
	}
	
	private static List<CtInvocation<?>> filterInvocations(String classQualifiedName, 
				List<CtInvocation<?>> invocations){
		return invocations.stream()
						  .filter(i -> !i.toString().equals("super()") &&
								  !getInvocationClassQualifiedName(i).equals(classQualifiedName))
						  .collect(Collectors.toList());
	}

	private static void loadClassTree(CtClass<?> changedClass, Launcher launcher, 
			Set<String> loaded) throws ApplicationException {
		CtTypeReference<?> superClass = changedClass.getSuperclass();
		if(superClass != null) {
			Optional<File> superFile = 
					FileSystemHandler.getInstance().getSrcFile(superClass.getSimpleName() + ".java");
			if(superFile.isPresent()) {
				File sf = superFile.get();
				SpoonResource resource = SpoonUtils.getSpoonResource(sf);
				loadClass(resource, launcher, loaded);
			}
		}
	}
	
	public static CtType<?> getFullChangedCtType(Launcher launcher, String changedType){
		List<CtType<?>> l = launcher.buildModel()
									.getAllTypes()
									.stream()
									.filter(c -> c.getQualifiedName().equals(changedType))
									.collect(Collectors.toList());
		return l.get(0);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static String getInvocationClassQualifiedName(CtInvocation<?> invocation) {
		CtType<?> parent = (CtType<?>)invocation.getExecutable()
												  .getParent(new TypeFilter(CtType.class));
		return parent.getQualifiedName();
	}
}

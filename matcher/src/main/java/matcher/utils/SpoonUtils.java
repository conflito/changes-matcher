package matcher.utils;

import java.io.File;
import java.io.FileNotFoundException;
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
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;

public class SpoonUtils {
	
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
	
	public static void loadLauncher(CtClass<?> changedClass, Launcher launcher) 
			throws ApplicationException {
		loadClassTree(changedClass, launcher);
		loadInvokedClasses(changedClass, launcher);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void loadInvokedClasses(CtClass<?> changedClass, Launcher launcher) 
			throws ApplicationException {
		List<CtInvocation<?>> invocations = 
				changedClass.getElements(new TypeFilter(CtInvocation.class));
		invocations = invocations.stream()
				.filter(i -> !i.toString().equals("super()") &&
						!getInvocationClassQualifiedName(i)
						.equals(changedClass.getQualifiedName()))
				.collect(Collectors.toList());
		for(CtInvocation<?> invocation: invocations) {
			String simpleName = invocation.getExecutable().getDeclaringType().getSimpleName();
			Optional<File> srcFile = FileSystemHandler.getInstance().getSrcFile(simpleName + ".java");
			if(srcFile.isPresent()) {
				launcher.addInputResource(srcFile.get().getAbsolutePath());
			}
		}
	}

	public static void loadClassTree(CtClass<?> changedClass, Launcher launcher) 
			throws ApplicationException {
		CtTypeReference<?> superClass = changedClass.getSuperclass();
		if(superClass != null) {
			Optional<File> superFile = 
					FileSystemHandler.getInstance().getSrcFile(superClass.getSimpleName() + ".java");
			if(superFile.isPresent()) {
				File sf = superFile.get();
				SpoonResource resource = SpoonUtils.getSpoonResource(sf);
				CtClass<?> sc = SpoonUtils.getCtClass(resource);
				launcher.addInputResource(sf.getAbsolutePath());
				loadClassTree(sc, launcher);
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

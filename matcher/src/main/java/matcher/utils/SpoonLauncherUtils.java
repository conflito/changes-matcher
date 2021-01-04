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

public class SpoonLauncherUtils {

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
				launcher.addInputResource(sf.getAbsolutePath());
				try {
					SpoonResource resource = SpoonResourceHelper.createFile(sf);
					CtClass<?> sc = (CtClass<?>) new AstComparator().getCtType(resource);
					loadClassTree(sc, launcher);
				} catch (FileNotFoundException e) {
					throw new ApplicationException("Invalid file", e);
				}
			}
		}
	}
	
	public static CtType<?> getFullChangedCtType(Launcher launcher, CtType<?> changedType){
		List<CtType<?>> l = launcher.buildModel()
									.getAllTypes()
									.stream()
									.filter(c -> c.getQualifiedName()
											.equals(changedType.getQualifiedName()))
									.collect(Collectors.toList());
		return l.get(0);
	}
	
	private static String getInvocationClassQualifiedName(CtInvocation<?> invocation) {
		return invocation.getTarget().getType().getQualifiedName();
	}
}

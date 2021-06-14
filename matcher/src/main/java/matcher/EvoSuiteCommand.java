package matcher;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

import matcher.exceptions.ApplicationException;
import matcher.handlers.PropertiesHandler;

public class EvoSuiteCommand {

	private final static String EVO_LOCATION = ".." + File.separator + "evosuite" +
			File.separator + "master" + File.separator + "target" + File.separator +
			"evosuite-master-1.0.6.jar";
	
	private final static String CMD_LINE_TEMPLATE = 
			"-projectCP %s -class %s -criterion methodcall " + 
		    "-Dcover_methods=\"%s\" " +
		    "-Dtest_factory=multi_test -Dregressioncp=\"%s\" " +
		    "-Dsecond_regressioncp=\"%s\" -Dassertion_strategy=specific " +
		    "-Dreplace_calls=false -Ddistance_threshold=%f -Dsearch_budget=%d -Dtest_dir=%s";
	
	private final static String METHOD_DELIMITIER = ":";
	
	private final String mergeClassPath;
	private final String firstVariantClassPath;
	private final String secondVariantClassPath;
	
	private final String targetClass;
	private final String targetMethods;
	
	private final double distanceThreshold;
	private final int timeBudget;
	private final boolean useNotAll;
	private final String outputDir;
	
	public EvoSuiteCommand(String targetClass, List<String> targetMethods) 
			throws ApplicationException {
		super();
		this.mergeClassPath = 
				PropertiesHandler.getInstance().getMergeClasspathDirPath();
		this.firstVariantClassPath = 
				PropertiesHandler.getInstance().getFirstVariantClasspathDirPath();
		this.secondVariantClassPath = 
				PropertiesHandler.getInstance().getSecondVariantClasspathDirPath();
		this.targetClass = targetClass;
		this.targetMethods = String.join(METHOD_DELIMITIER, targetMethods);
		this.distanceThreshold =
				PropertiesHandler.getInstance().getDistanceThreshold();
		this.timeBudget = PropertiesHandler.getInstance().getTimeBudget();
		
		this.useNotAll = useNotAll(targetMethods);
		
		LocalDateTime timestamp = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		String formatDateTime = timestamp.format(formatter);
		this.outputDir = "unsettle" + File.separator + targetClass + File.separator + 
				formatDateTime;
	}
	
	private boolean useNotAll(List<String> targetMethods) {
		if(targetMethods.size() == 1)
			return true;
		
		Set<String> set = new HashSet<>();
		for(String method: targetMethods) {
			String[] components = method.split("\\.");
			components[components.length - 1] = "";
			String classString = String.join(".", components);
			set.add(classString);
		}
		return set.size() == 1;
	}
	
	public String getCommand() {
		String notAll = "";
		if(useNotAll)
			notAll = " -Dnot_all=true";
		
		return "java -jar " + EVO_LOCATION + " " + 
				String.format(CMD_LINE_TEMPLATE, mergeClassPath, targetClass, 
				targetMethods, firstVariantClassPath, secondVariantClassPath, 
				distanceThreshold, timeBudget, outputDir) + notAll;
	}
	
	public void run() throws ApplicationException {
		Process process;
		try {
			process = Runtime.getRuntime().exec(getCommand());
		}
		catch(Exception e) {
			throw new ApplicationException("Something went wrong starting "
					+ "the test generation");
		}
		EvoSuiteGobbler gobbler = new EvoSuiteGobbler(
				process.getInputStream(), System.out::println);
		Executors.newSingleThreadExecutor(new UnsettleThreadFactory()).submit(gobbler);
		try {
			process.waitFor();
		} catch (InterruptedException e) {
			throw new ApplicationException("Something went wrong generating tests");
		}
	}
	
}

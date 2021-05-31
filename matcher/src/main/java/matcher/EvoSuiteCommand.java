package matcher;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executors;

import matcher.exceptions.ApplicationException;
import matcher.handlers.PropertiesHandler;

public class EvoSuiteCommand {

	private final static String EVO_LOCATION = ".." + File.separator + "evosuite" +
			File.separator + "master" + File.separator + "target" + File.separator +
			"evosuite-master-1.0.6.jar";
	
	private final static String CMD_LINE_TEMPLATE = 
			"%s %s -class %s -criterion methodcall " + 
		    "-Dcover_methods=\"%s\" -Dinstrument_context=true " +
		    "-Dtest_factory=multi_test -Dregressioncp=\"%s\" " +
		    "-Dsecond_regressioncp=\"%s\" -Dassertion_strategy=specific " +
		    "-Dreplace_calls=false -Ddistance_threshold=%f -Dsearch_budget=%d";
	
	private final static String METHOD_DELIMITIER = ":";
	
	private final String mergeClassPath;
	private final String firstVariantClassPath;
	private final String secondVariantClassPath;
	
	private final String targetClass;
	private final String targetMethods;
	
	private final double distanceThreshold;
	private final int timeBudget;
	private final String targetFlag;
	
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
		
		if(PropertiesHandler.getInstance().isJarClasspath())
			this.targetFlag = "-target";
		else
			this.targetFlag = "-projectCP";
	}
	
	public String getCommand() {		
		return "java -jar " + EVO_LOCATION + " " + 
				String.format(CMD_LINE_TEMPLATE, targetFlag, mergeClassPath, targetClass, 
				targetMethods, firstVariantClassPath, secondVariantClassPath, 
				distanceThreshold, timeBudget);
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
		Executors.newSingleThreadExecutor().submit(gobbler);
		try {
			process.waitFor();
		} catch (InterruptedException e) {
			throw new ApplicationException("Something went wrong generating tests");
		}
	}
	
}

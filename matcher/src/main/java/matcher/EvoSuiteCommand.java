package matcher;

import java.util.List;

import matcher.exceptions.ApplicationException;
import matcher.handlers.PropertiesHandler;

public class EvoSuiteCommand {

	private final static String CMD_LINE_TEMPLATE = 
			"-projectCP %s -class %s -criterion methodcall " + 
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
	}
	
	public String getCommand() {
		return String.format(CMD_LINE_TEMPLATE, mergeClassPath, targetClass, 
				targetMethods, firstVariantClassPath, secondVariantClassPath, 
				distanceThreshold, timeBudget);
	}
	
	public String[] getCommandArgs() {
		return getCommand().split(" ");
	}
	
}

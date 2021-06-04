package matcher.handlers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import matcher.exceptions.ApplicationException;

public class PropertiesHandler {

	private static PropertiesHandler instance;
	
	//Source properties
	public static final String BASE_SRC_DIR_PROPERTY_KEY = "base.src.dir";
	public static final String VAR1_SRC_DIR_PROPERTY_KEY = "var1.src.dir";
	public static final String VAR2_SRC_DIR_PROPERTY_KEY = "var2.src.dir";
	
	//Classpath properties
	public static final String VAR1_CP_DIR_PROPERTY_KEY = "var1.cp.dir";
	public static final String VAR2_CP_DIR_PROPERTY_KEY = "var2.cp.dir";
	public static final String MERGE_CP_DIR_PROPERTY_KEY = "merge.cp.dir";
	
	//Configurable properties
	public static final String DISTANCE_THRESHOLD_KEY = "distance.threshold";
	public static final String TIME_BUDGET_KEY = "time.budget";
	public static final String JAR_CLASSPATH = "jar.classpath";
	public static final String MATCHING_BUDGET_KEY = "matching.budget";
	
	//Default values for configurable properties
	private static final double DEFAULT_DISTANCE_THRESHOLD = 0.05d;
	private static final int DEFAULT_TIME_BUDGET = 60;
	private static final int DEFAULT_MATCHING_BUDGET = 120;
	
	private Properties prop;
	
	private PropertiesHandler(String path) throws ApplicationException {
		try(InputStream input = new FileInputStream(path)){
			prop = new Properties();
			prop.load(input);
		} catch (FileNotFoundException e) {
			throw new ApplicationException("Properties file not found");
		} catch (IOException e) {
			throw new ApplicationException("Error reading properties file");
		}
	}
	
	public String getBaseSourceDirPath() throws ApplicationException {
		String result = prop.getProperty(BASE_SRC_DIR_PROPERTY_KEY);
		if(result == null)
			throw new ApplicationException("Properties missing base source dir");
		return result;
	}
	
	public String getFirstVariantSourceDirPath() throws ApplicationException {
		String result = prop.getProperty(VAR1_SRC_DIR_PROPERTY_KEY);
		if(result == null)
			throw new ApplicationException("Properties missing first variant source dir");
		return result;
	}
	
	public String getSecondVariantSourceDirPath() throws ApplicationException{
		String result = prop.getProperty(VAR2_SRC_DIR_PROPERTY_KEY);
		if(result == null)
			throw new ApplicationException("Properties missing second variant source dir");
		return result;
	}
	
	public String getFirstVariantClasspathDirPath() throws ApplicationException {
		String result = prop.getProperty(VAR1_CP_DIR_PROPERTY_KEY);
		if(result == null)
			throw new ApplicationException("Properties missing first variant classpath dir");
		return result;
	}
	
	public String getSecondVariantClasspathDirPath() throws ApplicationException {
		String result = prop.getProperty(VAR2_CP_DIR_PROPERTY_KEY);
		if(result == null)
			throw new ApplicationException("Properties missing second variant classpath dir");
		return result;
	}
	
	public String getMergeClasspathDirPath() throws ApplicationException {
		String result = prop.getProperty(MERGE_CP_DIR_PROPERTY_KEY);
		if(result == null)
			throw new ApplicationException("Properties missing merge classpath dir");
		return result;
	}
	
	public double getDistanceThreshold() {
		if(!hasDistanceThreshold())
			return DEFAULT_DISTANCE_THRESHOLD;
		double result = 0.0d;
		try {
			result = Double.parseDouble(prop.getProperty(DISTANCE_THRESHOLD_KEY));
		}
		catch(NumberFormatException e) {
			return DEFAULT_DISTANCE_THRESHOLD;
		}
		return result;
	}
	
	public int getTimeBudget() {
		if(!hasTimeBudget())
			return DEFAULT_TIME_BUDGET;
		int result = 0;
		try {
			result = Integer.parseInt(prop.getProperty(DISTANCE_THRESHOLD_KEY));
		}
		catch(NumberFormatException e) {
			return DEFAULT_TIME_BUDGET;
		}
		return result;
	}
	
	public boolean isJarClasspath() {
		if(!hasJarClasspath())
			return false;
		return Boolean.parseBoolean(prop.getProperty(JAR_CLASSPATH));
	}
	
	public int getMatchingBudget() {
		if(!hasMatchingBudget())
			return DEFAULT_MATCHING_BUDGET;
		int result = 0;
		try {
			result = Integer.parseInt(prop.getProperty(MATCHING_BUDGET_KEY));
		}
		catch(NumberFormatException e) {
			return DEFAULT_MATCHING_BUDGET;
		}
		return result;
	}
	
	private boolean hasMatchingBudget() {
		return prop.containsKey(MATCHING_BUDGET_KEY);
	}
	
	private boolean hasDistanceThreshold() {
		return prop.containsKey(DISTANCE_THRESHOLD_KEY);
	}
	
	private boolean hasTimeBudget() {
		return prop.containsKey(TIME_BUDGET_KEY);
	}
	
	private boolean hasJarClasspath() {
		return prop.containsKey(JAR_CLASSPATH);
	}
	
	public static void createInstance(String path) throws ApplicationException {
		instance = new PropertiesHandler(path);
	}
	
	public static PropertiesHandler getInstance() throws ApplicationException{
		if(instance == null) 
			throw new ApplicationException("PropertiesHandler hasn't been built yet");
		return instance;
	}
	
}

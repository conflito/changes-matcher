package org.conflito.matcher.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.conflito.matcher.exceptions.ApplicationException;

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
  public static final String MATCHING_BUDGET_KEY = "matching.budget";
  public static final String THREAD_NUMBER_KEY = "thread.number";

  //Default values for configurable properties
  private static final int DEFAULT_MATCHING_BUDGET = 120;
  private static final int DEFAULT_THREADS = 2;

  private final Properties prop;

  private PropertiesHandler(String path) throws ApplicationException {
    try (InputStream input = new FileInputStream(path)) {
      prop = new Properties();
      prop.load(input);
    } catch (FileNotFoundException e) {
      throw new ApplicationException("Configuration file not found");
    } catch (IOException e) {
      throw new ApplicationException("Error reading configuration file");
    }
  }

  public String getBaseSourceDirPath() throws ApplicationException {
    String result = prop.getProperty(BASE_SRC_DIR_PROPERTY_KEY);
		if (result == null) {
			throw new ApplicationException("Configuration file missing base source dir");
		}
    return result;
  }

  public String getFirstVariantSourceDirPath() throws ApplicationException {
    String result = prop.getProperty(VAR1_SRC_DIR_PROPERTY_KEY);
		if (result == null) {
			throw new ApplicationException("Configuration file missing first variant source dir");
		}
    return result;
  }

  public String getSecondVariantSourceDirPath() throws ApplicationException {
    String result = prop.getProperty(VAR2_SRC_DIR_PROPERTY_KEY);
		if (result == null) {
			throw new ApplicationException("Configuration file missing second variant source dir");
		}
    return result;
  }

  public String getFirstVariantClasspathDirPath() throws ApplicationException {
    String result = prop.getProperty(VAR1_CP_DIR_PROPERTY_KEY);
		if (result == null) {
			throw new ApplicationException("Configuration file missing first variant classpath dir");
		}
    return result;
  }

  public String getSecondVariantClasspathDirPath() throws ApplicationException {
    String result = prop.getProperty(VAR2_CP_DIR_PROPERTY_KEY);
		if (result == null) {
			throw new ApplicationException("Configuration file missing second variant classpath dir");
		}
    return result;
  }

  public String getMergeClasspathDirPath() throws ApplicationException {
    String result = prop.getProperty(MERGE_CP_DIR_PROPERTY_KEY);
		if (result == null) {
			throw new ApplicationException("Properties missing merge classpath dir");
		}
    return result;
  }

  public int getMatchingBudget() {
		if (!hasMatchingBudget()) {
			return DEFAULT_MATCHING_BUDGET;
		}
    int result = 0;
    try {
      result = Integer.parseInt(prop.getProperty(MATCHING_BUDGET_KEY));
    } catch (NumberFormatException e) {
      return DEFAULT_MATCHING_BUDGET;
    }
		if (result == -1) {
			return Integer.MAX_VALUE;
		} else if (result <= 0) {
			return DEFAULT_MATCHING_BUDGET;
		}
    return result;
  }

  public int getNumberThreads() {
		if (!hasThreadNumber()) {
			return DEFAULT_THREADS;
		}
    int result = 0;
    try {
      result = Integer.parseInt(prop.getProperty(THREAD_NUMBER_KEY));
    } catch (NumberFormatException e) {
      return DEFAULT_THREADS;
    }
		if (result == -1) {
			return Runtime.getRuntime().availableProcessors();
		} else if (result <= 0) {
			return DEFAULT_THREADS;
		}
    return result;
  }

  private boolean hasThreadNumber() {
    return prop.containsKey(THREAD_NUMBER_KEY);
  }

  private boolean hasMatchingBudget() {
    return prop.containsKey(MATCHING_BUDGET_KEY);
  }

  public static void createInstance(String path) throws ApplicationException {
    instance = new PropertiesHandler(path);
  }

  public static PropertiesHandler getInstance() throws ApplicationException {
		if (instance == null) {
			throw new ApplicationException("PropertiesHandler hasn't been built yet");
		}
    return instance;
  }

}

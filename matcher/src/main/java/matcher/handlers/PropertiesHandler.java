package matcher.handlers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import matcher.exceptions.ApplicationException;

public class PropertiesHandler {

	private static PropertiesHandler instance;
	
	private static final String BASE_SRC_DIR_PROPERTY_KEY = "base.src.dir";
	private static final String VAR1_SRC_DIR_PROPERTY_KEY = "var1.src.dir";
	private static final String VAR2_SRC_DIR_PROPERTY_KEY = "var2.src.dir";
	
	private Properties prop;
	
	private PropertiesHandler(String path) throws ApplicationException {
		try(InputStream input = new FileInputStream(path)){
			prop = new Properties();
			prop.load(input);
		} catch (FileNotFoundException e) {
			throw new ApplicationException("Invalid properties file", e);
		} catch (IOException e) {
			throw new ApplicationException("Error reading properties file", e);
		}
	}
	
	public String getBaseSourceDirPath() {
		return prop.getProperty(BASE_SRC_DIR_PROPERTY_KEY);
	}
	
	public String getFirstVariantSourceDirPath() {
		return prop.getProperty(VAR1_SRC_DIR_PROPERTY_KEY);
	}
	
	public String getSecondVariantSourceDirPath() {
		return prop.getProperty(VAR2_SRC_DIR_PROPERTY_KEY);
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

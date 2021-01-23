package matcher.handlers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import matcher.exceptions.ApplicationException;

public class PropertiesHandler {

	private static PropertiesHandler instance;
	
	private static final String SRC_DIR_PROPERTY_KEY = "src.dir";
	private static final String TRACK_LIMIT_KEY = "track.limit";
	
	private static final int DEFAULT_TRACK_LIMIT = 5;
	
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
	
	public String getSourceDirPath() {
		return prop.getProperty(SRC_DIR_PROPERTY_KEY);
	}
	
	public int getTrackLimit() {
		try {
			return Integer.parseInt(prop.getProperty(TRACK_LIMIT_KEY));
		}
		catch(Exception e) {
			return DEFAULT_TRACK_LIMIT;
		}
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

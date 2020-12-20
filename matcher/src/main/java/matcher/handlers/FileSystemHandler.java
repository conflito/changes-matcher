package matcher.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import matcher.exceptions.ApplicationException;

public class FileSystemHandler {

	private static FileSystemHandler instance;
	
	private static final String PROPERTIES_PATH = "src/main/resources/config.properties"; 
	
	private Properties prop;
	
	private FileSystemHandler() throws ApplicationException {
		try(InputStream input = new FileInputStream(PROPERTIES_PATH)){
			prop = new Properties();
			prop.load(input);
		} catch (FileNotFoundException e) {
			throw new ApplicationException("Invalid properties file", e);
		} catch (IOException e) {
			throw new ApplicationException("Error reading properties file", e);
		}
	}
	
	public boolean fromTheSystem(String className) {
		String[] dirs = prop.getProperty("target.dirs").split(",");
		for(String dir: dirs) {
			if(existsInDir(className, dir))
				return true;
		}
		return false;
	}
	
	private boolean existsInDir(String className, String dirName) {
		File dir = new File(dirName);
		if(dir.isDirectory()) {
			for(File f: dir.listFiles()) {
				if(f.getName().equals(className))
					return true;
			}
		}
		return false;
	}
	
	public static FileSystemHandler getInstance() throws ApplicationException {
		if(instance == null)
			instance = new FileSystemHandler();
		return instance;
	}
}

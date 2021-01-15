package matcher.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import matcher.exceptions.ApplicationException;

public class FileSystemHandler {

	private static FileSystemHandler instance;
	
	private static final String PROPERTIES_PATH = "src" + File.separator +  "main" + 
			 File.separator + "resources" + File.separator + "config.properties"; 
	private static final String SRC_DIR_PROPERTY = "src.dir";
	
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
	
	public void setConfigPath(String path) throws ApplicationException {
		try(InputStream input = new FileInputStream(path)){
			prop = new Properties();
			prop.load(input);
		} catch (FileNotFoundException e) {
			throw new ApplicationException("Invalid properties file", e);
		} catch (IOException e) {
			throw new ApplicationException("Error reading properties file", e);
		}
	}
	
	public boolean fromTheSystem(String fileName) {
		return getSrcFile(fileName).isPresent();
	}
	
	public Optional<File> getSrcFile(String fileName){
		String dirName = prop.getProperty(SRC_DIR_PROPERTY);
		if(dirName == null)
			return Optional.empty();
		File dir = new File(dirName);
		return Optional.ofNullable(searchSourceFile(dir, fileName));
	}
	
	private File searchSourceFile(File dir, String fileName) {
		List<File> dirs = new ArrayList<>();
		for(File f: dir.listFiles()) {
			if(f.getName().equals(fileName)) {
				return f;
			}
			else if(f.isDirectory())
				dirs.add(f);
		}
		for(File f: dirs) {
			File result = searchSourceFile(f, fileName);
			if(result != null)
				return result;
		}
		return null;
	}
	
	public static FileSystemHandler getInstance() throws ApplicationException {
		if(instance == null)
			instance = new FileSystemHandler();
		return instance;
	}
}

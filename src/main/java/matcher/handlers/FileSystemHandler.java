package matcher.handlers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import matcher.exceptions.ApplicationException;

public class FileSystemHandler {

	private static FileSystemHandler instance;
	
	private String baseSourceDir;
	private String var1SourceDir;
	private String var2SourceDir;
	
	private Set<String> filenames;
	
	private FileSystemHandler() throws ApplicationException{
		baseSourceDir = PropertiesHandler.getInstance().getBaseSourceDirPath();
		var1SourceDir = PropertiesHandler.getInstance().getFirstVariantSourceDirPath();
		var2SourceDir = PropertiesHandler.getInstance().getSecondVariantSourceDirPath();
		
		filenames = new HashSet<>();
		searchSourceFiles(baseSourceDir);
		searchSourceFiles(var1SourceDir);
		searchSourceFiles(var2SourceDir);
	}
	
	public boolean fromTheSystem(String fileName) {
		return filenames.contains(fileName);
	}
	
	private void searchSourceFiles(String dirName) throws ApplicationException{
		if(dirName != null) {
			File dir = new File(dirName);
			if(dir.exists())
				searchSourceFiles(dir);
			else {
				throw new ApplicationException("Unknown source code directory: " + dirName);
			}
		}
		else
			throw new ApplicationException("Unknown source code directory: " + dirName);
	}
	
	private void searchSourceFiles(File dir) {
		List<File> dirs = new ArrayList<>();
		for(File f: dir.listFiles()) {
			if(f.isDirectory())
				dirs.add(f);
			else if(f.getName().endsWith(".java"))
				filenames.add(f.getName());
		}
		for(File f: dirs) {
			searchSourceFiles(f);
		}
	}
	
	public static void createInstance() throws ApplicationException {
		instance = new FileSystemHandler();
	}
	
	public static FileSystemHandler getInstance() {
		return instance;
	}
}


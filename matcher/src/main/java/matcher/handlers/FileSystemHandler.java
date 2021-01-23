package matcher.handlers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import matcher.exceptions.ApplicationException;

public class FileSystemHandler {

	private static FileSystemHandler instance;
	
	private String srcDir;
	
	private FileSystemHandler(String srcDir) throws ApplicationException {
		this.srcDir = srcDir;
	}
	
	public boolean fromTheSystem(String fileName) {
		return getSrcFile(fileName).isPresent();
	}
	
	public Optional<File> getSrcFile(String fileName){
		String dirName = srcDir;
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
	
	public static void createInstance(String path) throws ApplicationException {
		instance = new FileSystemHandler(path);
	}
	
	public static FileSystemHandler getInstance() throws ApplicationException {
		if(instance == null)
			throw new ApplicationException("FileSystemHandler hasn't been built yet");
		return instance;
	}
}

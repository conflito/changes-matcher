package matcher;

import java.io.File;
import java.util.List;

import matcher.entities.ChangeInstance;
import matcher.exceptions.ApplicationException;
import matcher.handlers.ChangeInstanceHandler;
import matcher.handlers.FileSystemHandler;
import matcher.handlers.MatchingHandler;
import matcher.patterns.ConflictPattern;
import matcher.utils.Pair;

public class Matcher {

	private ChangeInstanceHandler cih;
	private MatchingHandler mh;
	
	
	public Matcher() {
		cih = new ChangeInstanceHandler();
		mh = new MatchingHandler();
	}
	
	public Matcher(String configFilePath) throws ApplicationException {
		cih = new ChangeInstanceHandler();
		mh = new MatchingHandler();
		FileSystemHandler.getInstance().setConfigPath(configFilePath);
	}
	
	public List<List<Pair<Integer, String>>> matchingAssignments(String basePath, 
			String firstVariantPath, String secondVariantPath, ConflictPattern cp) 
					throws ApplicationException{
		File base = new File(basePath);
		File firstVar = new File(firstVariantPath);
		File secondVar = new File(secondVariantPath);
		ChangeInstance ci = cih.getChangeInstance(base, firstVar, secondVar, cp);
		return mh.matchingAssignments(ci, cp);
	}
	
	public List<List<Pair<Integer, String>>> matchingAssignments(String firstBasePath, 
			String firstVariantPath, String secondBasePath, String secondVariantPath, 
			ConflictPattern cp) throws ApplicationException{
		File base1 = new File(firstBasePath);
		File base2 = new File(secondBasePath);
		File var1 = new File(firstVariantPath);
		File var2 = new File(secondVariantPath);
		ChangeInstance ci = cih.getChangeInstance(base1, base2, var1, var2, cp);
		return mh.matchingAssignments(ci, cp);
	}
}

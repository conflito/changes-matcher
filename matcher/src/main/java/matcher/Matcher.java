package matcher;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import matcher.entities.ChangeInstance;
import matcher.exceptions.ApplicationException;
import matcher.handlers.ChangeInstanceHandler;
import matcher.handlers.FileSystemHandler;
import matcher.handlers.MatchingHandler;
import matcher.handlers.PropertiesHandler;
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
		PropertiesHandler.createInstance(configFilePath);
		FileSystemHandler.createInstance(PropertiesHandler.getInstance().getConfigFilePath());
	}
	
	public List<List<Pair<Integer, String>>> matchingAssignments(String[] bases,
			String[] variants1, String[] variants2, ConflictPattern cp)
			throws ApplicationException{
		if(bases == null || variants1 == null || variants2 == null)
			return new ArrayList<>();
		if(!sameLenght(bases, variants1, variants2))
			return new ArrayList<>();
		File[] basesFile = new File[bases.length];
		File[] variants1File = new File[variants1.length];
		File[] variants2File = new File[variants2.length];
		for(int i = 0; i < bases.length; i++) {
			String base = bases[i];
			String variant1 = variants1[i];
			String variant2 = variants2[i];
			if(base != null)
				basesFile[i] = new File(base);
			if(variant1 != null)
				variants1File[i] = new File(variant1);
			if(variant2 != null)
				variants2File[i] = new File(variant2);
		}
		ChangeInstance ci = cih.getChangeInstance(basesFile, variants1File, variants2File, cp);
		return mh.matchingAssignments(ci, cp);
	}

	private boolean sameLenght(String[] bases, String[] variants1, String[] variants2) {
		return bases.length == variants1.length && bases.length == variants2.length;
	}
}

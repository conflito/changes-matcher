package matcher;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import matcher.entities.ChangeInstance;
import matcher.exceptions.ApplicationException;
import matcher.handlers.ChangeInstanceHandler;
import matcher.handlers.MatchingHandler;
import matcher.handlers.PropertiesHandler;
import matcher.patterns.ConflictPattern;
import matcher.utils.Pair;

public class Matcher {

	private ChangeInstanceHandler cih;
	private MatchingHandler mh;
	
	public Matcher(String configFilePath) throws ApplicationException {
		PropertiesHandler.createInstance(configFilePath);
		
		cih = new ChangeInstanceHandler();
		mh = new MatchingHandler();
	}
	
	public List<List<Pair<Integer, String>>> matchingAssignments(String[] bases,
			String[] variants1, String[] variants2)
			throws ApplicationException{
		if(bases == null || variants1 == null || variants2 == null)
			return new ArrayList<>();
		if(!sameLenght(bases, variants1, variants2))
			return new ArrayList<>();
		File[] basesFile = fromStringArray(bases);
		File[] variants1File = fromStringArray(variants1);
		File[] variants2File = fromStringArray(variants2);

		List<List<Pair<Integer, String>>> result = new ArrayList<>();
		
		List<Pair<ChangeInstance, ConflictPattern>> pairs =
				cih.getChangeInstances(basesFile, variants1File, variants2File);
		System.out.println("##### Matching #####");
		for(Pair<ChangeInstance, ConflictPattern> p: pairs) {
			long start=System.currentTimeMillis();
			result.addAll(mh.matchingAssignments(p.getFirst(), p.getSecond()));
			long end = System.currentTimeMillis();
			System.out.println("Matching: " + (end-start));
		}
		
		return result;
	}
	
	public List<List<Pair<Integer, String>>> matchingAssignments(String[] bases,
			String[] variants1, String[] variants2, ConflictPattern cp)
			throws ApplicationException{
		if(bases == null || variants1 == null || variants2 == null)
			return new ArrayList<>();
		if(!sameLenght(bases, variants1, variants2))
			return new ArrayList<>();
		
		File[] basesFile = fromStringArray(bases);
		File[] variants1File = fromStringArray(variants1);
		File[] variants2File = fromStringArray(variants2);

		ChangeInstance ci = cih.getChangeInstance(basesFile, variants1File, variants2File, cp);
		System.out.println("##### Matching #####");
		long start=System.currentTimeMillis();
		List<List<Pair<Integer, String>>> result = mh.matchingAssignments(ci, cp);
		long end = System.currentTimeMillis();
		System.out.println("Matching: " + (end-start));

		return result;//mh.matchingAssignments(ci, cp);
	}

	private boolean sameLenght(String[] bases, String[] variants1, String[] variants2) {
		return bases.length == variants1.length && bases.length == variants2.length;
	}
	
	private File[] fromStringArray(String[] filePaths) {
		File[] result = new File[filePaths.length];
		for(int i = 0; i < filePaths.length; i++) {
			String path = filePaths[i];
			if(path != null)
				result[i] = new File(path);
		}
		return result;
	}
}

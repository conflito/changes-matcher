package matcher;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;


import matcher.catalogs.ConflictPatternCatalog;
import matcher.exceptions.ApplicationException;
import matcher.handlers.FileSystemHandler;
import matcher.handlers.InstancesCache;
import matcher.handlers.PropertiesHandler;
import matcher.handlers.SpoonHandler;
import matcher.patterns.ConflictPattern;
import matcher.utils.Pair;

public class Matcher {
	
	private ConflictPatternCatalog conflictsCatalog;
	
	private List<Pair<String, List<String>>> testingGoals;
	
	public Matcher(String configFilePath) throws ApplicationException {
		PropertiesHandler.createInstance(configFilePath);
		FileSystemHandler.createInstance();
		SpoonHandler.createInstance();
		InstancesCache.createInstance();
		
		conflictsCatalog = new ConflictPatternCatalog();
		testingGoals = new ArrayList<>();
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
		
		SpoonHandler.getInstance().loadLaunchers(basesFile, variants1File, 
				variants2File);
		
		List<List<Pair<Integer, String>>> result = new ArrayList<>();
		
		ExecutorService es = Executors.newCachedThreadPool();
		
		List<MatchingRunnable> runnables = new ArrayList<>();
		List<Future<List<List<Pair<Integer, String>>>>> futures = new ArrayList<>();
		
		Semaphore sem = new Semaphore(1);
		
		for(ConflictPattern cp: conflictsCatalog.getPatterns()) {
			MatchingRunnable mt = new MatchingRunnable(basesFile, variants1File, variants2File);
			mt.setConflictPattern(cp);
			mt.setSem(sem);
			runnables.add(mt);
			futures.add(es.submit(mt));
		}
		
		Set<Integer> completedFutures = new HashSet<>();
		
		while(completedFutures.size() != futures.size()) {
			for (int i = 0; i < futures.size(); i++) {
				Future<List<List<Pair<Integer, String>>>> future = futures.get(i);
				MatchingRunnable mt = runnables.get(i);
				if(!completedFutures.contains(i) && future.isDone()) {
					completedFutures.add(i);
					try {
						result.addAll(future.get());
						testingGoals.addAll(mt.getTestingGoals());
					} catch (InterruptedException e) {
						throw new ApplicationException("Interrupted execution");
					} catch (ExecutionException e) {
						throw new ApplicationException(e.getMessage());
					}
				}				
			}			
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
		
		SpoonHandler.getInstance().loadLaunchers(basesFile, variants1File, 
				variants2File);
		
		ExecutorService es = Executors.newCachedThreadPool();
		
		Semaphore sem = new Semaphore(1);
		
		MatchingRunnable mt = new MatchingRunnable(basesFile, variants1File, variants2File);
		mt.setConflictPattern(cp);
		mt.setSem(sem);
		
		Future<List<List<Pair<Integer, String>>>> future = es.submit(mt);
		
		try {
			List<List<Pair<Integer, String>>> result = future.get();
			testingGoals.addAll(mt.getTestingGoals());
			return result;
		} catch (InterruptedException e) {
			throw new ApplicationException("Interrupted execution");
		} catch (ExecutionException e) {
			throw new ApplicationException(e.getMessage());
		}
	}
	
	public List<Pair<String, List<String>>> getTestingGoals(){
		return this.testingGoals;
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

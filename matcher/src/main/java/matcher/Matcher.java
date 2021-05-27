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
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import matcher.catalogs.ConflictPatternCatalog;
import matcher.exceptions.ApplicationException;
import matcher.handlers.FileSystemHandler;
import matcher.handlers.InstancesCache;
import matcher.handlers.PropertiesHandler;
import matcher.handlers.SpoonHandler;
import matcher.patterns.ConflictPattern;
import matcher.utils.Pair;

public class Matcher {
	
	private final static Logger logger = Logger.getLogger(Matcher.class);
	
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
	
	public void match(String[] bases, String[] variants1, String[] variants2)
			throws ApplicationException{
		if(bases == null || variants1 == null || variants2 == null)
			throw new ApplicationException("Invalid arguments: null");
		if(!sameLength(bases, variants1, variants2))
			throw new ApplicationException("Invalid arguments: different array sizes");
		
		File[] basesFile = fromStringArray(bases);
		File[] variants1File = fromStringArray(variants1);
		File[] variants2File = fromStringArray(variants2);
		
		SpoonHandler.getInstance().loadLaunchers(basesFile, variants1File, 
				variants2File);
		
		ExecutorService es = Executors.newCachedThreadPool();
		Semaphore sem = new Semaphore(1);
		
		for(ConflictPattern cp: conflictsCatalog.getPatterns()) {
			UnsettleRunnable ur = new UnsettleRunnable(basesFile, variants1File, 
					variants2File, cp, sem);
			es.submit(ur);
		}
		es.shutdown();
		try {
			es.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			throw new ApplicationException("Something went wrong generating tests");
		}
	}
	
	public void match(String[] bases, String[] variants1, String[] variants2,
			String conflictName) throws ApplicationException{
		
		if(bases == null || variants1 == null || variants2 == null 
				|| conflictName == null)
			throw new ApplicationException("Invalid arguments: null");
		
		if(!sameLength(bases, variants1, variants2))
			throw new ApplicationException("Invalid arguments: different array sizes");
		
		if(!conflictsCatalog.hasPattern(conflictName))
			throw new ApplicationException("Unknown pattern name!");
		
		File[] basesFile = fromStringArray(bases);
		File[] variants1File = fromStringArray(variants1);
		File[] variants2File = fromStringArray(variants2);
		
		SpoonHandler.getInstance().loadLaunchers(basesFile, variants1File, 
				variants2File);
		
		ExecutorService es = Executors.newCachedThreadPool();
		Semaphore sem = new Semaphore(1);
		ConflictPattern cp = conflictsCatalog.getPattern(conflictName);
		
		UnsettleRunnable ur = new UnsettleRunnable(basesFile, variants1File, 
				variants2File, cp, sem);
		es.submit(ur);
		es.shutdown();
		try {
			es.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			throw new ApplicationException("Something went wrong generating tests");
		}
	}
	
	public List<List<Pair<Integer, String>>> matchingAssignments(String[] bases,
			String[] variants1, String[] variants2)
			throws ApplicationException{
		if(bases == null || variants1 == null || variants2 == null)
			return new ArrayList<>();
		if(!sameLength(bases, variants1, variants2))
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
			MatchingRunnable mt = new MatchingRunnable(basesFile, variants1File, 
					variants2File, cp, sem);

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
						logger.error("Interruption error. Shutting down...");
						es.shutdownNow();
						throw new ApplicationException("Interrupted execution");
					} catch (ExecutionException e) {
						logger.error("Error in execution. Shutting down...");
						es.shutdownNow();
						throw new ApplicationException("Error in execution");
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
		if(!sameLength(bases, variants1, variants2))
			return new ArrayList<>();
		
		File[] basesFile = fromStringArray(bases);
		File[] variants1File = fromStringArray(variants1);
		File[] variants2File = fromStringArray(variants2);
		
		SpoonHandler.getInstance().loadLaunchers(basesFile, variants1File, 
				variants2File);
		
		ExecutorService es = Executors.newCachedThreadPool();
		
		Semaphore sem = new Semaphore(1);
		
		MatchingRunnable mt = new MatchingRunnable(basesFile, variants1File, 
				variants2File, cp, sem);
		
		Future<List<List<Pair<Integer, String>>>> future = es.submit(mt);
		
		try {
			List<List<Pair<Integer, String>>> result = future.get();
			testingGoals.addAll(mt.getTestingGoals());
			return result;
		} catch (InterruptedException e) {
			throw new ApplicationException("Interrupted execution");
		} catch (ExecutionException e) {
			throw new ApplicationException("Something went wrong");
		}
	}
	
	public List<Pair<String, List<String>>> getTestingGoals(){
		return this.testingGoals;
	}

	private boolean sameLength(String[] bases, String[] variants1, String[] variants2) {
		return bases.length == variants1.length && bases.length == variants2.length;
	}
	
	private File[] fromStringArray(String[] filePaths) throws ApplicationException {
		File[] result = new File[filePaths.length];
		for(int i = 0; i < filePaths.length; i++) {
			String path = filePaths[i];
			if(path != null) {
				File f = new File(path);
				if(!f.exists())
					throw new ApplicationException("File " + f.getAbsolutePath() +
							" does not exist.");
				result[i] = f;
				
			}
		}
		return result;
	}
}

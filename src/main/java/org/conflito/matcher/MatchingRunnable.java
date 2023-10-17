package org.conflito.matcher;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import org.conflito.matcher.entities.ChangeInstance;
import org.conflito.matcher.exceptions.ApplicationException;
import org.conflito.matcher.handlers.ChangeInstanceHandler;
import org.conflito.matcher.handlers.MatchingHandler;
import org.conflito.matcher.handlers.PropertiesHandler;
import org.conflito.matcher.patterns.ConflictPattern;
import org.conflito.matcher.utils.Pair;

public class MatchingRunnable implements Callable<List<List<Pair<Integer, String>>>>{

	private final static Logger logger = Logger.getLogger(MatchingRunnable.class);

	private Semaphore sem;
	
	private ChangeInstanceHandler cih;
	private MatchingHandler mh;

	private File[] basesFile;
	private File[] variants1File;
	private File[] variants2File;

	private ConflictPattern cp;

	public MatchingRunnable(File[] basesFile, File[] variants1File, 
			File[] variants2File, ConflictPattern cp, Semaphore sem) {
		super();
		this.basesFile = basesFile;
		this.variants1File = variants1File;
		this.variants2File = variants2File;
		this.cp = cp;
		this.sem = sem;

		cih = new ChangeInstanceHandler();
		mh = new MatchingHandler();
	}

	@Override
	public List<List<Pair<Integer, String>>> call() throws Exception {
		if(cp == null) 
			throw new ApplicationException("Missing conflict pattern.");
		
		if(sem == null)
			throw new ApplicationException("Something went wrong with the matching");
		
		ChangeInstance ci;
		
		try {
			sem.acquire();

			ci = cih.getChangeInstance(basesFile, variants1File, 
					variants2File, cp);

			sem.release();
		}
		catch(Exception e) {
			sem.release();
			throw e;
		}
		
		List<List<Pair<Integer, String>>> result;
		ExecutorService executor = Executors.newSingleThreadExecutor(
				new UnsettleThreadFactory());
		MatchingCallable matcher = new MatchingCallable(mh, ci, cp);
		
		logger.info("Starting matching for " + cp.getConflictName() + "...");
		
		Future<List<List<Pair<Integer, String>>>> future = executor.submit(matcher);

		try {
			result = future.get(PropertiesHandler.getInstance().getMatchingBudget(), TimeUnit.SECONDS);
		}
		catch (TimeoutException ex) {
			logger.info("Ran out of time for matching " + cp.getConflictName() + "...");
			return new ArrayList<>();
		} 
		catch (InterruptedException e) {
			logger.info("Interruption in matching for " + cp.getConflictName() + "...");
			return new ArrayList<>();
		} 
		catch (ExecutionException e) {
			logger.info("Something went wrong in matching for " + cp.getConflictName() + "...");
			logger.warn(e);
			return new ArrayList<>();
		}

		logger.info("Finished matching for " + cp.getConflictName() + "...");
		
		return result;
	}

	public List<Pair<String, List<String>>> getTestingGoals(){
		return mh.getTestingGoals();
	}
	
	public String getConflictName() {
		return cp.getConflictName();
	}
}

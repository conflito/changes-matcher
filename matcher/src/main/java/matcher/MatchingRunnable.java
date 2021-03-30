package matcher;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;

import org.apache.log4j.Logger;

import matcher.entities.ChangeInstance;
import matcher.exceptions.ApplicationException;
import matcher.handlers.ChangeInstanceHandler;
import matcher.handlers.MatchingHandler;
import matcher.patterns.ConflictPattern;
import matcher.utils.Pair;

public class MatchingRunnable implements Callable<List<List<Pair<Integer, String>>>>{

	private final static Logger logger = Logger.getLogger(MatchingRunnable.class);

	private Semaphore sem;
	
	private ChangeInstanceHandler cih;
	private MatchingHandler mh;

	private File[] basesFile;
	private File[] variants1File;
	private File[] variants2File;

	private ConflictPattern cp;

	public MatchingRunnable(File[] basesFile, File[] variants1File, File[] variants2File) {
		super();
		this.basesFile = basesFile;
		this.variants1File = variants1File;
		this.variants2File = variants2File;

		cih = new ChangeInstanceHandler();
		mh = new MatchingHandler();
	}

	public void setConflictPattern(ConflictPattern cp) {
		this.cp = cp;
	}
	
	public void setSem(Semaphore sem) {
		this.sem = sem;
	}

	@Override
	public List<List<Pair<Integer, String>>> call() throws Exception {
		if(cp == null) 
			throw new ApplicationException("Null conflict pattern.");
		
		sem.acquire();
	
		ChangeInstance ci = cih.getChangeInstance(basesFile, variants1File, 
				variants2File, cp);
		
		sem.release();
		
		logger.info("Starting matching for " + cp.getConflictName() + "...");
		return mh.matchingAssignments(ci, cp);
	}

	public List<Pair<String, List<String>>> getTestingGoals(){
		return mh.getTestingGoals();
	}
}

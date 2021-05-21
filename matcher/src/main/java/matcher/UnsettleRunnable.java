package matcher;

import java.io.File;
import java.util.List;
import java.util.concurrent.Semaphore;

import org.apache.log4j.Logger;

import matcher.entities.ChangeInstance;
import matcher.handlers.ChangeInstanceHandler;
import matcher.handlers.MatchingHandler;
import matcher.patterns.ConflictPattern;
import matcher.utils.Pair;

public class UnsettleRunnable implements Runnable{

	private final static Logger logger = Logger.getLogger(UnsettleRunnable.class);

	private Semaphore sem;
	
	private ChangeInstanceHandler cih;
	private MatchingHandler mh;

	private File[] basesFile;
	private File[] variants1File;
	private File[] variants2File;

	private ConflictPattern cp;
	
	public UnsettleRunnable(File[] basesFile, File[] variants1File, 
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
	public void run() {
		if(cp == null || sem == null)
			return ;
		
		ChangeInstance ci = null;
		
		try {
			sem.acquire();
			ci = cih.getChangeInstance(basesFile, variants1File, 
					variants2File, cp);
			sem.release();
		} catch (Exception e) {
			return ;
		}
		
		logger.info("Starting matching for " + cp.getConflictName() + "...");
		
		mh.matchingAssignments(ci, cp);
		
		List<Pair<String, List<String>>> testingGoals = mh.getTestingGoals();
		for(Pair<String, List<String>> goal: testingGoals) {
			String targetClass = goal.getFirst();
			List<String> targetMethods = goal.getSecond();
			try {
				EvoSuiteCommand cmd = new EvoSuiteCommand(targetClass, targetMethods);
				
				logger.info("Starting test generation for an instance of " + 
						cp.getConflictName() + "...");

				//TODO Run EvoSuite with the cmd
				
			} catch (Exception e) {
				continue;
			}
		}
		
		
	}

}

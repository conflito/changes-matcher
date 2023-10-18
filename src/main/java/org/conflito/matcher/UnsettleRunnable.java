package org.conflito.matcher;

import java.io.File;
import java.util.List;
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

public class UnsettleRunnable implements Runnable {

  private final static Logger logger = Logger.getLogger(UnsettleRunnable.class);

  private final Semaphore sem;

  private final ChangeInstanceHandler cih;
  private final MatchingHandler mh;

  private final File[] basesFile;
  private final File[] variants1File;
  private final File[] variants2File;

  private final ConflictPattern cp;

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
		if (cp == null || sem == null) {
			return;
		}

    ChangeInstance ci = null;

    try {
      sem.acquire();
      ci = cih.getChangeInstance(basesFile, variants1File,
          variants2File, cp);
      sem.release();
    } catch (Exception e) {
      sem.release();
      return;
    }

    ExecutorService executor = Executors.newSingleThreadExecutor(
        new UnsettleThreadFactory());
    MatchingCallable matcher = new MatchingCallable(mh, ci, cp);

    logger.info("Starting matching for " + cp.getConflictName() + "...");

    Future<List<List<Pair<Integer, String>>>> future = executor.submit(matcher);

    try {
      future.get(PropertiesHandler.getInstance().getMatchingBudget(), TimeUnit.SECONDS);
    } catch (TimeoutException ex) {
      logger.info("Ran out of time for matching " + cp.getConflictName() + "...");
      return;
    } catch (InterruptedException e) {
      logger.info("Interruption in matching for " + cp.getConflictName() + "...");
      return;
    } catch (ExecutionException e) {
      logger.info("Something went wrong in matching for " + cp.getConflictName() + "...");
      logger.warn(e);
      return;
    } catch (ApplicationException e) {
      logger.info(e.getMessage());
      return;
    }
    logger.info("Finished matching for " + cp.getConflictName() + "...");
    runEvoSuite();
  }

  private void runEvoSuite() {
    List<Pair<String, List<String>>> testingGoals = mh.getTestingGoals();
    for (Pair<String, List<String>> goal : testingGoals) {
      String targetClass = goal.getFirst();
      List<String> targetMethods = goal.getSecond();
      try {
        EvoSuiteCommand cmd = new EvoSuiteCommand(targetClass, targetMethods);

        logger.info("Starting test generation for an instance of " +
            cp.getConflictName() + "...");

        cmd.run();

        logger.info("Finished test generation for an instance of " +
            cp.getConflictName() + "...");

      } catch (Exception e) {
        logger.error(e.getMessage());
        continue;
      }
    }
  }

}

package org.conflito.matcher;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.conflito.matcher.catalogs.ConflictPatternCatalog;
import org.conflito.matcher.exceptions.ApplicationException;
import org.conflito.matcher.handlers.FileSystemHandler;
import org.conflito.matcher.handlers.InstancesCache;
import org.conflito.matcher.handlers.PropertiesHandler;
import org.conflito.matcher.handlers.SpoonHandler;
import org.conflito.matcher.patterns.ConflictPattern;
import org.conflito.matcher.utils.Pair;

public class Matcher {

  private final static Logger logger = Logger.getLogger(Matcher.class);

  private final ConflictPatternCatalog conflictsCatalog;

  private final List<Pair<String, List<String>>> testingGoals;

  private final List<String> matchedConflicts;

  public Matcher(String configFilePath) throws ApplicationException {
    PropertiesHandler.createInstance(configFilePath);
    FileSystemHandler.createInstance();
    SpoonHandler.createInstance();
    InstancesCache.createInstance();

    conflictsCatalog = new ConflictPatternCatalog();
    testingGoals = new ArrayList<>();
    matchedConflicts = new ArrayList<String>();
  }

  public static List<String> patternNames() throws ApplicationException {
    return new ConflictPatternCatalog().getPatternsNames();
  }

  public void match(String[] bases, String[] variants1, String[] variants2)
      throws ApplicationException {
    if (bases == null || variants1 == null || variants2 == null) {
      throw new ApplicationException("Invalid arguments: null");
    }
    if (!sameLength(bases, variants1, variants2)) {
      throw new ApplicationException("Invalid arguments: different array sizes");
    }

    File[] basesFile = fromStringArray(bases);
    File[] variants1File = fromStringArray(variants1);
    File[] variants2File = fromStringArray(variants2);

    SpoonHandler.getInstance().loadLaunchers(basesFile, variants1File,
        variants2File);

    ExecutorService es = Executors.newFixedThreadPool(
        PropertiesHandler.getInstance().getNumberThreads(),
        new UnsettleThreadFactory());
    Semaphore sem = new Semaphore(1);

    for (ConflictPattern cp : conflictsCatalog.getPatterns()) {
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
      String conflictName) throws ApplicationException {

    if (bases == null || variants1 == null || variants2 == null
        || conflictName == null) {
      throw new ApplicationException("Invalid arguments: null");
    }

    if (!sameLength(bases, variants1, variants2)) {
      throw new ApplicationException("Invalid arguments: different array sizes");
    }

    if (!conflictsCatalog.hasPattern(conflictName)) {
      throw new ApplicationException("Unknown pattern name!");
    }

    File[] basesFile = fromStringArray(bases);
    File[] variants1File = fromStringArray(variants1);
    File[] variants2File = fromStringArray(variants2);

    SpoonHandler.getInstance().loadLaunchers(basesFile, variants1File,
        variants2File);

    ExecutorService es = Executors.newCachedThreadPool(new UnsettleThreadFactory());
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
      throws ApplicationException {
    if (bases == null || variants1 == null || variants2 == null) {
      throw new ApplicationException("Invalid arguments: null");
    }

    if (!sameLength(bases, variants1, variants2)) {
      throw new ApplicationException("Invalid arguments: different array sizes");
    }

    File[] basesFile = fromStringArray(bases);
    File[] variants1File = fromStringArray(variants1);
    File[] variants2File = fromStringArray(variants2);

    SpoonHandler.getInstance().loadLaunchers(basesFile, variants1File,
        variants2File);

    List<List<Pair<Integer, String>>> result = new ArrayList<>();

    ExecutorService es = Executors.newFixedThreadPool(
        PropertiesHandler.getInstance().getNumberThreads(),
        new UnsettleThreadFactory());

    List<MatchingRunnable> runnables = new ArrayList<>();
    List<Future<List<List<Pair<Integer, String>>>>> futures = new ArrayList<>();

    Semaphore sem = new Semaphore(1);

    for (ConflictPattern cp : conflictsCatalog.getPatterns()) {
      MatchingRunnable mt = new MatchingRunnable(basesFile, variants1File,
          variants2File, cp, sem);

      runnables.add(mt);
      futures.add(es.submit(mt));
    }
    es.shutdown();

    try {
      es.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
    } catch (InterruptedException e) {

    }

    for (int i = 0; i < futures.size(); i++) {
      Future<List<List<Pair<Integer, String>>>> future = futures.get(i);
      MatchingRunnable mt = runnables.get(i);
      try {
        List<List<Pair<Integer, String>>> assignment = future.get();
        if (!assignment.isEmpty()) {
          result.addAll(assignment);
          testingGoals.addAll(mt.getTestingGoals());
          for (Object e : assignment) {
            matchedConflicts.add(mt.getConflictName());
          }
        }
      } catch (InterruptedException e) {
        logger.warn("Interruption...");
      } catch (ExecutionException e) {
        logger.error("Error in execution for " + mt.getConflictName());
        throw new ApplicationException("Error in execution");
      }
    }

    return result;
  }

  public List<List<Pair<Integer, String>>> matchingAssignments(String[] bases,
      String[] variants1, String[] variants2, String conflictName)
      throws ApplicationException {
    if (conflictName == null) {
      throw new ApplicationException("Invalid arguments: null");
    }

    if (!conflictsCatalog.hasPattern(conflictName)) {
      throw new ApplicationException("Unknown pattern name!");
    }

    return matchingAssignments(bases, variants1, variants2,
        conflictsCatalog.getPattern(conflictName));
  }

  public List<List<Pair<Integer, String>>> matchingAssignments(String[] bases,
      String[] variants1, String[] variants2, ConflictPattern cp)
      throws ApplicationException {
    if (bases == null || variants1 == null || variants2 == null) {
      throw new ApplicationException("Invalid arguments: null");
    }

    if (!sameLength(bases, variants1, variants2)) {
      throw new ApplicationException("Invalid arguments: different array sizes");
    }

    File[] basesFile = fromStringArray(bases);
    File[] variants1File = fromStringArray(variants1);
    File[] variants2File = fromStringArray(variants2);

    SpoonHandler.getInstance().loadLaunchers(basesFile, variants1File,
        variants2File);

    ExecutorService es = Executors.newCachedThreadPool(new UnsettleThreadFactory());

    Semaphore sem = new Semaphore(1);

    MatchingRunnable mt = new MatchingRunnable(basesFile, variants1File,
        variants2File, cp, sem);

    Future<List<List<Pair<Integer, String>>>> future = es.submit(mt);

    try {
      List<List<Pair<Integer, String>>> result = future.get();
      if (!result.isEmpty()) {
        testingGoals.addAll(mt.getTestingGoals());
        for (Object e : result) {
          matchedConflicts.add(mt.getConflictName());
        }
      }
      return result;
    } catch (InterruptedException e) {
      throw new ApplicationException("Interrupted execution");
    } catch (ExecutionException e) {
      throw new ApplicationException("Something went wrong");
    }
  }

  public List<Pair<String, List<String>>> getTestingGoals() {
    return this.testingGoals;
  }

  public List<String> getMatchedConflicts() {
    return this.matchedConflicts;
  }

  private boolean sameLength(String[] bases, String[] variants1, String[] variants2) {
    return bases.length == variants1.length && bases.length == variants2.length;
  }

  private File[] fromStringArray(String[] filePaths) throws ApplicationException {
    File[] result = new File[filePaths.length];
    for (int i = 0; i < filePaths.length; i++) {
      String path = filePaths[i];
      if (path != null) {
        File f = new File(path);
        if (!f.exists()) {
          throw new ApplicationException("File " + f.getAbsolutePath() +
              " does not exist.");
        }
        result[i] = f;

      }
    }
    return result;
  }
}

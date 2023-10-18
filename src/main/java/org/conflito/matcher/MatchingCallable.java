package org.conflito.matcher;

import java.util.List;
import java.util.concurrent.Callable;
import org.conflito.matcher.entities.ChangeInstance;
import org.conflito.matcher.exceptions.ApplicationException;
import org.conflito.matcher.handlers.MatchingHandler;
import org.conflito.matcher.patterns.ConflictPattern;
import org.conflito.matcher.utils.Pair;

public class MatchingCallable implements Callable<List<List<Pair<Integer, String>>>> {

  private final MatchingHandler mh;

  private final ChangeInstance ci;
  private final ConflictPattern cp;

  public MatchingCallable(MatchingHandler mh, ChangeInstance ci, ConflictPattern cp) {
    this.mh = mh;
    this.ci = ci;
    this.cp = cp;
  }

  @Override
  public List<List<Pair<Integer, String>>> call() throws Exception {
		if (ci == null) {
			throw new ApplicationException("Missing change instance.");
		}
		if (cp == null) {
			throw new ApplicationException("Missing conflict pattern.");
		}
    return mh.matchingAssignments(ci, cp);
  }


}

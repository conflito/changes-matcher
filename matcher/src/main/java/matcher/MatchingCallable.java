package matcher;

import java.util.List;
import java.util.concurrent.Callable;

import matcher.entities.ChangeInstance;
import matcher.exceptions.ApplicationException;
import matcher.handlers.MatchingHandler;
import matcher.patterns.ConflictPattern;
import matcher.utils.Pair;

public class MatchingCallable implements Callable<List<List<Pair<Integer, String>>>>{

	private MatchingHandler mh;
	
	private ChangeInstance ci;
	private ConflictPattern cp;
	
	public MatchingCallable(MatchingHandler mh, ChangeInstance ci, ConflictPattern cp) {
		this.mh = mh;
		this.ci = ci;
		this.cp = cp;
	}
	
	@Override
	public List<List<Pair<Integer, String>>> call() throws Exception {
		if(ci == null)
			throw new ApplicationException("Missing change instance.");
		if(cp == null)
			throw new ApplicationException("Missing conflict pattern.");
		return mh.matchingAssignments(ci, cp);
	}

	
	
}

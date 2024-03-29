package org.conflito.matcher.output;

import java.util.List;
import org.conflito.matcher.exceptions.ApplicationException;
import org.conflito.matcher.utils.Pair;

public interface Outputer {

  void write(List<List<Pair<Integer, String>>> assignments,
      List<Pair<String, List<String>>> testingGoals,
      List<String> matchedConflicts) throws ApplicationException;

  void write(String text) throws ApplicationException;
}

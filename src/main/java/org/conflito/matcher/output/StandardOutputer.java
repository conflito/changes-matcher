package org.conflito.matcher.output;

import java.util.List;
import org.conflito.matcher.exceptions.ApplicationException;
import org.conflito.matcher.output.json.JsonBuilder;
import org.conflito.matcher.utils.Pair;

public class StandardOutputer implements Outputer {

  @Override
  public void write(List<List<Pair<Integer, String>>> assignments,
      List<Pair<String, List<String>>> testingGoals,
      List<String> matchedConflicts) throws ApplicationException {

    System.out.println(JsonBuilder.build(assignments, testingGoals, matchedConflicts));
  }

  @Override
  public void write(String text) throws ApplicationException {
    System.out.println(text);
  }

}

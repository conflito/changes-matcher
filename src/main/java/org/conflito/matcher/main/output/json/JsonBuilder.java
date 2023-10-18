package org.conflito.matcher.main.output.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.conflito.matcher.exceptions.ApplicationException;
import org.conflito.matcher.utils.Pair;

public class JsonBuilder {

  public static String build(List<List<Pair<Integer, String>>> assignments,
      List<Pair<String, List<String>>> testingGoals,
      List<String> matchedConflicts) throws ApplicationException {

    ObjectMapper mapper = new ObjectMapper();
    mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);

    DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
    prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);

    List<Entry> entries = new ArrayList<>();

    for (int i = 0; i < assignments.size(); i++) {
      String conflictName = matchedConflicts.get(i);
      List<Pair<Integer, String>> assignment = assignments.get(i);
      Pair<String, List<String>> testingGoal = testingGoals.get(i);

      Entry entry = new Entry(conflictName, assignment, testingGoal);
      entries.add(entry);
    }

    try {
      String jsonStr = mapper.writer(prettyPrinter)
          .writeValueAsString(entries);
      return jsonStr;
    } catch (JsonProcessingException e) {
      throw new ApplicationException("Something went wrong writing the JSON");
    }
  }
}

package org.conflito.matcher.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.conflito.matcher.entities.ChangeInstance;
import org.conflito.matcher.handlers.identifiers.ClassVariableIdentifier;
import org.conflito.matcher.handlers.identifiers.ConstructorVariableIdentifier;
import org.conflito.matcher.handlers.identifiers.FieldTypeVariableIdentifier;
import org.conflito.matcher.handlers.identifiers.FieldVariableIdentifier;
import org.conflito.matcher.handlers.identifiers.InterfaceVariableIdentifier;
import org.conflito.matcher.handlers.identifiers.MethodVariableIdentifier;
import org.conflito.matcher.handlers.identifiers.UpdatedEntitiesIdentifier;
import org.conflito.matcher.handlers.identifiers.VisibilityActionsIdentifier;
import org.conflito.matcher.patterns.ConflictPattern;
import org.conflito.matcher.utils.MapUtilities;
import org.conflito.matcher.utils.Pair;

public class MatchingHandler {

  @SuppressWarnings("unused")
  private final static Logger logger = Logger.getLogger(MatchingHandler.class);

  private final FieldVariableIdentifier fvi;
  private final MethodVariableIdentifier mvi;
  private final ConstructorVariableIdentifier cvi;
  private final ClassVariableIdentifier clvi;
  private final UpdatedEntitiesIdentifier uei;
  private final VisibilityActionsIdentifier vai;
  private final InterfaceVariableIdentifier ivi;
  private final FieldTypeVariableIdentifier ftvi;

  private final List<ConflictPattern> matchedPatterns;

  public MatchingHandler() {
    super();
    fvi = new FieldVariableIdentifier();
    mvi = new MethodVariableIdentifier();
    cvi = new ConstructorVariableIdentifier();
    clvi = new ClassVariableIdentifier();
    uei = new UpdatedEntitiesIdentifier();
    vai = new VisibilityActionsIdentifier();
    ivi = new InterfaceVariableIdentifier();
    ftvi = new FieldTypeVariableIdentifier();

    this.matchedPatterns = new ArrayList<>();
  }

  public List<List<Pair<Integer, String>>> matchingAssignments(ChangeInstance ci,
      ConflictPattern cp) {
    List<List<Pair<Integer, String>>> result = new ArrayList<>();
    if (!cp.fitForMatch(ci)) {
      return result;
    }

    List<Pair<Integer, List<String>>> variablePossibleValues = identifyVariableValues(ci, cp);

    calculateMatchings(ci, cp, variablePossibleValues, new ArrayList<>(), result);

    return result;
  }

  private void calculateMatchings(ChangeInstance ci, ConflictPattern cp,
      List<Pair<Integer, List<String>>> variablePossibleValues,
      List<Pair<Integer, String>> currentAssignment,
      List<List<Pair<Integer, String>>> result) {

    if (!variablePossibleValues.isEmpty()) {
      Pair<Integer, List<String>> nextVariablePairValues =
          variablePossibleValues.get(0);
      variablePossibleValues.remove(0);
      int varId = nextVariablePairValues.getFirst();
      List<String> varValues = nextVariablePairValues.getSecond();
      for (String possibleValue : varValues) {
        currentAssignment.add(new Pair<>(varId, possibleValue));
        calculateMatchings(ci, cp, new ArrayList<>(variablePossibleValues),
            new ArrayList<>(currentAssignment), result);
        currentAssignment.remove(currentAssignment.size() - 1);
      }
    } else {
      if (validAssignment(cp, currentAssignment)) {
        checkMatching(ci, cp, currentAssignment, result);
      }
    }
  }

  private void checkMatching(ChangeInstance ci, ConflictPattern cp,
      List<Pair<Integer, String>> assignment,
      List<List<Pair<Integer, String>>> result) {

    assignValues(cp, assignment);
    if (cp.matches(ci)) {
      result.add(assignment);
      matchedPatterns.add(new ConflictPattern(cp));
    }
    cp.clean();
  }

  private boolean validAssignment(ConflictPattern cp,
      List<Pair<Integer, String>> assignment) {
    for (int i = 0; i < assignment.size(); i++) {
      for (int j = i + 1; j < assignment.size(); j++) {
        Pair<Integer, String> p1 = assignment.get(i);
        Pair<Integer, String> p2 = assignment.get(j);
        if (p1.getSecond().equals(p2.getSecond()) &&
            !cp.canBeEqual(p1.getFirst(), p2.getFirst())) {
          return false;
        }
      }
    }
    return true;
  }


  public List<String> getTestBDDs() {
    return matchedPatterns.stream()
        .map(ConflictPattern::getTestBDD)
        .collect(Collectors.toList());
  }

  public List<Pair<String, List<String>>> getTestingGoals() {
    List<Pair<String, List<String>>> result = new ArrayList<>();
    for (ConflictPattern cp : matchedPatterns) {
      if (cp.hasTestingGoal()) {
        String targetClass = cp.getTestTargetClass();
        List<String> targetMethods = cp.getTestTargetMethods();
        result.add(new Pair<>(targetClass, targetMethods));
      }
    }
    return result;
  }

  private void assignValues(ConflictPattern cp, List<Pair<Integer, String>> values) {
    for (Pair<Integer, String> p : values) {
      cp.setVariableValue(p.getFirst(), p.getSecond());
    }
  }

  private List<Pair<Integer, List<String>>> identifyVariableValues(ChangeInstance ci,
      ConflictPattern cp) {
    Map<Integer, List<String>> result = fvi.identify(ci, cp);
    Map<Integer, List<String>> methods = mvi.identify(ci, cp);
    Map<Integer, List<String>> constructors = cvi.identify(ci, cp);
    Map<Integer, List<String>> classes = clvi.identify(ci, cp);
    Map<Integer, List<String>> updates = uei.identify(ci, cp);
    Map<Integer, List<String>> visibilities = vai.identify(ci, cp);
    Map<Integer, List<String>> interfaces = ivi.identify(ci, cp);
    Map<Integer, List<String>> fieldTypes = ftvi.identify(ci, cp);
    MapUtilities.mergeMaps(result, methods);
    MapUtilities.mergeMaps(result, constructors);
    MapUtilities.mergeMaps(result, classes);
    MapUtilities.mergeMaps(result, updates);
    MapUtilities.mergeMaps(result, visibilities);
    MapUtilities.mergeMaps(result, interfaces);
    MapUtilities.mergeMaps(result, fieldTypes);
    return toPairList(result);
  }

  private List<Pair<Integer, List<String>>> toPairList(Map<Integer, List<String>> values) {
    List<Pair<Integer, List<String>>> result = new ArrayList<>();
    for (Entry<Integer, List<String>> e : values.entrySet()) {
      Pair<Integer, List<String>> p = new Pair<>(e.getKey(), e.getValue());
      result.add(p);
    }
    return result;
  }

}

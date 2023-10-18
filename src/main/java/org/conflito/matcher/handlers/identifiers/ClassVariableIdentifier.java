package org.conflito.matcher.handlers.identifiers;

import java.util.List;
import java.util.Map;
import org.conflito.matcher.entities.ChangeInstance;
import org.conflito.matcher.patterns.ConflictPattern;
import org.conflito.matcher.utils.MapUtilities;

public class ClassVariableIdentifier implements IVariableIdentifier {

  @Override
  public Map<Integer, List<String>> identify(ChangeInstance changeInstance,
      ConflictPattern conflictPattern) {

    Map<Integer, List<String>> result = classesInBase(changeInstance, conflictPattern);
    MapUtilities.mergeMaps(result, classesInDeltas(changeInstance, conflictPattern));
    return result;
  }

  private Map<Integer, List<String>> classesInBase(ChangeInstance changeInstance,
      ConflictPattern conflictPattern) {
    List<Integer> vars = conflictPattern.getClassVariableIds();
    List<String> cs = changeInstance.getClassQualifiedNames();

    return MapUtilities.combine(vars, cs);
  }

  private Map<Integer, List<String>> classesInDeltas(ChangeInstance changeInstance,
      ConflictPattern conflictPattern) {
    List<Integer> vars = conflictPattern.getDeltaClassesVariableIds();
    List<String> cs = changeInstance.getDeltaClassesQualifiedNames();
    return MapUtilities.combine(vars, cs);
  }

}

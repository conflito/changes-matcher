package org.conflito.matcher.handlers.identifiers;

import java.util.List;
import java.util.Map;
import org.conflito.matcher.entities.ChangeInstance;
import org.conflito.matcher.patterns.ConflictPattern;
import org.conflito.matcher.utils.MapUtilities;

public class ConstructorVariableIdentifier implements IVariableIdentifier {

  @Override
  public Map<Integer, List<String>> identify(ChangeInstance changeInstance,
      ConflictPattern conflictPattern) {
    Map<Integer, List<String>> result =
        constructorsInBase(changeInstance, conflictPattern);
    MapUtilities.mergeMaps(result, constructorsInDeltas(changeInstance, conflictPattern));
    return result;
  }

  private Map<Integer, List<String>> constructorsInBase(ChangeInstance changeInstance,
      ConflictPattern conflictPattern) {
    List<Integer> vars = conflictPattern.getConstructorsVariableIds();
    List<String> cs = changeInstance.getConstructorsQualifiedNames();

    return MapUtilities.combine(vars, cs);
  }

  private Map<Integer, List<String>> constructorsInDeltas(ChangeInstance changeInstance,
      ConflictPattern conflictPattern) {
    List<Integer> vars = conflictPattern.getDeltaConstructorsVariableIds();
    List<String> cs = changeInstance.getDeltaConstructorsQualifiedNames();
    return MapUtilities.combine(vars, cs);
  }

}

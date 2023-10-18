package org.conflito.matcher.entities.deltas;

import org.conflito.matcher.entities.Visibility;

public interface Visible {

  String getQualifiedName();

  Visibility getVisibility();
}

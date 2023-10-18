package org.conflito.matcher.utils;

public class Pair<T, K> {

  private final T first;
  private final K second;

  public Pair(T first, K second) {
    super();
    this.first = first;
    this.second = second;
  }

  public T getFirst() {
    return first;
  }

  public K getSecond() {
    return second;
  }


  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((first == null) ? 0 : first.hashCode());
    result = prime * result + ((second == null) ? 0 : second.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Pair other = (Pair) obj;
    if (first == null) {
      if (other.first != null) {
        return false;
      }
    } else if (!first.equals(other.first)) {
      return false;
    }
    if (second == null) {
      return other.second == null;
    } else
      return second.equals(other.second);
  }

  public String toString() {
    return "(" + first.toString() + ", " + second.toString() + ")";
  }
}

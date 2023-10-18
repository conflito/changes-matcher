package org.conflito.matcher.processors;

public interface Processor<K, T> {

  K process(T element);
}

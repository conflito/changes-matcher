package org.conflito.matcher;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class MatchingThreadFactory implements ThreadFactory {

  @Override
  public Thread newThread(Runnable r) {
    Thread t = Executors.defaultThreadFactory().newThread(r);
    t.setDaemon(true);
    return t;
  }
}

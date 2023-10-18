package org.conflito.matcher.main.output;

public class OutputerFactory {

  private static final OutputerFactory instance = new OutputerFactory();

  private OutputerFactory() {
  }

  public Outputer getOutputer() {
    return new StandardOutputer();
  }

  public Outputer getOutputer(String filename) {
    return new FileOutputer(filename);
  }

  public static OutputerFactory getInstance() {
    return instance;
  }
}

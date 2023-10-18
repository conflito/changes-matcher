package org.conflito.matcher.output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.conflito.matcher.exceptions.ApplicationException;
import org.conflito.matcher.output.json.JsonBuilder;
import org.conflito.matcher.utils.Pair;

public class FileOutputer implements Outputer {

  private final String filename;

  public FileOutputer(String filename) {
    this.filename = filename;
  }

  @Override
  public void write(List<List<Pair<Integer, String>>> assignments,
      List<Pair<String, List<String>>> testingGoals,
      List<String> matchedConflicts) throws ApplicationException {

    File fileOut = new File(filename);
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileOut))) {
      bw.write(JsonBuilder.build(assignments, testingGoals, matchedConflicts));
    } catch (IOException e) {
      throw new ApplicationException("Something went wrong writing to output file");
    }
  }

  @Override
  public void write(String text) throws ApplicationException {
    File fileOut = new File(filename);
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileOut))) {
      bw.write(text);
    } catch (IOException e) {
      throw new ApplicationException("Something went wrong writing to output file");
    }
  }

}

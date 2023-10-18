package org.conflito.matcher;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.conflito.matcher.exceptions.ApplicationException;
import org.conflito.matcher.handlers.PropertiesHandler;
import org.conflito.matcher.output.Outputer;
import org.conflito.matcher.output.OutputerFactory;
import org.conflito.matcher.utils.Pair;

public class Main {

  private static final String name = "Changes-Matcher";

  public static void main(String[] args) throws ApplicationException {

    CommandLine cmd = getCommandLine(args);

    if (cmd.hasOption("ch")) {
      dumpConfigFileTemplate();
      System.exit(0);
    } else if (cmd.hasOption("l")) {
      Outputer out;
      if (cmd.hasOption("out")) {
        out = OutputerFactory.getInstance()
            .getOutputer(cmd.getOptionValue("out"));
      } else {
        out = OutputerFactory.getInstance().getOutputer();
      }
      List<String> names = Matcher.patternNames();
      Collections.sort(names);
      String text = String.join("\n", names);
      out.write(text);
      System.exit(0);
    }

    String[] baseFilePaths = cmd.getOptionValue('b').split(";", -1);
    String[] var1FilePaths = cmd.getOptionValue("v1").split(";", -1);
    String[] var2FilePaths = cmd.getOptionValue("v2").split(";", -1);
    String configFilePath = cmd.getOptionValue('c');

    turnEmptyToNull(baseFilePaths);
    turnEmptyToNull(var1FilePaths);
    turnEmptyToNull(var2FilePaths);
    Matcher matcher = new Matcher(configFilePath);

    List<List<Pair<Integer, String>>> result;
    if (cmd.hasOption("cn")) {
      String conflictName = cmd.getOptionValue("cn");
      result = matcher.matchingAssignments(baseFilePaths, var1FilePaths,
          var2FilePaths, conflictName);
    } else {
      result = matcher.matchingAssignments(baseFilePaths, var1FilePaths,
          var2FilePaths);
    }

    Outputer out;
    if (cmd.hasOption("out")) {
      out = OutputerFactory.getInstance()
          .getOutputer(cmd.getOptionValue("out"));
    } else {
      out = OutputerFactory.getInstance().getOutputer();
    }

    out.write(result, matcher.getTestingGoals(), matcher.getMatchedConflicts());
  }

  private static void turnEmptyToNull(String[] a) {
    for (int i = 0; i < a.length; i++) {
      if (a[i].equals("")) {
        a[i] = null;
      }
    }
  }

  private static CommandLine getCommandLine(String[] args) {
    Options options = getOptions();
    CommandLineParser parser = new DefaultParser();
    HelpFormatter formatter = new HelpFormatter();
    CommandLine cmd = null;

    try {
      cmd = parser.parse(options, args);
    } catch (ParseException e) {
      System.out.println(e.getMessage());
      formatter.printHelp(name, options);
      System.exit(1);
    }

    return cmd;
  }

  private static Options getOptions() {
    Options options = new Options();

    Option base = new Option("b", "base", true,
        "Base file paths arg: <path>;<path>;(...)");
    base.setRequired(true);

    Option firstVariant = new Option("v1", "variant1", true,
        "First variant file paths arg: <path>;<path>;(...)");
    firstVariant.setRequired(true);

    Option secondVariant = new Option("v2", "variant2", true,
        "Second variant file paths arg: <path>;<path>;(...)");
    secondVariant.setRequired(true);

    Option configFile = new Option("c", "config", true,
        "Config file path arg");
    configFile.setRequired(true);

    Option configFileHelp = new Option("ch", "config_help", false,
        "Dumps the template for the config file");

    Option conflictName = new Option("cn", "conflict_name", true,
        "The name of the conflict to try to match");

    Option outFile = new Option("out", "output_file", true,
        "Path to the output file");

    Option listPatterns = new Option("l", "list_patterns", false,
        "List the available patterns' names");

    options.addOption(base);
    options.addOption(firstVariant);
    options.addOption(secondVariant);
    options.addOption(configFile);
    options.addOption(configFileHelp);
    options.addOption(conflictName);
    options.addOption(outFile);
    options.addOption(listPatterns);

    return options;
  }

  private static void dumpConfigFileTemplate() {
    File f = new File("UNSETTLE_config_template.properties");
    if (!f.exists()) {
      try {
        f.createNewFile();
        BufferedWriter bw = new BufferedWriter(new FileWriter(f));
        bw.append("######## Required properties ########");
        bw.newLine();
        bw.append("# Path to the source directory of the base version");
        bw.newLine();
        bw.append(PropertiesHandler.BASE_SRC_DIR_PROPERTY_KEY +
            "=some/example/path/to/replace");

        bw.newLine();
        bw.newLine();

        bw.append("# Path to the source directory of the first variant version");
        bw.newLine();
        bw.append(PropertiesHandler.VAR1_SRC_DIR_PROPERTY_KEY +
            "=some/example/path/to/replace");

        bw.newLine();
        bw.newLine();

        bw.append("# Path to the source directory of the second variant version");
        bw.newLine();
        bw.append(PropertiesHandler.VAR2_SRC_DIR_PROPERTY_KEY +
            "=some/example/path/to/replace");

        bw.newLine();
        bw.newLine();

        bw.append("# Path to the classpath directory of the first variant version");
        bw.newLine();
        bw.append(PropertiesHandler.VAR1_CP_DIR_PROPERTY_KEY +
            "=some/example/path/to/replace");

        bw.newLine();
        bw.newLine();

        bw.append("# Path to the classpath directory of the second variant version");
        bw.newLine();
        bw.append(PropertiesHandler.VAR2_CP_DIR_PROPERTY_KEY +
            "=some/example/path/to/replace");

        bw.newLine();
        bw.newLine();

        bw.append("# Path to the classpath directory of the merge version");
        bw.newLine();
        bw.append(PropertiesHandler.MERGE_CP_DIR_PROPERTY_KEY +
            "=some/example/path/to/replace");

        bw.newLine();
        bw.newLine();
        bw.append("######## Optional properties ########");
        bw.newLine();
        bw.append("# Time budget in seconds (int) for the matching");
        bw.newLine();
        bw.append("#" + PropertiesHandler.MATCHING_BUDGET_KEY + "=120");
        bw.newLine();
        bw.newLine();
        bw.append("# Number of threads to run in parallel");
        bw.newLine();
        bw.append("#" + PropertiesHandler.THREAD_NUMBER_KEY + "=2");

        bw.close();
      } catch (IOException e) {
        System.out.println("Something went wrong creating the file.");
        System.exit(1);
      }
      System.out.println("Template config file created successfully.");
    } else {
      System.out.println("A file named 'UNSETTLE_config_template.properties' already "
          + "exists. Stopping file creation.");
    }
  }
}

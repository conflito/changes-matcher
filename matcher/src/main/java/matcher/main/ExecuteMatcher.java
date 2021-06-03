package matcher.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import matcher.Matcher;
import matcher.exceptions.ApplicationException;
import matcher.handlers.PropertiesHandler;
import matcher.utils.Pair;

import org.apache.commons.cli.*;

public class ExecuteMatcher {

	public static void main(String[] args) throws ApplicationException {
		CommandLine cmd = getCommandLine(args);
		
		if(cmd.hasOption("ch")) {
			dumpConfigFileTemplate();
			System.exit(0);
		}
		
		String baseFilePaths[] = cmd.getOptionValue('b').split(";");
		String var1FilePaths[] = cmd.getOptionValue("v1").split(";");
		String var2FilePaths[] = cmd.getOptionValue("v2").split(";");
		String configFilePath = cmd.getOptionValue('c');
		
		Matcher matcher = new Matcher(configFilePath);
		
		if(cmd.hasOption("mo")) {
			List<List<Pair<Integer, String>>> result;
			if(cmd.hasOption("cn")) {
				String conflictName = cmd.getOptionValue("cn");
				result = matcher.matchingAssignments(baseFilePaths, var1FilePaths, 
						var2FilePaths, conflictName);
			}
			else
				result = matcher.matchingAssignments(baseFilePaths, var1FilePaths, 
						var2FilePaths);
			System.out.println(result);
			System.out.println(matcher.getTestingGoals());
		}
		else {
			if(cmd.hasOption("cn")) {
				String conflictName = cmd.getOptionValue("cn");
				matcher.match(baseFilePaths, var1FilePaths, var2FilePaths, 
						conflictName);
			}
			else
				matcher.match(baseFilePaths, var1FilePaths, var2FilePaths);
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
			formatter.printHelp("utility-name", options);
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
		
		Option matchOnly = new Option("mo", "match_only", false,
				"Only performs the matching, it does not generate tests");
		
		Option conflictName = new Option("cn", "conflict_name", true,
				"The name of the conflict to try to match");
		
		options.addOption(base);
		options.addOption(firstVariant);
		options.addOption(secondVariant);
		options.addOption(configFile);
		options.addOption(configFileHelp);
		options.addOption(matchOnly);
		options.addOption(conflictName);
		
		return options;
	}
	
	private static void dumpConfigFileTemplate() {
		File f = new File("UNSETTLE_config_template.properties");
		if(!f.exists()) {
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
				bw.append("# Threshold value (double) to consider two versions "
						+ "'different enough' during test generation");
				bw.newLine();
				bw.append("#" + PropertiesHandler.DISTANCE_THRESHOLD_KEY + 
						"=0.05");
				bw.newLine();
				bw.newLine();
				bw.append("# Time budget in seconds (int) for the test generation");
				bw.newLine();
				bw.append("#" + PropertiesHandler.TIME_BUDGET_KEY + "=60");
				bw.newLine();
				bw.newLine();
				bw.append("# If the classpath is in jar form");
				bw.newLine();
				bw.append("#" + PropertiesHandler.JAR_CLASSPATH + "=true");
				
				
				bw.close();
			} catch (IOException e) {
				System.out.println("Something went wrong creating the file.");
				System.exit(1);
			}
			System.out.println("Template config file created successfully.");
		}
		else
			System.out.println("A file named 'UNSETTLE_config_template.properties' already "
					+ "exists. Stopping file creation.");
	}
}

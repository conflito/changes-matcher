package matcher.main.output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import matcher.exceptions.ApplicationException;
import matcher.main.output.json.JsonBuilder;
import matcher.utils.Pair;

public class FileOutputer implements Outputer{

	private String filename;
	
	public FileOutputer(String filename) {
		this.filename = filename;
	}
	
	@Override
	public void write(List<List<Pair<Integer, String>>> assignments, 
			List<Pair<String, List<String>>> testingGoals,
			List<String> matchedConflicts) throws ApplicationException {
			
		File fileOut = new File(filename);
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(fileOut))) {
			bw.write(JsonBuilder.build(assignments, testingGoals, matchedConflicts));
		} catch (IOException e) {
			throw new ApplicationException("Something went wrong writing to output file");
		}
	}

}

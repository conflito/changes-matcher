package matcher.main.output;

import java.util.List;

import matcher.exceptions.ApplicationException;
import matcher.main.output.json.JsonBuilder;
import matcher.utils.Pair;

public class StandardOutputer implements Outputer {

	@Override
	public void write(List<List<Pair<Integer, String>>> assignments, 
			List<Pair<String, List<String>>> testingGoals,
			List<String> matchedConflicts) throws ApplicationException {

		System.out.println(JsonBuilder.build(assignments, testingGoals, matchedConflicts));
	}

	@Override
	public void write(String text) throws ApplicationException {
		System.out.println(text);		
	}

}

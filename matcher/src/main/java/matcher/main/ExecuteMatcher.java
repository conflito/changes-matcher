package matcher.main;

import java.util.List;

import matcher.Matcher;
import matcher.exceptions.ApplicationException;
import matcher.utils.Pair;

public class ExecuteMatcher {

	public static void main(String[] args) throws ApplicationException {
		if(args.length == 4) {
			String baseFilePath = args[0];
			String var1FilePath = args[1];
			String var2FilePath = args[2];
			String configFilePath = args[3];
			
			Matcher matcher = new Matcher(configFilePath);
			
			String[] bases = {baseFilePath};
			String[] variants1 = {var1FilePath};
			String[] variants2 = {var2FilePath};
			
			List<List<Pair<Integer, String>>> result = 
					matcher.matchingAssignments(bases, variants1, variants2);
			
			System.out.println(result);
		}
	}
}

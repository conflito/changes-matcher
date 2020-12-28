package matcher.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import matcher.entities.ChangeInstance;
import matcher.handlers.identifiers.ClassVariableIdentifier;
import matcher.handlers.identifiers.ConstructorVariableIdentifier;
import matcher.handlers.identifiers.FieldVariableIdentifier;
import matcher.handlers.identifiers.MethodVariableIdentifier;
import matcher.patterns.ConflictPattern;
import matcher.utils.MapUtilities;
import matcher.utils.Pair;

public class MatchingHandler {

	private FieldVariableIdentifier fvi;
	private MethodVariableIdentifier mvi;
	private ConstructorVariableIdentifier cvi;
	private ClassVariableIdentifier clvi;

	public MatchingHandler() {
		super();
		fvi = new FieldVariableIdentifier();
		mvi = new MethodVariableIdentifier();
		cvi = new ConstructorVariableIdentifier();
		clvi = new ClassVariableIdentifier();
	}
	
	public List<List<Pair<Integer, String>>> matchingAssignments(ChangeInstance ci, ConflictPattern cp){
		List<List<Pair<Integer, String>>> result = new ArrayList<>();
		List<Pair<Integer, List<String>>> variablePossibleValues = variableValues(ci, cp);
		List<List<Pair<Integer, String>>> combinations = calculateResults(variablePossibleValues);
		for(List<Pair<Integer, String>> l: combinations) {
			assignValues(cp, l);
			if(cp.matches(ci))
				result.add(l);
		}
		return result;
	}
	
	private void assignValues(ConflictPattern cp, List<Pair<Integer, String>> values) {
		for(Pair<Integer, String> p: values) {
			cp.setVariableValue(p.getFirst(), p.getSecond());
		}
	}

	private List<Pair<Integer, List<String>>> variableValues(ChangeInstance ci, ConflictPattern cp){
		Map<Integer, List<String>> result = fvi.identify(ci, cp);
		Map<Integer, List<String>> methods = mvi.identify(ci, cp);
		Map<Integer, List<String>> constructors = cvi.identify(ci, cp);
		Map<Integer, List<String>> classes = clvi.identify(ci, cp);
		MapUtilities.mergeMaps(result, methods);
		MapUtilities.mergeMaps(result, constructors);
		MapUtilities.mergeMaps(result, classes);
		return toPairList(result);
	}

	private List<Pair<Integer, List<String>>> toPairList(Map<Integer, List<String>> values) {
		List<Pair<Integer, List<String>>> result = new ArrayList<>();
		for(Entry<Integer, List<String>> e: values.entrySet()) {
			Pair<Integer, List<String>> p = new Pair<>(e.getKey(), e.getValue());
			result.add(p);
		}
		return result;
	}

	private List<List<Pair<Integer, String>>> calculateResults(List<Pair<Integer, List<String>>> values) {
		List<List<Pair<Integer, String>>> result = new ArrayList<>();
		calculateResults(values, result, new ArrayList<>());
		return result;
	}
	
	private void calculateResults(List<Pair<Integer, List<String>>> values, 
			List<List<Pair<Integer, String>>> result, List<Pair<Integer, String>> curr) {
		if(!values.isEmpty()) {
			Pair<Integer, List<String>> p = values.get(0);
			values.remove(0);
			int varId = p.getFirst();
			List<String> varValues = p.getSecond();
			for(String s: varValues) {
				curr.add(new Pair<>(varId, s));
				calculateResults(forwardCheck(values, s), result, new ArrayList<>(curr));
				curr.remove(curr.size()-1);
			}
		}
		else {
			result.add(curr);
		}
	}
	
	private List<Pair<Integer, List<String>>> forwardCheck(List<Pair<Integer, List<String>>> values, 
			String value) {
		List<Pair<Integer, List<String>>> result = new ArrayList<>();
		for(Pair<Integer, List<String>> p: values) {
			List<String> trimmedValues = new ArrayList<>();
			for(String s: p.getSecond()) {
				if(!s.equals(value))
					trimmedValues.add(s);
			}
			result.add(new Pair<>(p.getFirst(), trimmedValues));
		}
		return result;
	}


}
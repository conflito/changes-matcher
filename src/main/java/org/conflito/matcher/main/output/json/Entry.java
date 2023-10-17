package org.conflito.matcher.main.output.json;

import java.util.ArrayList;
import java.util.List;

import org.conflito.matcher.utils.Pair;

public class Entry {

	private class AssignmentEntry{
		private int variable;
		private String value;

		private AssignmentEntry(int variable, String value) {
			super();
			this.variable = variable;
			this.value = value;
		}
	}

	private class GoalEntry{
		private String targetClass;
		private List<String> coverMethods;

		private String coverMethodsLine;

		public GoalEntry(String targetClass, List<String> coverMethods) {
			super();
			this.targetClass = targetClass;
			this.coverMethods = coverMethods;
			this.coverMethodsLine = String.join(":", coverMethods);
		}
	}

	private String conflictName;
	private List<AssignmentEntry> variableAssignments;
	private GoalEntry testingGoal;

	public Entry(String conflictName, List<Pair<Integer, String>> assignment,
			Pair<String, List<String>> testingGoal) {
		this.conflictName = conflictName;
		this.variableAssignments = new ArrayList<>();
		for(Pair<Integer, String> pair: assignment) {
			variableAssignments.add(
					new AssignmentEntry(pair.getFirst(), pair.getSecond()));
		}

		this.testingGoal = new GoalEntry(
				testingGoal.getFirst(), testingGoal.getSecond());

	}
}
